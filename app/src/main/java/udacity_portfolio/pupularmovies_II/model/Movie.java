package udacity_portfolio.pupularmovies_II.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 12/1/2015.
 */
public class Movie extends Model{

    @Column(name="title")
    public String title;

    @Column(name="thumbnail")
    public String thumbNail;

    @Column(name="overview")
    public String overView;

    @Column(name="voteAverage")
    public String voteAverage;

    @Column(name="releaseDate")
    public String releaseDate;

    @Column(name="poster")
    public String poster;

    @Column(name="trailers")
    public String trailers;

    @Column(name="movieid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int id;

    @Column(name="isFavourite")
    public boolean isFavourite;

    @Column(name="isHighestRated")
    public boolean isHighestRated;

    @Column(name="isPopular")
    public boolean isPopular;


    public Movie() {
        super();
    }

    public static List<Movie> getAllMovies(){
        return new Select().from(Movie.class).execute();
    }

    public static List<Movie> getAllFavouriteMovies(){
        return new Select().from(Movie.class).where("isFavourite = ?", true).execute();
    }

    public static List<Movie> getAllHighestRatedMovies(){
        return new Select().from(Movie.class).where("isHighestRated = ?", true).execute();
    }

    public static List<Movie> getAllPopularMovies(){
        return new Select().from(Movie.class).where("isPopular = ?", true).execute();
    }

    public static Movie getMovieById(int id){
        return new Select().from(Movie.class).where("movieid = ?" , id).executeSingle();

    }
}
