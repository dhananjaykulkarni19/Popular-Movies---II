package udacity_portfolio.pupularmovies_II.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 12/1/2015.
 */
public class Utils {

    public static String YOUTUBE_API_KEY = "AIzaSyC83QrydzAKFdN9MS4yKaVq7il_bTIXu_w";
    public static String MOVIE_API_KEY = "198a20c03dc9c1de0937a8ccab1b7603";
    public static String URL = "http://api.themoviedb.org/3/discover/movie?language=en&api_key=" + MOVIE_API_KEY;
    public static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";
    public static String POPULARITY_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + MOVIE_API_KEY;
    public static String HIGHEST_RATED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&api_key=" + MOVIE_API_KEY;
    public static String TRAILERS_URL_PART_1 = "http://api.themoviedb.org/3/movie/";
    public static String TRAILERS_URL_PART_2 = "/videos?api_key=" + MOVIE_API_KEY;
    public static String REVIEWS_URL_PART_1 = "http://api.themoviedb.org/3/movie/";
    public static String REVIEWS_URL_PART_2 = "/reviews?api_key=" + MOVIE_API_KEY;
    public static String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    public static String APP_PREFERENCES = "APP_PREFERENCES";

    public static SharedPreferences getAppPreferences(Context context){

        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_MULTI_PROCESS);
    }

}
