package com.example.qrchaser.player.myQRCodes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrchaser.R;
import com.example.qrchaser.general.QRCodeAdapter;
import com.example.qrchaser.oop.QRCode;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.List;

// To be completed
public class EditQRCodeScreenActivity extends AppCompatActivity {

    private String hash;
    private FirebaseFirestore db;
    private QRCode qrCode;

    ImageView qrImageView;
    TextView qrName;
    TextView qrScore;
    TextView qrComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_qrcode_screen);

        qrName = findViewById(R.id.qr_code_name_textView);
        qrScore = findViewById(R.id.qr_score_textView);
        qrComments = findViewById(R.id.qr_comments_textView);
        qrImageView = findViewById(R.id.qr_imageView);

        hash = getIntent().getStringExtra("qrHash");

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
                    } else {
                        Log.d("queryQR", "QR does not exist");
                    }
                } else {
                    Log.d("queryQR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void updateViewData() {
        qrName.setText("Name: " + qrCode.getName());
        qrScore.setText(String.valueOf("Score: " + qrCode.getScore()));

        List<String> comments = qrCode.getComments();
        if(!comments.isEmpty()) {
            //TODO: change qrComments to listview and display the whole list
            qrComments.setText("Comments: " + comments.get(0));
        }
    }

    //return true if qr has image
    private void updateImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://qrchaseredition2.appspot.com");
        StorageReference imgReference = storageReference.child(qrCode.getName() + ".jpg");

        //set max img size to ~10kb, most image size should be around 5kb
        imgReference.getBytes(10000)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //create Bitmap from data and set imageView
                Bitmap imgBM = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                qrImageView.setImageBitmap(imgBM);
                Log.d("LoadImg", "Img Found");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.d("LoadImg", "Img not found");
            }
        });
    }
}