package udacity_portfolio.pupularmovies_II.ui;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.adapters.MovieReviewsAdapter;
import udacity_portfolio.pupularmovies_II.model.Movie;
import udacity_portfolio.pupularmovies_II.model.MovieReviews;

public class ReviewsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Bind(R.id.lvreviews)
    ListView lvReviews;

    String movieId;

    MovieReviewsAdapter mAdapter;

    ActionBar mActionBar;

    List<MovieReviews> movieReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        initActionbar();

        lvReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showInformationDialog(movieReviews.get(position).content);
            }
        });

    }

    private void showInformationDialog(String content){

        DialogFragment reviewsDialog = ReviewsDialog.newInstance(content);
        reviewsDialog.show(getSupportFragmentManager(), getResources().getString(R.string.label_reviews));

    }

    private void initActionbar(){

        mActionBar = getSupportActionBar();
        if(mActionBar != null){

            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(R.layout.custom_actionbar);
            ImageView ivBack = (ImageView) findViewById(R.id.ivback);
            TextView tvTitle = (TextView) findViewById(R.id.tvtitle);

            tvTitle.setText(R.string.label_reviews);

            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
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

    public void onEventMainThread(Movie movie) {

        movieId = String.valueOf(movie.id);

        /*adapter = new MoveReviewsAdapter2(ReviewsActivity.this, MovieReviews.getAllMovieReviews(movieId));
        lvReviews.setAdapter(adapter);*/

        movieReviews = MovieReviews.getAllMovieReviews(movieId);

        mAdapter = new MovieReviewsAdapter(ReviewsActivity.this, movieReviews);
        lvReviews.setAdapter(mAdapter);
    }
}

