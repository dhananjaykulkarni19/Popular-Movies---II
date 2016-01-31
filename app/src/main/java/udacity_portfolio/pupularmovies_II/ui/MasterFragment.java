package udacity_portfolio.pupularmovies_II.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import udacity_portfolio.pupularmovies_II.R;

/**
 * Created by admin on 1/26/2016.
 */
public class MasterFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    View rootView;

    public MasterFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_master_layout, container, false);
            ButterKnife.bind(this, rootView);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return rootView;


        /*rootView = inflater.inflate(R.layout.fragment_master_layout, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;*/
    }
}
