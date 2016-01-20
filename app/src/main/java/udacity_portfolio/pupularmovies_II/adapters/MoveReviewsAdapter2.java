package udacity_portfolio.pupularmovies_II.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.model.MovieReviews;

/**
 * Created by android-3 on 20/1/16.
 */
public class MoveReviewsAdapter2 extends RecyclerView.Adapter<MoveReviewsAdapter2.ViewHolder>{

    private Context mContext;
    private List<MovieReviews> movieReviewList;

    public MoveReviewsAdapter2(Context mContext, List<MovieReviews> movieReviewList) {
        this.mContext = mContext;
        this.movieReviewList = movieReviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MovieReviews movieReview = movieReviewList.get(position);

        holder.tvReviewText.setText(movieReview.content);
        holder.tvAuthorName.setText(movieReview.author);
    }

    @Override
    public int getItemCount() {
        return movieReviewList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public  TextView tvReviewText, tvAuthorName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvReviewText = (TextView) itemView.findViewById(R.id.tvreviewtext);
            tvAuthorName = (TextView) itemView.findViewById(R.id.tvauthorname);
        }
    }
}
