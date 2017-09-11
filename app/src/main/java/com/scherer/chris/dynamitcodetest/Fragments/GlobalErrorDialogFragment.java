package com.scherer.chris.dynamitcodetest.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by christopherscherer on 9/8/17.
 *
 * Error dialog used to display messages to the user
 */

public class GlobalErrorDialogFragment extends DialogFragment {
    private String errorMessage;

    public GlobalErrorDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(errorMessage)
                .setPositiveButton("Ok", (dialog, id) -> {
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setErrorMessage(String s){
        errorMessage = s;
    }
}
