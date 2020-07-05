package com.example.libreria;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {

    private ApiUtil(){}

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String QUERY_PARAMETER = "q";
    private static final String KEY = "key";
    private static final String API_KEY = "AIzaSyB6vAYvoueEfFq_CcMCfkrKx_WRbzgOMfA";
    private static final String TITLE = "intitle:";
    private static final String AUTHOR = "inauthor:";
    private static final String PUBLISHER = "inpublisher:";
    private static final String SUBJECT = "subject:";
    private static final String ISBN = "isbn:";


    public static URL buildUrl(String title){

        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER,title)
                .appendQueryParameter(KEY,API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildSearchUrl(String title, String author, String publisher, String subject, String isbn){
        URL url = null;
        StringBuilder sb = new StringBuilder();
        if(!title.isEmpty()) sb.append(TITLE + title + "+");
        if(!author.isEmpty()) sb.append(AUTHOR + author + "+");
        if(!publisher.isEmpty()) sb.append(PUBLISHER + publisher + "+");
        if(!subject.isEmpty()) sb.append(SUBJECT + subject + "+");
        if(!isbn.isEmpty()) sb.append(ISBN + isbn + "+");
        sb.setLength(sb.length()-1);
        String query = sb.toString();

        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER,query)
                .appendQueryParameter(KEY,API_KEY)
                .build();

        try{
            url = new URL(uri.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return url;
    }

    public static String fetchJson(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if(hasData){
                return scanner.next();
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }finally {
            connection.disconnect();
        }
    }

    public static ArrayList<EstanteParaLibros> booksFromJson(String json){

        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHDATE = "publishedDate";
        final String PAGECOUNT = "pageCount";
        final String AVGRATING = "averageRating";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";
        final String DESC = "description";
        final String IMAGELINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";

        ArrayList<EstanteParaLibros> libros = new ArrayList<EstanteParaLibros>();

        try{
            JSONObject booksObject = new JSONObject(json);
            JSONArray booksArray = booksObject.getJSONArray(ITEMS);

             for(int i = 0; i<booksArray.length(); i++){
                JSONObject singleBook = booksArray.getJSONObject(i);
                JSONObject bookVolumeInfo = singleBook.getJSONObject(VOLUMEINFO);
                 JSONObject imageLinks = null;

                 if(bookVolumeInfo.has(IMAGELINKS)){
                   imageLinks = bookVolumeInfo.getJSONObject(IMAGELINKS);
                 }

                int authorNum;

                try{
                    authorNum = bookVolumeInfo.getJSONArray(AUTHORS).length();
                }catch (Exception e){
                    authorNum = 0;
                }

                 String[] authors = new String[authorNum];
                if ( authorNum != 0) {
                    for (int j = 0; j < authorNum; j++) {
                        authors[j] = bookVolumeInfo.getJSONArray(AUTHORS).get(j).toString();
                    }
                }

                 EstanteParaLibros unLibro = new EstanteParaLibros(
                         singleBook.getString(ID),
                         bookVolumeInfo.getString(TITLE),
                         (bookVolumeInfo.isNull(SUBTITLE)?"":bookVolumeInfo.getString(SUBTITLE)),
                         (authors.length == 0 ? null : authors),
                         bookVolumeInfo.getString(PUBLISHER),
                         bookVolumeInfo.getString(PUBLISHDATE),
                         (bookVolumeInfo.isNull(PAGECOUNT) ? 0 : bookVolumeInfo.getInt(PAGECOUNT)),
                         (bookVolumeInfo.isNull(AVGRATING) ? 0 : bookVolumeInfo.getInt(AVGRATING)),
                         (bookVolumeInfo.isNull(DESC) ? "" : bookVolumeInfo.getString(DESC)),
                         (imageLinks.isNull(THUMBNAIL) ? "" : imageLinks.getString(THUMBNAIL))
                 );

                 libros.add(unLibro);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        return libros;
    }

}
