package com.example.android.googlebookslister;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/** Created by Bobby Reay - 12/1/2018 **/

/** Contains task loader and code to populate book_list layout**/

public class BooksList extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=video+game+development&maxResults=25";
    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter mBookAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        ListView bookListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.emptyView);
        bookListView.setEmptyView(mEmptyStateTextView);

        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mBookAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = mBookAdapter.getItem(position);
                Intent bookUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getInfoUrl()));
                startActivity(bookUrlIntent);
            }
        });

        /**
         * Tests network or wifi connection. If there is connection, the Loader will proceed as
         * planned, if it is not connected, it will enter "no internet connection" in empty
         * text view and set visibility of progress bar to gone.
         **/

        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager in order to interact with the loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null
            // for the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface.
            Log.i("BooksList", "onCreate: initloader initialized");
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet);
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String query = getIntent().getStringExtra("EXTRA_QUERY_INFO");

        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_books);

        mBookAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mBookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
    }

    public static class BookLoader extends AsyncTaskLoader<List<Book>> {
        private String[] mUrl;

        public BookLoader(Context context, String... url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Book> loadInBackground() {
            if (mUrl.length < 1 || mUrl[0] == null) {
                return null;
            }

            return QueryUtils.fetchBookData(mUrl[0]);
        }

    }
}
