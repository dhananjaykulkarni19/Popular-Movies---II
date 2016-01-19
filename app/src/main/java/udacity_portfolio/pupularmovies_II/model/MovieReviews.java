package udacity_portfolio.pupularmovies_II.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by admin on 1/9/2016.
 */
public class MovieReviews extends Model {

    @Column(name="reviewId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String reviewId;

    @Column(name="author")
    public String author;

    @Column(name="content")
    public String content;

    @Column(name="url")
    public String url;

    @Column(name="MovieId")
    public int movieid;

    public MovieReviews(){
        super();
    }

    public static List<MovieReviews> getAllMovieReviews(String movieid){
        return new Select().from(MovieReviews.class).where("MovieId = ?", movieid).execute();
    }
}
