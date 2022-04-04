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

/**
 * This fragment prompt the user to type in the new comment
 */
public class AddCommentFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(String comment, int returnCode);
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_comment, null);
        EditText addComment = view.findViewById(R.id.add_comment_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Enter new comments")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // takes user input roll
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String comment = addComment.getText().toString();
                        if(!comment.isEmpty()) {
                            listener.onOkPressed(comment, 2);
                        }
                    } // end onClick
                }).create();
    } // end onCreateDialog

} // end AddCommentFragment Class
