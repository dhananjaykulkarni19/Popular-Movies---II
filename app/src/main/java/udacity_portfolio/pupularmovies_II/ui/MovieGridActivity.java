package udacity_portfolio.pupularmovies_II.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.adapters.MovieAdapter;
import udacity_portfolio.pupularmovies_II.app.ApplicationController;
import udacity_portfolio.pupularmovies_II.model.Movie;
import udacity_portfolio.pupularmovies_II.utils.Constants;

public class MovieGridActivity extends AppCompatActivity implements SortDialog.SortDialogListener{

    private final String TAG = this.getClass().getSimpleName();

    private List<Movie> mMovieList;

    @Bind(R.id.moviegridview)
    RecyclerView movieGridView;

    @Bind(R.id.pbloading)
    ProgressBar progressBar;

    private RecyclerView.LayoutManager mLayoutManager;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        ButterKnife.bind(this);
        String screenType = getResources().getString(R.string.screen_type);

        if(screenType.equalsIgnoreCase("Large")){

            Log.i(TAG, "Tablet Layout Loaded");
            FrameLayout layout = (FrameLayout) findViewById(R.id.fragment_container);
            movieGridView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);

            MasterFragment fragment = new MasterFragment();
            if(layout != null){

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
            }else{
                Log.i(TAG, "Container Null");
            }

            /*mLayoutManager = new GridLayoutManager(this, 3);
            movieGridView.setLayoutManager(mLayoutManager);*/
        }else {

            mLayoutManager = new GridLayoutManager(this, 2);
            movieGridView.setLayoutManager(mLayoutManager);
        }

        if(isNetworkAvailable()){
            Log.i(TAG, "Making request from activity");
            makeMovieRequest(Constants.URL);
        }else{
            showInformationDialog(getString(R.string.network_message));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            mLayoutManager = new GridLayoutManager(this, 3);
            movieGridView.setLayoutManager(mLayoutManager);

        }else{

            mLayoutManager = new GridLayoutManager(this, 2);
            movieGridView.setLayoutManager(mLayoutManager);
        }
    }

    private void makeMovieRequest(final String url) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    parseResponse(url, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showInformationDialog(error.getMessage());
            }
        });

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.sort:
                showSortDialog();
            break;

        }
        return true;
    }

    private void showSortDialog(){

        DialogFragment sortFragment = new SortDialog();
        sortFragment.show(getSupportFragmentManager(), getResources().getString(R.string.label_sort));

    }

    private void showInformationDialog(String title){

        DialogFragment infoDialog = InformationDialog.newInstance(title);
        infoDialog.show(getSupportFragmentManager(), getResources().getString(R.string.label_network));

    }

    private void parseResponse(String url, JSONObject response) throws JSONException {

        Log.i(TAG, "Parsing JSON from activity");

        movieGridView.setVisibility(View.VISIBLE);
        mMovieList = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");

        for(int resultCount = 0; resultCount < results.length(); resultCount++){

            String title = results.getJSONObject(resultCount).getString("original_title");
            String thumbNail = results.getJSONObject(resultCount).getString("backdrop_path");
            String overView = results.getJSONObject(resultCount).getString("overview");
            String voteAverage = results.getJSONObject(resultCount).getString("vote_average");
            String releaseDate = results.getJSONObject(resultCount).getString("release_date");
            String poster = results.getJSONObject(resultCount).getString("poster_path");
            int id = results.getJSONObject(resultCount).getInt("id");

            movie = new Movie();
            movie.title = title;
            movie.thumbNail  = Constants.IMAGE_BASE_URL +thumbNail;
            movie.overView = overView;
            movie.voteAverage = voteAverage;
            movie.releaseDate = releaseDate;
            movie.poster = Constants.IMAGE_BASE_URL + poster;
            movie.id = id;

            if(url.equalsIgnoreCase(Constants.HIGHEST_RATED_URL)){
                movie.isHighestRated = true;
            }else if(url.equalsIgnoreCase(Constants.POPULARITY_URL)){
                movie.isPopular = true;
            }

            movie.save();
        }

        progressBar.setVisibility(View.GONE);
        movieGridView.setVisibility(View.VISIBLE);

        EventBus.getDefault().postSticky(Movie.getAllMovies());

        if(url.equalsIgnoreCase(Constants.URL)){
            mMovieList = Movie.getAllMovies();
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), mMovieList);
            movieGridView.setAdapter(adapter);

            if(getSupportActionBar() != null){

                getSupportActionBar().setTitle(R.string.app_name);
            }

        }else if(url.equalsIgnoreCase(Constants.HIGHEST_RATED_URL)){
            mMovieList = Movie.getAllHighestRatedMovies();
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), mMovieList);
            movieGridView.setAdapter(adapter);

            if(getSupportActionBar() != null){

                getSupportActionBar().setTitle(R.string.highest_rated);
            }

        }else if(url.equalsIgnoreCase(Constants.POPULARITY_URL)){
            mMovieList = Movie.getAllPopularMovies();
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), mMovieList);
            movieGridView.setAdapter(adapter);

            if(getSupportActionBar() != null){

                getSupportActionBar().setTitle(R.string.most_popular);
            }
        }
    }

    @Override
    public void which(int which, boolean isChecked, DialogInterface dialog) {

        if(which == 0 && isChecked){

            movieGridView.setVisibility(View.GONE);
            makeMovieRequest(Constants.HIGHEST_RATED_URL);
            dialog.dismiss();

        }else if(which == 1 && isChecked){

            makeMovieRequest(Constants.POPULARITY_URL);
            dialog.dismiss();
            movieGridView.setVisibility(View.GONE);

        }else if(which ==2 && isChecked){

            dialog.dismiss();

            if(getFavouriteMovies().size() > 0){
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), getFavouriteMovies());
                movieGridView.setAdapter(adapter);
            }else{
                Toast.makeText(MovieGridActivity.this, R.string.no_favourites, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private List<Movie> getFavouriteMovies(){

       return Movie.getAllFavouriteMovies();
    }
}
