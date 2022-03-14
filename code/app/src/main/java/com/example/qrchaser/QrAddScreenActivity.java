package com.example.qrchaser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class QrAddScreenActivity extends AppCompatActivity {

    private Button scan, addPhoto, addLocation, confirm, cancel;
    private EditText nicknameET, commentET;
    String qrName, qrComment;
    String qrValue;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_add_screen);

        scan = findViewById(R.id.qr_scan_button);
        addPhoto = findViewById(R.id.qr_add_photo_button);
        addLocation = findViewById(R.id.qr_add_location_button);
        confirm = findViewById(R.id.qr_confirm_button);
        cancel = findViewById(R.id.qr_add_cancel_button);

        nicknameET = findViewById(R.id.qr_nickname_edit_text);
        commentET = findViewById(R.id.qr_comments_edit_text);

        ActivityResultLauncher<Intent> scannerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent scannerResult = result.getData();
                            qrValue = scannerResult.getStringExtra("qrValue");

                            //for testing the result
                            Toast.makeText(getApplicationContext(), qrValue, Toast.LENGTH_SHORT).show();
                        }
                    } // end onActivityResult
                }
        );

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrAddScreenActivity.this, CameraScannerActivity.class);
                scannerResultLauncher.launch(intent);
            } // end onClick
        }); // end scan.setOnClickListener

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2022-03-13 Implement
            } // end onClick
        }); // end addPhoto.setOnClickListener

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2022-03-13 Implement

            } // end onClick
        }); // end addLocation.setOnClickListener

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean nameCheck = false;
                Boolean commentCheck = false;
                Boolean scanCheck = false;
                Boolean photoCheck = false;
                Boolean locationCheck = false;


                qrName = nicknameET.getText().toString();
                qrComment = commentET.getText().toString();
                // Check if qrValue nad name are not null, then check other optional values


                if (!qrName.isEmpty()) { nameCheck = true; }
                if (!qrComment.isEmpty()) { commentCheck = true; }
                if (qrValue != null) { scanCheck = true; }
                // Do other checks here

                if (nameCheck && scanCheck) {
                    // Call QRCode constructor here
                    QRCode scannedQR = new QRCode(qrValue, qrName);

                    // For testing
                    int score = scannedQR.getScore();
                    String scoreTestString = String.valueOf(score);
                    String hashTestString = scannedQR.getHash();
                    String testString = "score: " + scoreTestString + " hash: " + hashTestString;

                    // For Testing
                    Toast.makeText(getApplicationContext(), testString, Toast.LENGTH_SHORT).show();


                    //TODO: Need User object
//                    if(commentCheck) {
//                        QRComment(new User(), qrComment);
//                    }
                } else if(!nameCheck && !scanCheck) {
                    Toast.makeText(getApplicationContext(), "Please Scan and Enter nickname for this QRCode", Toast.LENGTH_SHORT).show();
                } else if(!scanCheck) {
                    Toast.makeText(getApplicationContext(), "Please Scan the QRCode", Toast.LENGTH_SHORT).show();
                } else if (!nameCheck) {
                    Toast.makeText(getApplicationContext(), "Please Enter nickname for this QRCode", Toast.LENGTH_SHORT).show();
                }
            } // end onClick
        }); // end confirm.setOnClickListener

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } // end onClick
        }); // end cancel.setOnClickListener
    } // end onCreate
} // end QrAddScreenActivity Class
