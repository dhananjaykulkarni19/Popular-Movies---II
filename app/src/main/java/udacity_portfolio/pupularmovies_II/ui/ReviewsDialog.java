package udacity_portfolio.pupularmovies_II.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import udacity_portfolio.pupularmovies_II.R;

/**
 * Created by admin on 12/13/2015.
 */
public class ReviewsDialog extends DialogFragment {

    public static ReviewsDialog newInstance(String content){

        ReviewsDialog dialog = new ReviewsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("content");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_reviews));
        builder.setMessage(title).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
