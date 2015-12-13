package udacity_portfolio.pupularmovies_i.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;
import udacity_portfolio.pupularmovies_i.R;
import udacity_portfolio.pupularmovies_i.model.Movie;
import udacity_portfolio.pupularmovies_i.ui.MovieDetailsActivity;

/**
 * Created by admin on 12/2/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>  {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private List<Movie> movieList;
    //private OnItemClickListener onItemClickListener;

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        //v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Movie movie = movieList.get(position);
        Picasso.with(mContext).load(movie.getPoster()).into(holder.imgMoviePoster);

        holder.imgMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "Adapter Clicked Movie : " + movie.getTitle());
                Intent intent = new Intent();
                //intent.setClass(mContext, MovieDetailsActivity.class);
                intent.setClass(mContext, MovieDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i(TAG, "Clicked Movie : " + movie.getTitle());
                EventBus.getDefault().postSticky(movie);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgMoviePoster;

        public ViewHolder(View itemView) {
                super(itemView);

            imgMoviePoster = (ImageView) itemView.findViewById(R.id.imgmovieposter);

        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, Movie movie);
    }
}
