package com.example.android.googlebookslister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "&key=AIzaSyCq3urARExdLvRVtFx1eYHmS5HLtMJtGfU";
    public static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    public static final String MAX_RESULTS = "&maxResults=25";
    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=video+game+development&maxResults=25&key=AIzaSyCq3urARExdLvRVtFx1eYHmS5HLtMJtGfU";
    private EditText searchEditText;

    public String searchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditTextView);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BooksList.class);
                searchQuery = getSearchUrl(BASE_URL);

                intent.putExtra("EXTRA_QUERY_INFO", searchQuery);
                startActivity(intent);

                Log.d("SearchQuery", searchQuery);



            }
        });
    }

    public String getSearchUrl(String url) {
        String params = searchEditText.getText().toString();
        params = params.replace(" ", "+");
        url += params + "&filter=ebooks&prettyPrint=false";
        return url;
    }


}
