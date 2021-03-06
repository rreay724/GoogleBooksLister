package com.example.android.googlebookslister;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/** Created by Bobby Reay - 12/1/2018 **/


/** Contains networking and JSON parsing code **/

public class QueryUtils {


    private static final String LOG_TAG = "QueryUtils";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /** Helper method to fetch book data and call networking code within method **/

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e("QueryUtils", "Error closing input stream", e);
        }

        List<Book> books = extractBooks(jsonResponse);
        Log.i(LOG_TAG, "fetchBookData initialized");
        return books;
    }


    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Book> extractBooks(final String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();
        String summary;

        try {
            JSONObject jsonBookObject = new JSONObject(bookJSON);
            JSONArray bookArray = jsonBookObject.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                String id = currentBook.getString("id");
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                JSONObject searchInfo = currentBook.optJSONObject("searchInfo");


                // Add an extra try block because it was not returning all results because of author
                // listing.
                try {
                    String title = volumeInfo.getString("title");

                    // If book does not have summary
                    summary = volumeInfo.optString("description");
                    if (summary == null) {
                        summary = searchInfo.optString("textSnippet");
                    }

                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    String author = extractAllAuthors(authors);

//                    String date = volumeInfo.getString("publishedDate");
                    String url = volumeInfo.getString("infoLink");

                    JSONObject imageLink = volumeInfo.getJSONObject("imageLinks");
                    String imageUrl = imageLink.getString("thumbnail");
                    // Load bitmap from image URL
                    Bitmap bookImage = makeHttpRequest(imageUrl);

                    books.add(new Book(id, title, author, bookImage, summary, url));

//                } catch (JSONException e) {
//                    Log.e(LOG_TAG, "Problem parsing author JSON results", e);
//                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Problem parsing input stream for image URL");
                }

                Log.v(LOG_TAG, "JSON data successfully parsed");
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);

        }
        return books;
    }

    /**
     * Make an HTTP request to the given imageURL and return a Bitmap as the response.
     */
    private static Bitmap makeHttpRequest(String imageUrl) throws IOException {
        Bitmap bookImage = null;
        if (imageUrl == null) {
            return bookImage;
        }

        URL url = createUrl(imageUrl);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                bookImage = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error reading bitmap inputstream");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bookImage;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem reading input stream.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** Helper method to create {@link} URL object **/
    private static final URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "createUrl: error", e);
        }
        return url;
    }

    private static String extractAllAuthors(JSONArray authorsArray) throws JSONException{
        String authorsList = null;

        if(authorsArray.length() == 0)
            authorsList = "No Author";

        for (int i = 0; i < authorsArray.length(); i++){
            if (i == 0)
                authorsList = "" + authorsArray.getString(0);
            else
                authorsList = authorsList + ", " + authorsArray.getString(i);
        }
        return authorsList;
    }
}
