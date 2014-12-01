package com.example.tyson.transguard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.tyson.transguard.TransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.tyson.transguard.TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TransactionFragment extends DialogFragment {

    String name;
    String date;
    String amount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Please Confirm Transaction \n\n" +
                "Name: " + TransGuardMainMenu.rName + "\n" +
                "Date: " + TransGuardMainMenu.rDate + "\n" +
                "Transaction Total:" + TransGuardMainMenu.rAmount + "\n")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                TransGuardMainMenu.isTrans = true;
                TransGuardMainMenu.checkinButton.setVisibility(View.VISIBLE);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                return;
                }
            });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
