package com.example.android.googlebookslister;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/** Created by Bobby Reay - 12/1/2018 **/


/** Contains code for Book Adapter linking views to book_display_details layout**/

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
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
        authorTextView.setText(checkForAuthor(currentBook.getAuthor()));

//        TextView dateTextView = listItemView.findViewById(R.id.dateTextView);
//        dateTextView.setText(currentBook.getDate());


        TextView summaryTextView = listItemView.findViewById(R.id.book_summary);
        summaryTextView.setText(checkForSummary(currentBook.getSummary()));

        ImageView bookThumbnailImage = listItemView.findViewById(R.id.bookImage);
        bookThumbnailImage.setImageBitmap(currentBook.getBookImage());

        return listItemView;

    }

    // Helper method to check for missing summary
    private String checkForSummary(String description) {
        if (description == null || description.isEmpty()) {
            description = "Description is not available.";
        }
        return description;
    }

    private String checkForAuthor(String authors){
        if (authors == null || authors.isEmpty()) {
            authors = "No author to display.";
        }
        return authors;
    }
}
