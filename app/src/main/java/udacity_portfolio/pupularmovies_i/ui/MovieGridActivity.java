package udacity_portfolio.pupularmovies_i.ui;

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
import udacity_portfolio.pupularmovies_i.R;
import udacity_portfolio.pupularmovies_i.adapters.MovieAdapter;
import udacity_portfolio.pupularmovies_i.app.ApplicationController;
import udacity_portfolio.pupularmovies_i.model.Movie;
import udacity_portfolio.pupularmovies_i.utils.Constants;

public class MovieGridActivity extends AppCompatActivity implements SortDialog.SortDialogListener{

    private final String TAG = this.getClass().getSimpleName();

    private List<Movie> mMovieList;

    @Bind(R.id.moviegridview)
    RecyclerView movieGridView;

    @Bind(R.id.pbloading)
    ProgressBar progressBar;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        ButterKnife.bind(this);


        movieGridView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        movieGridView.setLayoutManager(mLayoutManager);

        if(isNetworkAvailable()){
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

    private void makeMovieRequest(String url){

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Log.i(TAG, "Response : " + response);
                if(response != null){
                    try {
                        parseResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showInformationDialog(error.getMessage());
                //Log.i(TAG, "Error : " + error.getMessage());
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
        sortFragment.show(getSupportFragmentManager(), "Sort");

    }

    private void showInformationDialog(String title){

        DialogFragment infoDialog = InformationDialog.newInstance(title);
        infoDialog.show(getSupportFragmentManager(), "Network");

    }

    private void parseResponse(JSONObject response) throws JSONException {

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

            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setThumbNail(Constants.IMAGE_BASE_URL +thumbNail);
            movie.setOverView(overView);
            movie.setVoteAverage(voteAverage);
            movie.setReleaseDate(releaseDate);
            movie.setPoster(Constants.IMAGE_BASE_URL + poster);

            mMovieList.add(movie);
        }

        //Log.i(TAG, "Total Movies : " + mMovieList.size());

        progressBar.setVisibility(View.GONE);
        movieGridView.setVisibility(View.VISIBLE);
        MovieAdapter adapter = new MovieAdapter(getApplicationContext(), mMovieList);
        movieGridView.setAdapter(adapter);
    }

    @Override
    public void which(int which, boolean isChecked, DialogInterface dialog) {
        Log.i(TAG, "Which in Activity : " + which + " isChecked : " + isChecked);

        if(which == 0 && isChecked){

            movieGridView.setVisibility(View.GONE);
            makeMovieRequest(Constants.HIGHEST_RATED_URL);
            dialog.dismiss();

        }else if(which == 1 && isChecked){

            makeMovieRequest(Constants.POPULARITY_URL);
            dialog.dismiss();
            movieGridView.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
