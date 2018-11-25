package com.example.android.googlebookslister;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book>{

    public BookAdapter (Context context, List<Book> books){
        super (context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_display_details, parent, false);
        }



        Book currentBook = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.titleTextView);
        titleView.setText(currentBook.getTitle());

        TextView authorTextView = listItemView.findViewById(R.id.authorTextView);
        authorTextView.setText(currentBook.getAuthor());

        TextView publisherTextView = listItemView.findViewById(R.id.publisherTextView);
        publisherTextView.setText("Published by " + currentBook.getPublisher());

        TextView dateTextView = listItemView.findViewById(R.id.dateTextView);
        dateTextView.setText(currentBook.getDate());

        return listItemView;

    }
}
