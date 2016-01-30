package udacity_portfolio.pupularmovies_II.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by admin on 1/30/2016.
 */
public class FavouriteMovie extends Model{

    @Column(name="favMovieId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String favMovieId;

    public FavouriteMovie(){

        super();
    }

    public static List<FavouriteMovie> getAllFavouriteMovieIds(){

        return new Select().from(FavouriteMovie.class).execute();
    }


}


