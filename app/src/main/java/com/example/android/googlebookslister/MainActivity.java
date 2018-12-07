package com.example.android.googlebookslister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.URLEncoder;

/** Created by Bobby Reay - 12/1/2018 **/

/** This class contains code to send search string entered in search box to BookList class **/

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=video+game+development&maxResults=25";
    private static final String MAX_RESULTS = "&maxResults=40";
    public String searchQuery;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditTextView);

        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BooksList.class);
                searchQuery = getSearchUrl();

                if (searchQuery.equals("https://www.googleapis.com/books/v1/volumes?q=&maxResults=25")){
                    hideKeyboard(MainActivity.this);
                    Toast.makeText(getBaseContext(), "Please enter a search.", Toast.LENGTH_SHORT).show();
                }

                // putExtra will carry data in searchQuery variable over to BookList.java
                intent.putExtra("EXTRA_QUERY_INFO", searchQuery);
                startActivity(intent);

                Log.d("SearchQuery", searchQuery);
            }
        });
    }

    /** Method to construct search URL. Using URLEncoder.encode will format properly to create
     * a proper URL string.
     */
    public String getSearchUrl() {
        String params = searchEditText.getText().toString();
        String encode = URLEncoder.encode(params);
        String url = BASE_URL + encode + MAX_RESULTS;
        return url;
    }

    /** Method to hide the soft keyboard when search button is clicked **/
    private static void hideKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow
                (activity.getCurrentFocus().getWindowToken(), 0);
    }


}
