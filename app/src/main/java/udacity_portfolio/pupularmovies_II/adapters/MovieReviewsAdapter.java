package udacity_portfolio.pupularmovies_II.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import udacity_portfolio.pupularmovies_II.R;
import udacity_portfolio.pupularmovies_II.model.MovieReviews;

/**
 * Created by admin on 1/9/2016.
 */
public class MovieReviewsAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private List<MovieReviews> movieReviewList;

    public MovieReviewsAdapter(Context mContext, List<MovieReviews> movieReviewList) {
        this.mContext = mContext;
        this.movieReviewList = movieReviewList;
    }

    @Override
    public int getCount() {
        return movieReviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

            holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tvauthorname);
            holder.tvReviewText = (TextView) convertView.findViewById(R.id.tvreviewtext);

            convertView.setTag(holder);
        }else{

            holder = (ViewHolder) convertView.getTag();
        }

        MovieReviews movieReviews = movieReviewList.get(position);

        holder.tvReviewText.setText(movieReviews.content);
        holder.tvAuthorName.setText(movieReviews.author);

        return convertView;
    }

    protected static class ViewHolder{

        TextView tvReviewText, tvAuthorName;

    }
}
