package com.example.qrchaser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.j256.ormlite.stmt.query.In;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class QrAddScreenActivity extends AppCompatActivity {

    private Button scan, addPhoto, addLocation, confirm, cancel;
    private EditText nicknameET, commentET;
    private Bitmap image;
    private ImageView imageView;
    String qrName, qrComment;
    String qrValue;
    final String TAG = "Sample";
    FirebaseFirestore db;

    private ActivityResultLauncher<Intent> galleryResultLauncher;
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    private FusedLocationProviderClient fusedLocationProviderClient;
    // Both impossible values
    private double latitude = 200;
    private double longitude  = 200;
    private boolean SetLocation = false;
    private int qrSize = 0;

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

        imageView = findViewById(R.id.qr_image_preview_imageView);

        //scanner result receiver
        ActivityResultLauncher<Intent> scannerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent scannerResult = result.getData();
                            qrValue = scannerResult.getStringExtra("qrValue");

                            //for testing the result
                            Toast.makeText(getApplicationContext(), qrValue, Toast.LENGTH_SHORT).show();
                        }
                    } // end onActivityResult
                }
        );

        //gallery result receiver
        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri uri = result.getData().getData();
                            try {
                                image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageView.setImageBitmap(image);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } // end onActivityResult
                    }
                }
        );

        //camera result receiver
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = result.getData().getExtras();
                            image = (Bitmap) bundle.get("data");
                            imageView.setImageBitmap(image);
                        } // end onActivityResult
                    }
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
                //Launch a fragment and ask user to use camera or gallery
                new QrAddScreenAddPhotoFragment().show(getSupportFragmentManager(), "QR_ADD_PHOTO");
            } // end onClick
        });

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2022-03-13 Implement Better
                SetLocation = true;

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
                if (image != null) { photoCheck = true; }
                // Do other checks here
                QRCode scannedQR;
                if (nameCheck && scanCheck) {

                    db = FirebaseFirestore.getInstance();

                    // Get a top level reference to the collection
                    final CollectionReference QRCodesReference =
                            db.collection("QRCodes");



                    db.collection("QRCodes")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int count = 0;
                                        for (DocumentSnapshot document : task.getResult()) {
                                            count++;
                                            qrSize = count;
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    // Call QRCode constructor here
                    if(SetLocation) {
                        scannedQR = new QRCode(qrValue, qrName, latitude, longitude, qrSize);
                    } else {
                        scannedQR = new QRCode(qrValue, qrName, 200, 200, qrSize);
                    }

                    // For testing
                    int score = scannedQR.getScore();
                    String scoreTestString = String.valueOf(score);
                    String hashTestString = scannedQR.getHash();
                    String testString = "score: " + scoreTestString + " hash: " + hashTestString;

                    // For Testing
                    Toast.makeText(getApplicationContext(), testString, Toast.LENGTH_SHORT).show();


                    //TODO: Compress image here
//                    if(photoCheck) {
//                    }



                    HashMap<String, String> qrCodes = new HashMap<>();
                    if (scannedQR.getId().length()>0) {
                        // If there’s some data in the EditText field, then we create a new key-value pair.
                        qrCodes.put("ID", scannedQR.getId());
                        qrCodes.put("Name", scannedQR.getName());
                        qrCodes.put("HashValue", scannedQR.getHash());
                        qrCodes.put("Lat", Double.toString(scannedQR.getLatitude()));
                        qrCodes.put("Lon", Double.toString(scannedQR.getLongitude()));
                        qrCodes.put("Lon", Double.toString(scannedQR.getLongitude()));
                        qrCodes.put("Score", Double.toString(scannedQR.getScore()));
                    }

                    QRCodesReference
                            .document(scannedQR.getId())
                            .set(qrCodes)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });

                    QRCodesReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                        }
                    });

                    //TODO: Create comment here (Need User object)
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


    /**
     *  Create intent for image capture, and launch the camera activity for the user to take a picture
     */
    public void cameraAddPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResultLauncher.launch(intent);

    }


    /**
     *  Create intent for selecting image, and launch the gallery activity for the user to select a picture
     */
    public void galleryAddPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryResultLauncher.launch(intent);

    }
} // end QrAddScreenActivity Class
