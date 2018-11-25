package com.example.android.googlebookslister;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BooksList extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>{

    private BookAdapter mBookAdapter;
    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=video+game+development&maxResults=25&key=AIzaSyCq3urARExdLvRVtFx1eYHmS5HLtMJtGfU";
    private static final int BOOK_LOADER_ID =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        ListView bookListView = findViewById(R.id.list);

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

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.i("onCreateLoader", "onCreateLoader: initialized");
        return new BookLoader (this, BOOK_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

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
        private String mUrl;

        public BookLoader(Context context, String url){
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Book> loadInBackground() {
            if (mUrl == null){
                return null;
        }

        List<Book> result = QueryUtils.fetchBookData(mUrl);
            return result;


        }

    }
}
