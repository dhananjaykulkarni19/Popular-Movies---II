package udacity_portfolio.pupularmovies_II.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.adapters.MovieAdapter;
import udacity_portfolio.pupularmovies_II.app.ApplicationController;
import udacity_portfolio.pupularmovies_II.model.Movie;
import udacity_portfolio.pupularmovies_II.model.MovieReviews;
import udacity_portfolio.pupularmovies_II.utils.Constants;

/**
 * Created by admin on 1/26/2016.
 */
public class MovieDetailFragment extends Fragment {

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

    MovieReviews movieReviews;
    private boolean isFavourite = false;

    private String movieId;

    private ArrayList<String> mTrailersList;
    private ArrayList<MovieReviews> mReviewsList;

    String mTrailersUrl, mReviewsUrl;

    Movie movie;

    View rootView;

    public MovieDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        Toast.makeText(getActivity(), "MovieDetailFragment", Toast.LENGTH_SHORT).show();

        return rootView;
    }

    @OnClick(R.id.fab)
    public void onFavouriteClick(){

        if(isFavourite){

            movie.isFavourite = false;
            Toast.makeText(getActivity(), getString(R.string.label_un_favourite), Toast.LENGTH_SHORT).show();
            isFavourite = false;
            btnFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            movie.save();

        }else {
            movie.isFavourite = true;
            Toast.makeText(getActivity(), getString(R.string.label_favourite), Toast.LENGTH_SHORT).show();
            isFavourite = true;
            btnFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.favourite_color)));
            movie.save();
        }
    }

    @OnClick(R.id.btntrailer1)
    public void playTrailer1(){

        if(mTrailersList.size() > 0){

            startYoutube(0);

        }
    }

    @OnClick(R.id.btntrailer2)
    public void playTrailer2(){

        if(mTrailersList.size() > 1){

            startYoutube(1);

        }
    }

    @OnClick(R.id.btntrailer3)
    public void playTrailer3(){

        if(mTrailersList.size() > 2){

            startYoutube(2);

        }
    }

    @OnClick(R.id.btnreviews)
    public void reviews(){

        if(MovieReviews.getAllMovieReviews(movieId).size() == 0){
            Toast.makeText(getActivity(), R.string.no_reviews, Toast.LENGTH_SHORT).show();
        }else{

            trailerMasterLayout.setVisibility(View.VISIBLE);

            Intent intent = new Intent();
            intent.setClass(getActivity(), ReviewsActivity.class);
            EventBus.getDefault().postSticky(movie);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().registerSticky(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Movie movie){

        Log.i("Movie", "Received Movie Details in fragment");

        this.movie = movie;

        Picasso.with(getActivity())
                .load(movie.poster)
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_image_error)
                .into(ivMovieThumbnail);

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

    private void startYoutube(int position){

        try{
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), Constants.YOUTUBE_API_KEY, mTrailersList.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){

            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.no_youtube), Toast.LENGTH_SHORT).show();

        }

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

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
