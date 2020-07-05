package com.example.libreria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText bkTitle = (EditText) findViewById(R.id.srTitle);
        final EditText bkAuthor = (EditText) findViewById(R.id.srAuthor);
        final EditText bkPublisher = (EditText) findViewById(R.id.srPublisher);
        final EditText bkSubject = (EditText) findViewById(R.id.srSubject);
        final EditText bkIsbn = (EditText) findViewById(R.id.srISBN);
        final Button button = (Button) findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = bkTitle.getText().toString().trim();
                String author = bkAuthor.getText().toString().trim();
                String publisher = bkPublisher.getText().toString().trim();
                String subject = bkSubject.getText().toString().trim();
                String isbn = bkIsbn.getText().toString().trim();

                if(title.isEmpty() && author.isEmpty() && publisher.isEmpty() && subject.isEmpty() && isbn.isEmpty()){
                    String message = "Please Input at least one field";
                    Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                    snackbar.setDuration(3000);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                    snackbar.show();
                }else{
                    java.net.URL searchUrl = ApiUtil.buildSearchUrl(title,author,publisher,subject,isbn);

                    Context context = getApplicationContext();
                    int position = SpUtil.getPrefInt(context,SpUtil.POSITION);
                    if(position == 0 || position == 7){
                        position = 1;
                    }else{
                        position++;
                    }

                    String key = SpUtil.QUERY + String.valueOf(position);
                    String value = title + "," + author + "," + publisher + "," + subject + "," + isbn;
                    SpUtil.setPrefString(context,key,value);
                    SpUtil.setPrefInt(context, SpUtil.POSITION, position);

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("Query",searchUrl.toString());
                    startActivity(intent);
                }

            }
        });
    }
}