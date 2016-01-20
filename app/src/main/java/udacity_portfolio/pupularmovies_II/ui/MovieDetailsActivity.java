package udacity_portfolio.pupularmovies_II.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.adapters.MovieReviewsAdapter;
import udacity_portfolio.pupularmovies_II.app.ApplicationController;
import udacity_portfolio.pupularmovies_II.model.Movie;
import udacity_portfolio.pupularmovies_II.model.MovieReviews;
import udacity_portfolio.pupularmovies_II.utils.Constants;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.moviethumbnail)
    ImageView ivMovieThumbnail;

    @Bind(R.id.tvoverview)
    TextView tvOverview;

    @Bind(R.id.tvreleasedate)
    TextView tvReleaseDate;

    @Bind(R.id.tvvoteaverage)
    TextView tvVoterAverage;

    @Bind(R.id.fab)
    FloatingActionButton btnFavourite;

    @Bind(R.id.btntrailer1)
    Button btnTrailer1;

    @Bind(R.id.btntrailer2)
    Button btnTrailer2;

    @Bind(R.id.btntrailer3)
    Button btnTrailer3;

    @Bind(R.id.trailermasterlayout)
    LinearLayout trailerMasterLayout;

    @Bind(R.id.trailerbuttonlayout)
    LinearLayout trailerButtonLayout;

    @Bind(R.id.btnreviews)
    Button btnReviews;

    @Bind(R.id.tvgettingvideos)
    TextView tvVideoInfo;

    Movie movie;
    MovieReviews movieReviews;
    private boolean isFavourite = false;

    private String movieId;

    private ArrayList<String> mTrailersList;
    private ArrayList<MovieReviews> mReviewsList;

    String mTrailersUrl, mReviewsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @OnClick(R.id.fab)
    public void onFavouriteClick(){

        if(isFavourite){

            movie.isFavourite = false;
            Toast.makeText(this, getString(R.string.label_un_favourite), Toast.LENGTH_SHORT).show();
            isFavourite = false;
            btnFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            movie.save();

        }else {
            movie.isFavourite = true;
            Toast.makeText(this, getString(R.string.label_favourite), Toast.LENGTH_SHORT).show();
            isFavourite = true;
            btnFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.favourite_color)));
            movie.save();
        }
    }

    @OnClick(R.id.btntrailer1)
    public void playTrailer1(){

        if(mTrailersList.size() > 0){

            Intent intent = YouTubeStandalonePlayer.createVideoIntent(MovieDetailsActivity.this, Constants.YOUTUBE_API_KEY, mTrailersList.get(0));
            startActivity(intent);

        }
    }

    @OnClick(R.id.btntrailer2)
    public void playTrailer2(){

        if(mTrailersList.size() > 1){

            Intent intent = YouTubeStandalonePlayer.createVideoIntent(MovieDetailsActivity.this, Constants.YOUTUBE_API_KEY, mTrailersList.get(1));
            startActivity(intent);

        }
    }

    @OnClick(R.id.btntrailer3)
    public void playTrailer3(){

        if(mTrailersList.size() > 2){

            Intent intent = YouTubeStandalonePlayer.createVideoIntent(MovieDetailsActivity.this, Constants.YOUTUBE_API_KEY, mTrailersList.get(2));
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnreviews)
    public void reviews(){

        if(MovieReviews.getAllMovieReviews(movieId).size() == 0){
            Toast.makeText(this, R.string.no_reviews, Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.setClass(MovieDetailsActivity.this, ReviewsActivity.class);
            EventBus.getDefault().postSticky(movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().registerSticky(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_share, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_share:

                if(mTrailersList != null && mTrailersList.size() > 0 ){

                    String video = "https://www.youtube.com/watch?v=" + mTrailersList.get(0);

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, video);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                }

            break;
        }
        return true;
    }

    public void onEventMainThread(Movie movie){

        this.movie = movie;

        Picasso.with(this)
                .load(movie.poster)
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_image_error)
                .into(ivMovieThumbnail);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(movie.title);
        }

        tvOverview.setText(getString(R.string.overview) + " " + movie.overView);
        tvReleaseDate.setText(getString(R.string.release_date) + " " + movie.releaseDate);
        tvVoterAverage.setText(getString(R.string.vote_average) + " " + movie.voteAverage);

        checkFavourite();

        movieId = String.valueOf(movie.id);

        trailerMasterLayout.setVisibility(View.VISIBLE);
        tvVideoInfo.setVisibility(View.VISIBLE);

        mTrailersUrl = Constants.TRAILERS_URL_PART_1 + movieId + Constants.TRAILERS_URL_PART_2;
        mReviewsUrl = Constants.REVIEWS_URL_PART_1 + movieId + Constants.REVIEWS_URL_PART_2;

        getMovieData(mTrailersUrl);
        getMovieData(mReviewsUrl);
    }

    private void checkFavourite(){

        if(movie.isFavourite){
            btnFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.favourite_color)));
        }else{
            btnFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
    }

    private void getMovieData(final String url){

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    parseJSON(url, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MovieDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Exception ..!!!");

            }
        });

        ApplicationController.getInstance().addToRequestQueue(request);
    }

    private void parseJSON(String url, JSONObject response) throws JSONException {

        if(url.equalsIgnoreCase(mTrailersUrl)){

            mTrailersList  = new ArrayList<>();

            JSONArray trailersArray = response.getJSONArray("results");

            for(int resultCount =0; resultCount < trailersArray.length(); resultCount++){

                mTrailersList.add(trailersArray.getJSONObject(resultCount).getString("key"));
            }

            movie.trailers = TextUtils.join(",", mTrailersList);
            movie.save();

            if(mTrailersList.size() < 2){

                btnTrailer2.setVisibility(View.GONE);
                btnTrailer3.setVisibility(View.GONE);
                tvVideoInfo.setVisibility(View.GONE);
                trailerMasterLayout.setVisibility(View.VISIBLE);
                trailerButtonLayout.setVisibility(View.VISIBLE);

            }else if(mTrailersList.size() < 3){
                btnTrailer3.setVisibility(View.GONE);
                tvVideoInfo.setVisibility(View.GONE);
                trailerMasterLayout.setVisibility(View.VISIBLE);
                trailerButtonLayout.setVisibility(View.VISIBLE);
            }else{

                tvVideoInfo.setVisibility(View.GONE);
                trailerMasterLayout.setVisibility(View.VISIBLE);
                trailerButtonLayout.setVisibility(View.VISIBLE);
            }

        }else if (url.equalsIgnoreCase(mReviewsUrl)){

            mReviewsList = new ArrayList<>();

            JSONArray reviewsArray = response.getJSONArray("results");

            for(int reviewsCount = 0; reviewsCount < reviewsArray.length(); reviewsCount++){

                movieReviews = new MovieReviews();

                movieReviews.reviewId = reviewsArray.getJSONObject(reviewsCount).getString("id");
                movieReviews.author = reviewsArray.getJSONObject(reviewsCount).getString("author");
                movieReviews.content = reviewsArray.getJSONObject(reviewsCount).getString("content");
                movieReviews.url = reviewsArray.getJSONObject(reviewsCount).getString("url");
                movieReviews.movieid = movie.id;

                movieReviews.save();

            }
        }
    }
}
