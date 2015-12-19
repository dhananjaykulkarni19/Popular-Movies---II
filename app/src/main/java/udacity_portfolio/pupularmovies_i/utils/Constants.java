package udacity_portfolio.pupularmovies_i.utils;

/**
 * Created by admin on 12/1/2015.
 */
public class Constants {

    public static String API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    public static String URL = "http://api.themoviedb.org/3/discover/movie?language=en&api_key="+API_KEY;
    public static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";
    public static String POPULARITY_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY;
    public static String HIGHEST_RATED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&api_key="+API_KEY;
}
