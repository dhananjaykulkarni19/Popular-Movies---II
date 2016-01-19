package udacity_portfolio.pupularmovies_II.app;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by admin on 12/1/2015.
 */
public class ApplicationController extends Application {

    public static final String TAG = "VolleyTag";

    private static ApplicationController sInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        ActiveAndroid.initialize(this);
    }

    public static synchronized ApplicationController getInstance(){

        return sInstance;
    }

    public RequestQueue getRequestQueue(){

        if (mRequestQueue == null) {

            sInstance.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return sInstance.mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> request){

        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag){

        if(mRequestQueue!=null){
            mRequestQueue.cancelAll(tag);
        }
    }


}
