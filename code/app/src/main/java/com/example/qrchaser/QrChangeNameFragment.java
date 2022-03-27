package com.example.qrchaser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class QrChangeNameFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(String newName, int returnCode);
    } // end OnFragmentInteractionListener

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    } // end onAttach

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_qr_change_name, null);
        EditText newNameET = view.findViewById(R.id.qr_change_name_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Enter new name")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    //takes user input roll
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = newNameET.getText().toString();
                        if(!newName.equals("")) {
                            listener.onOkPressed(newName, 1);
                        }
                    }
                }).create();
    } // end onCreateDialog
} // end QrChangeNameFragment Class

