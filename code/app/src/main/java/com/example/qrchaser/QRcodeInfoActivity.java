package com.example.qrchaser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrchaser.general.CommentAdapter;
import com.example.qrchaser.oop.Comments;
import com.example.qrchaser.oop.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class QRcodeInfoActivity extends AppCompatActivity {
    // UI
    private TextView qrName, score, location;
    private ListView commentsListView;
    private ImageView imageView;
    private Button backButton;
    // Database
    private FirebaseFirestore db;
    // General Data
    private String hash;
    private QRCode qrCode;
    private ArrayAdapter<Comments> commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_info);

        hash = getIntent().getStringExtra("qrHash");

        qrName = findViewById(R.id.qrcode_info_qrname_textView);
        score = findViewById(R.id.qrcode_info_score_textView);
        commentsListView = findViewById(R.id.qrcode_info_comment_listView);
        location = findViewById(R.id.qrcode_info_location_textView);
        imageView = findViewById(R.id.qrcode_info_imageView);
        backButton = findViewById(R.id.qrcode_info_back_button);

        //back button - return to previous activity
        backButton.setOnClickListener( v -> {
            finish();
        });

        db = FirebaseFirestore.getInstance();
        DocumentReference QRCodeReference = db.collection("QRCodes").document(hash);
        QRCodeReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        qrCode = document.toObject(QRCode.class);
                        updateViewData();
                        updateImageView();

                        commentsAdapter = new CommentAdapter(QRcodeInfoActivity.this, 0, qrCode.getComments());
                        commentsListView.setAdapter(commentsAdapter);

                    } else {
                        Log.d("queryQR", "QR does not exist");
                    }
                } else {
                    Log.d("queryQR", "Error getting documents: ", task.getException());
                }
            }
        });
    } // end onCreate

    //Use qrCode information to update textViews
    private void updateViewData() {
        qrName.setText("Name: " + qrCode.getName());
        score.setText("Score: " + qrCode.getScore());
        location.setText("Latitude: " + qrCode.getLatitude() + " Longitude: " + qrCode.getLongitude());
    } // end updateViewData

    //Same code as EditQRCodeScreenActivity
    //TODO: find a way to reuse this method
    private void updateImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://qrchaseredition2.appspot.com");
        StorageReference imgReference = storageReference.child(qrCode.getHash() + ".jpg");

        //set max img size to ~10kb, most image size should be around 5kb
        imgReference.getBytes(10000)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //create Bitmap from data and set imageView
                        Bitmap imgBM = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(imgBM);
                        Log.d("LoadImg", "Img Found");
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.d("LoadImg", "Img not found");
                    } // end onFailure
                });
    } // end updateImageView
} // end QRcodeInfoActivity Class