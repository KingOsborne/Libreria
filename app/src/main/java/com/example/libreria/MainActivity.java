package com.example.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar loading;
    private RecyclerView rvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = (ProgressBar) findViewById(R.id.progressInd);
        rvView = (RecyclerView) findViewById(R.id.rv_view);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView.LayoutManager constraintLayout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        rvView.setLayoutManager(constraintLayout);

        URL bookUrl;

        try {
            if(query == null || query.isEmpty()){
                bookUrl = ApiUtil.buildUrl("cooking");
            }else{
                bookUrl = new URL(query);
            }

            new FetchBooks().execute(bookUrl);
        }catch (Exception e){

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        ArrayList<String> recentList = SpUtil.getQueryList(getApplicationContext());
        int itemNum = recentList.size();

        MenuItem recentMenu;
        for(int i = 0; i < itemNum; i++){
            recentMenu = menu.add(Menu.NONE,i,Menu.NONE,recentList.get(i));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.advanced_search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + String.valueOf(position);
                String query = SpUtil.getPrefString(getApplicationContext(),preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[5];

                for(int i = 0; i < prefParams.length; i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = ApiUtil.buildSearchUrl(
                        (queryParams[0]==null) ? "" : queryParams[0],
                        (queryParams[1]==null) ? "" : queryParams[1],
                        (queryParams[2]==null) ? "" : queryParams[3],
                        (queryParams[3]==null) ? "" : queryParams[3],
                        (queryParams[4]==null) ? "" : queryParams[4]
                );

                new FetchBooks().execute(bookUrl);


                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookURL = ApiUtil.buildUrl(query);
            new FetchBooks().execute(bookURL);
        }catch (Exception e){

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class FetchBooks extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL bookURL = urls[0];
            String result = null;

            try{
                result = ApiUtil.fetchJson(bookURL);
            }catch (IOException e){

            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            TextView textError = (TextView) findViewById(R.id.txtError);
            loading.setVisibility(View.INVISIBLE);

            if(result == null){
                rvView.setVisibility(View.INVISIBLE);
                textError.setVisibility(View.VISIBLE);
            }else{
                textError.setVisibility(View.INVISIBLE);
                rvView.setVisibility(View.VISIBLE);

                ArrayList<EstanteParaLibros> libros = ApiUtil.booksFromJson(result);

                LibrosAdapter adapter = new LibrosAdapter(libros);

                adapter.notifyDataSetChanged();
                rvView.setAdapter(adapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }
    }
}
