package com.nocom.movie2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data.MovieContract;

import static android.R.attr.id;

//import static com.nocom.movie2.R.id.overview; rm1



public class MovieActivity extends AppCompatActivity {

    public static final String Log_TAG = MovieActivity.class.getName();
    private static final String MOST_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=###&language=en-US&page=1";
    private static final String HIGHEST_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=###&language=en-US&page=1";

    private static final int MOVIE_LOADER_ID = 1;
    Bundle bundle;
    ImageView imageView;
    String searched = MOST_POPULAR;
    MovieAdapter mAdapter;
    LoaderManager loaderManager;
    TextView emptytext;
    TextView overvview;
    TextView title;
    TextView releasedate;
    TextView rating;
    int condtion = 0;
    private GridView movieListView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        overvview = (TextView) findViewById(R.id.overview);
       title = (TextView) findViewById(R.id.title);
        releasedate = (TextView) findViewById(R.id.releasedate);
        rating = (TextView) findViewById(R.id.rate);
        imageView = (ImageView) findViewById(R.id.photo2);
        movieListView = (GridView) findViewById(R.id.gridview);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        emptytext = (TextView) findViewById(R.id.empty_view);
        movieListView.setEmptyView(emptytext);movieListView.setAdapter(mAdapter);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(MOVIE_LOADER_ID, null, loaderone);
        connect();

    }

    public void connect() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.restartLoader(MOVIE_LOADER_ID, bundle, loaderone);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            emptytext.setText("No Internet Connection ");
            progressBar.setVisibility(View.INVISIBLE);
            // Update empty state with no connection error message

        }

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie currentMovie = mAdapter.getItem(position);
                Context context = MovieActivity.this;
                Class destinationActivity = Movie_detail_Activity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                String overview = currentMovie.getOverview();
                String title = currentMovie.gettitle();
                String image = currentMovie.getPoster2();
                String release = currentMovie.getreleasedate();
                double rate = currentMovie.getvoteaverge();


                Bundle b = new Bundle();
                b.putParcelable("Movie", currentMovie);
                startChildActivityIntent.putExtras(b);



                // startChildActivityIntent.putExtra("Movie", currentMovie);
                // int id = currentMovie.getId();
                String stringdouble = Double.toString(rate);
                startChildActivityIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
                //  startChildActivityIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, id);


                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, overview);
                startChildActivityIntent.putExtra(Intent.EXTRA_REFERRER, release);
                startChildActivityIntent.putExtra("image", image);
                startChildActivityIntent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, stringdouble);
                startActivity(startChildActivityIntent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.most_popular:
                if (condtion == 1) {
                    mAdapter.clear();
                    searched = MOST_POPULAR;
                    condtion = 0;
                    connect();

                } else {

                    Toast.makeText(getApplicationContext(), "Already chosen chose the another option",
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.highest_rated:
                if (condtion == 0) {
                    mAdapter.clear();
                    searched = HIGHEST_RATED;
                    condtion = 1;
                    connect();

                } else {

                    Toast.makeText(getApplicationContext(), "Already chosen chose the another option ",
                            Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.Favorite:
                Intent intent = new Intent(MovieActivity.this, Favorites.class);
                Uri currentPetUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);

        }




        return super.onOptionsItemSelected(item);
    }


    public LoaderManager.LoaderCallbacks<List<Movie>> loaderone = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            // Create a new loader for the given URL
            return new MovieLoader(MovieActivity.this, searched);

        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

            progressBar.setVisibility(View.INVISIBLE);

            mAdapter.clear();
            if (movies != null && !movies.isEmpty()) {
                mAdapter.addAll(movies);
            }
        }


        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

            mAdapter.clear();
        }


    };

}

