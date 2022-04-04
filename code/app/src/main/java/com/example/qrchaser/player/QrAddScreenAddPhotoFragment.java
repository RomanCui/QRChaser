package com.example.qrchaser.player;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrchaser.R;
import com.example.qrchaser.player.myQRCodes.QrAddScreenActivity;

/**
 * A fragment that prompt the user to select if they want to add photo via camera or gallery
 */
public class QrAddScreenAddPhotoFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_qr_add_photo, null);

        Button camera = view.findViewById(R.id.qr_add_photo_camera_button);
        Button gallery = view.findViewById(R.id.qr_add_photo_gallery_button);

        camera.setOnClickListener((v) -> {
            ((QrAddScreenActivity)getActivity()).cameraAddPhoto();
            dismiss();
        });

        gallery.setOnClickListener((v) -> {
            ((QrAddScreenActivity)getActivity()).galleryAddPhoto();
            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("How would you like to add photo?").create();
    } // end onCreateDialog

} // end QrAddScreenAddPhotoFragment Class
