package com.example.android.googlebookslister;


import android.graphics.Bitmap;

/** Created by Bobby Reay - 12/1/2018 **/

/** Class to define getter methods for BookAdapter.java **/

public class Book {

    private String mId;
    private String mTitle;
    private String mAuthor;
    //    private String mDate;
    private Bitmap mBookImage;
    private String mSummary;
    private String mInfoUrl;


    public Book(String id, String title, String author, Bitmap bookImage, String summary, String infoUrl) {
        mId = id;
        mTitle = title;
        mAuthor = author;
//        mDate = date;
        mBookImage = bookImage;
        mSummary = summary;
        mInfoUrl = infoUrl;


    }

    public Book(String id, String title, String author, String summary, String infoUrl) {
        mId = id;
        mTitle = title;
        mAuthor = author;
//        mDate = date;
        mSummary = summary;
        mInfoUrl = infoUrl;


    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

//    public String getDate() {
//        return mDate;
//    }

    public Bitmap getBookImage() {
        return mBookImage;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getInfoUrl() {
        return mInfoUrl;
    }

}
