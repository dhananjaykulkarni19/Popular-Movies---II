package udacity_portfolio.pupularmovies_II.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.adapters.MovieReviewsAdapter;
import udacity_portfolio.pupularmovies_II.model.Movie;
import udacity_portfolio.pupularmovies_II.model.MovieReviews;

/**
 * Created by admin on 1/31/2016.
 */
public class ReviewsFragment extends Fragment{

    View rootView;

    @Bind(R.id.lvreviews)
    ListView lvReviews;

    List<MovieReviews> movieReviews;
    String movieId;

    public ReviewsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, rootView);

        lvReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showInformationDialog(movieReviews.get(position).content);
            }
        });


        return rootView;
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

        movieId = String.valueOf(movie.id);

        /*adapter = new MoveReviewsAdapter2(ReviewsActivity.this, MovieReviews.getAllMovieReviews(movieId));
        lvReviews.setAdapter(adapter);*/

        movieReviews = MovieReviews.getAllMovieReviews(movieId);

        MovieReviewsAdapter mAdapter = new MovieReviewsAdapter(getActivity(), movieReviews);
        lvReviews.setAdapter(mAdapter);

    }

    private void showInformationDialog(String content){

        DialogFragment reviewsDialog = ReviewsDialog.newInstance(content);
        reviewsDialog.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.label_reviews));

    }

}
