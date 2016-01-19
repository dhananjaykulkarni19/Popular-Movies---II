package udacity_portfolio.pupularmovies_II.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

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

        mAdapter = new MovieReviewsAdapter(ReviewsActivity.this, MovieReviews.getAllMovieReviews(movieId));
        lvReviews.setAdapter(mAdapter);
    }
}

