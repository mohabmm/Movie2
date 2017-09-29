package com.nocom.movie2;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import data.MovieContract;
import data.MovieCursorAdapter;

/**
 * Created by Moha on 9/24/2017.
 */

public class Favorites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;
    MovieCursorAdapter movieCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        getLoaderManager().initLoader(PET_LOADER, null, this);
        ListView listView = (ListView) findViewById(R.id.favoriteList);
        movieCursorAdapter = new MovieCursorAdapter(this, null);
        listView.setAdapter(movieCursorAdapter);






    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getLoaderManager().restartLoader(PET_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(this) {

                // Initialize a Cursor, this will hold all the task data
                Cursor mTaskData = null;

                // onStartLoading() is called when a loader first starts loading data
                @Override
                protected void onStartLoading() {
                    if (mTaskData != null) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mTaskData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }

                // loadInBackground() performs asynchronous loading of data
                @Override
                public Cursor loadInBackground() {
                    // Will implement to load data
                    try {
                        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                MovieContract.MovieEntry.COLUMN_ID);

                    } catch (Exception e) {
                        Log.e("ERROR", "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;


                    }
                }

                // deliverResult sends the result of the load, a Cursor, to the registered listener
                public void deliverResult(Cursor data) {
                    mTaskData = data;
                    super.deliverResult(data);
                }
            };

        }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        movieCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}
