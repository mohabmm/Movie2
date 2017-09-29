package com.nocom.movie2;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

import data.MovieContract;
import data.MovieCursorAdapter;

/**
 * Created by Moha on 9/13/2017.
 */
public class Movie_detail_Activity extends AppCompatActivity {
    static String key = "";
    static String rev = "";
    ImageView iamgt;
    TextView overvview;
    TextView title;
    ImageView iamge2;
    String gettingImageUrl;
    String TRAILER_WEBSITE;
    String REVIEW_WEBSITE;
    TextView releasedate;
    TextView rating;
    TextView reviews;
    Movie currentMovie;
    Button TrailerButton;
    //TextView review ;
    Button button;
    MovieCursorAdapter movieCursorAdapter;

    public Movie_detail_Activity(String nkey, String ncontent) {

        key = nkey;
        rev = ncontent;

    }

    public Movie_detail_Activity() {

    }


    public Movie_detail_Activity(String ncontent) {
        rev = ncontent;
    }


    public static String getKey() {
        return key;
    }

    public static String getReview() {
        return rev;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //  Movie_detail_Activity currentMovieActivity;


        button = (Button) findViewById(R.id.button2);


        setTitle(R.string.MovieDetail);
        TrailerButton = (Button) findViewById(R.id.trailer);
        iamgt = (ImageView) findViewById(R.id.image);//image
        overvview = (TextView) findViewById(R.id.overview);
        reviews = ((TextView) findViewById(R.id.review));
        //review=(TextView)findViewById(R.id.review);
        title = (TextView) findViewById(R.id.title);
        releasedate = (TextView) findViewById(R.id.releasedate);
        rating = (TextView) findViewById(R.id.rate);
        Bundle b = this.getIntent().getExtras();
        Intent intentThatStartedThisActivity = getIntent();
        if (b != null && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT) && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_SHORTCUT_NAME) && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_REFERRER) && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME)) {
            String textEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            String textentered2 = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
            String textentered3 = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_REFERRER);
            String textentered4 = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME);
            currentMovie = b.getParcelable("Movie");
            overvview.setText(textEntered);
            title.setText(textentered2);
            rating.setText(textentered4);
            Intent getImage = getIntent();
            releasedate.setText(textentered3);
            gettingImageUrl = String.valueOf(getImage.getStringExtra("image"));
            Picasso.with(this).load(gettingImageUrl)
                    .placeholder(R.drawable.progress_animation)
                    .into(iamgt);

            REVIEW_WEBSITE = "https://api.themoviedb.org/3/movie/" + currentMovie.getId() + "/reviews?api_key=###&language=en-US"; // please enter your key

            TRAILER_WEBSITE = "https://api.themoviedb.org/3/movie/" + currentMovie.getId() + "/videos?api_key=###&language=en-US"; // please enter your api key


        }
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        QuakeRevs tasks = new QuakeRevs();
        tasks.execute(REVIEW_WEBSITE);
        Log.i("review first one ", rev);
        TrailerAsyncTask task = new TrailerAsyncTask();
        task.execute(TRAILER_WEBSITE);
        key = Movie_detail_Activity.getKey();
        Log.i("THE KEY IS ", key);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the MocieActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    public void clicked(View view) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + key));
        Log.i("message", "https://www.youtube.com/watch?v=" + key);


        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }

    }

    public void onClickAddTask(View view) {
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(MovieContract.MovieEntry.COLUMN_ID, currentMovie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, currentMovie.getOverview());
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        // COMPLETED (8) Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if (uri != null) {
            Toast.makeText(getBaseContext(), "movie is added ", Toast.LENGTH_LONG).show();
        }
    }

    private class TrailerAsyncTask extends AsyncTask<String, Void, List<Movie_detail_Activity>> {

        private ProgressDialog progressDialog;

        @Override
        protected List<Movie_detail_Activity> doInBackground(String... strings) {
            List<Movie_detail_Activity> result = null;
            if (Looper.myLooper() == null)
                Looper.prepare();

            try {
                result = QueryU.fetchTrailerData(TRAILER_WEBSITE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Movie_detail_Activity> movie_detail_activities) {
            super.onPostExecute(movie_detail_activities);

        }
    }

    private class QuakeRevs extends AsyncTask<String, Void, List<Movie_detail_Activity>> {

        private ProgressDialog progressDialog;

        @Override
        protected List<Movie_detail_Activity> doInBackground(String... strings) {
            List<Movie_detail_Activity> result = null;
            if (Looper.myLooper() == null)
                Looper.prepare();

            try {
                result = QueryReview.fetchReviewData(REVIEW_WEBSITE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Movie_detail_Activity> movie_detail_activities) {
            super.onPostExecute(movie_detail_activities);
            reviews.setText(rev);
            Log.e("review text ",rev);
        }
    }
}
