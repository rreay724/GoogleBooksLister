package com.example.android.googlebookslister;

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mDate;
    private String mInfoUrl;


    public Book (String title, String author, String date, String publisher, String infoUrl){
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mDate = date;
        mInfoUrl = infoUrl;

    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getDate() {
        return mDate;
    }

    public String getInfoUrl() {
        return mInfoUrl;
    }

}
