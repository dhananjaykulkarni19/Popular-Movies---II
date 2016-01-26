package udacity_portfolio.pupularmovies_II.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.adapters.MovieAdapter;
import udacity_portfolio.pupularmovies_II.model.Movie;

/**
 * Created by admin on 1/26/2016.
 */
public class MovieGridFragment extends Fragment {

    View rootView;

    @Bind(R.id.moviegridview)
    RecyclerView movieGridView;

    @Bind(R.id.pbloading)
    ProgressBar pbLoading;

    private RecyclerView.LayoutManager mLayoutManager;

    public MovieGridFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        ButterKnife.bind(this, rootView);

        pbLoading.setVisibility(View.VISIBLE);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        movieGridView.setLayoutManager(mLayoutManager);

        //Toast.makeText(getActivity(), "MovieGridFragment", Toast.LENGTH_SHORT).show();

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

    public void onEventMainThread(List<Movie> list){

        Log.i("List", "Movie list received in fragment");

        pbLoading.setVisibility(View.GONE);
        movieGridView.setVisibility(View.VISIBLE);
        MovieAdapter movieAdapter = new MovieAdapter(getActivity(), list);
        movieGridView.setAdapter(movieAdapter);
    }
}
