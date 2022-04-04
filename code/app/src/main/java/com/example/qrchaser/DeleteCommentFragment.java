package com.example.qrchaser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteCommentFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;
    private int index;

    public DeleteCommentFragment(int index) {
        this.index = index;
    } // end DeleteCommentFragment Constructor

    public interface OnFragmentInteractionListener {
        void onDeletePressed(int index);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle("Delete Comment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDeletePressed(index);
                    } // end onClick
                }).create();
    } // end onCreateDialog

} // end AddCommentFragment Class
