package com.example.qrchaser.player.myQRCodes;

import static com.example.qrchaser.general.SaveANDLoad.loadData;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.example.qrchaser.R;
import com.example.qrchaser.oop.Comments;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.QRCode;
import com.example.qrchaser.oop.QRCodeScoreComparator1;
import com.example.qrchaser.player.CameraScannerActivity;
import com.example.qrchaser.player.QrAddScreenAddPhotoFragment;
import com.example.qrchaser.player.map.SelectQRLocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This Activity Class allows the user to add QR codes.
 * The QR code must be added with a name, but may include a photo (In progress),
 * location (Under review), and a comment.
 */
public class QrAddScreenActivity extends AppCompatActivity {
    // UI
    private Button scan, addPhoto, addLocation, confirm, cancel;
    private EditText nicknameET, commentET;
    private Bitmap image;
    private ImageView imageView;
    // QR code Data
    private String qrName, qrComment, qrValue;
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    // Both impossible values as the default (For checking if a location was set or not)
    private double latitude = 200;
    private double longitude  = 200;
    // Player Data
    private Player currentPlayer;
    private int numQR, totalScore, singleScore;
    private String playerName;
    // ActivityResultLaunchers
    private ActivityResultLauncher<Intent> galleryResultLauncher;
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<Intent>  locationResultLauncher;
    // Database
    private final String TAG = "Error";
    private FirebaseFirestore db;
    // Boolean Value check
    // Ronggang(Alex) implemented comment check in the QRCode class
    private Boolean nameCheck = false;
    private Boolean scanCheck = false;
    private Boolean photoCheck = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_add_screen);

        // Setup UI
        scan = findViewById(R.id.qr_scan_button);
        addPhoto = findViewById(R.id.qr_add_photo_button);
        addLocation = findViewById(R.id.qr_add_location_button);
        confirm = findViewById(R.id.qr_confirm_button);
        cancel = findViewById(R.id.qr_add_cancel_button);
        nicknameET = findViewById(R.id.qr_nickname_edit_text);
        commentET = findViewById(R.id.qr_comments_edit_text);
        imageView = findViewById(R.id.qr_image_preview_imageView);

        // Scanner result receiver
        ActivityResultLauncher<Intent> scannerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent scannerResult = result.getData();

                            // To do: not allow the same user to scan the same QR code
                            qrValue = scannerResult.getStringExtra("qrValue");

                            //for testing the result
                            Toast.makeText(getApplicationContext(), qrValue, Toast.LENGTH_SHORT).show();
                        }
                    } // end onActivityResult
                }
        );

        // Gallery result receiver
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

        // Camera result receiver
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

        // Location result receiver
        locationResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = result.getData().getExtras();
                            latitude = (double) bundle.get("latitude");
                            longitude = (double) bundle.get("longitude");
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

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrAddScreenActivity.this, SelectQRLocationActivity.class);
                locationResultLauncher.launch(intent);

            } // end onClick
        }); // end addLocation.setOnClickListener

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // for testing
                //qrValue = "AAA";

                qrName = nicknameET.getText().toString();
                qrComment = commentET.getText().toString();

                // Check if qrValue and name are not null, then check other optional values
                if (!qrName.isEmpty()) { nameCheck = true; }
                if (qrValue != null) { scanCheck = true; }
                if (image != null) { photoCheck = true; }

                // Do other checks here

                // If the name and scan are added, the program can store a QR code
                // in the database
                if (nameCheck && scanCheck) {

                    String playerID = loadData(getApplicationContext(), "uniqueID");

                    // Get Player info from the database
                    db = FirebaseFirestore.getInstance();
                    CollectionReference accountsRef = db.collection("Accounts");
                    DocumentReference myAccount = accountsRef.document(playerID);

                    // Source can be CACHE, SERVER, or DEFAULT.
                    Source source = Source.CACHE;

                    // Get the document, forcing the SDK to use the offline cache
                    myAccount.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Document found in the offline cache
                                DocumentSnapshot document = task.getResult();
                                currentPlayer = document.toObject(Player.class);
                                playerName = currentPlayer.getNickname();
                                Log.d("name", playerName);

                                //check comments
                                Comments comments;
                                if(qrComment.equals("")) {
                                    comments = null;
                                } else {
                                    comments = new Comments(playerName,qrComment);
                                }

                                // store the scanned code to database
                                QRCode scannedQR = new QRCode(qrValue, qrName, playerID, comments, latitude, longitude);
                                scannedQR.saveToDatabase();


                                // For testing
                                int score = scannedQR.getScore();
                                String scoreTestString = String.valueOf(score);
                                String hashTestString = scannedQR.getHash();
                                String testString = "score: " + scoreTestString + " hash: " + hashTestString;

                                // For Testing
                                Toast.makeText(getApplicationContext(), testString, Toast.LENGTH_SHORT).show();

                                // Compressed the image and upload to firebase storage
                                if(photoCheck) compressAndUpload(image, scannedQR.getHash());

                                // updating the user's scores
                                CollectionReference QRCodesReference = db.collection("QRCodes");
                                QRCodesReference.whereArrayContains("owners", playerID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        QRCode qrCode = document.toObject(QRCode.class);
                                                        qrCodes.add(qrCode);
                                                    }// Populate the listview
                                                    numQR = qrCodes.size();
                                                    totalScore = 0;
                                                    for (int i = 0; i < qrCodes.size(); i++){
                                                        totalScore += qrCodes.get(i).getScore();
                                                    }
                                                    Collections.sort(qrCodes, new QRCodeScoreComparator1());
                                                    if (qrCodes.size() > 0) {
                                                        singleScore = qrCodes.get(0).getScore();
                                                    } else {
                                                        singleScore = 0;
                                                    }

                                                    currentPlayer.setNumQR(numQR);
                                                    currentPlayer.setTotalScore(totalScore);
                                                    currentPlayer.setHighestScore(singleScore);
                                                    currentPlayer.updateDatabase();

                                                    // Return to previous activity
                                                    finish();
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            } // end onComplete
                                        });

                            } else {
                                Toast.makeText(getApplicationContext(),"Load Failed",Toast.LENGTH_LONG).show();
                            }
                        } // end onComplete
                    }); // end addOnCompleteListener

                }
                // If there is a missing mandatory component, prompt the user for input
                else if(!nameCheck && !scanCheck) {
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
    } // end cameraAddPhoto


    /**
     *  Create intent for selecting image, and launch the gallery activity for the user to select a picture
     */
    public void galleryAddPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryResultLauncher.launch(intent);
    } // end galleryAddPhoto

    /**
     * Takes the bitmap image, compress it to JPG file, and upload that to firebase storage
     * @param img Bitmap image to upload
     * @param imgName name to be saved on storage
     */
    public void compressAndUpload(Bitmap img, String imgName) {
        //TODO: Implement proper authentication and rules for database
        FirebaseStorage db = FirebaseStorage.getInstance();
        StorageReference storage = db.getReferenceFromUrl("gs://qrchaseredition2.appspot.com");
        StorageReference imageRef = storage.child(imgName+".jpg");
        int imgQuality = 100;

        if(img.getByteCount() > 5000) {
            imgQuality = 50;

            // Resize image
            if (img.getWidth() > 500) {
                double scale = 250 / img.getWidth();
                int width = 250;
                int height = img.getHeight() * 250 / img.getWidth();

                Bitmap resizedImg = Bitmap.createScaledBitmap(img, width, height, false);
                img.recycle();
                img = resizedImg;
            }
        }

        // Compress the image and convert it to byte array
        ByteArrayOutputStream imgBuffer = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, imgQuality, imgBuffer); //compress to smallest size
        byte[] imageBytes = imgBuffer.toByteArray();

        imageRef.putBytes(imageBytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getMetadata().getPath();
                Toast.makeText(getApplicationContext(), downloadUrl, Toast.LENGTH_SHORT).show();
            } // end onSuccess
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "upload failed", Toast.LENGTH_SHORT).show();
            } // end onFailure
        });
    } // end compressAndUpload
} // end QrAddScreenActivity Class
