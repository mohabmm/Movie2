package com.nocom.movie2;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Looper;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Moha on 9/13/2017.
 */

public  class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieLoader.class.getName();
    private   String murl;

    public MovieLoader(Context context, String url) {
        super(context);
        murl = url;
    }




    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<Movie> loadInBackground() {

        if (Looper.myLooper()==null)
            Looper.prepare();
        // Perform the network request, parse the response, and extract a list of movies.
        List<Movie> movies = null;
        try {
            movies = QueryUtlis.fetchEarthquakeData(murl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;

    }
}
