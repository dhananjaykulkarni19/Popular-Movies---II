package udacity_portfolio.pupularmovies_i.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.ArrayList;

import udacity_portfolio.pupularmovies_i.R;

/**
 * Created by admin on 12/10/2015.
 */
public class SortDialog extends DialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_sort))
                .setMultiChoiceItems(R.array.sort_menu, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        mSortDialogListener.which(which, isChecked, dialog);
                    }
                });
        return builder.create();
    }

    public interface SortDialogListener{

        void which(int which, boolean isChecked, DialogInterface dialog);
    }

    SortDialogListener mSortDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mSortDialogListener = (SortDialogListener) activity;
    }
}
