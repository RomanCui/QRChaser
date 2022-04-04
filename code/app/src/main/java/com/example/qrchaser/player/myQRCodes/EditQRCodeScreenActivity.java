package com.example.qrchaser.player.myQRCodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrchaser.AddCommentFragment;
import com.example.qrchaser.QrChangeNameFragment;
import com.example.qrchaser.R;
import com.example.qrchaser.general.CommentAdapter;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Comments;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 *  This activity allows user to edit the general information about the qrcode such changing name, and adding comments
 */
public class EditQRCodeScreenActivity extends SaveANDLoad implements AddCommentFragment.OnFragmentInteractionListener, QrChangeNameFragment.OnFragmentInteractionListener {
    // UI
    private Button changeComments, changeName, removeQR, backButton;
    private ImageView qrImageView;
    private TextView qrName, qrScore;
    private ListView qrCommentsListView;
    private ArrayAdapter<Comments> commentsAdapter;
    // General Data
    private String hash;
    private Player player;
    private QRCode qrCode;
    // Database
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_qrcode_screen);

        // Setup UI
        changeComments = findViewById(R.id.qr_change_comments_button);
        changeName = findViewById(R.id.qr_change_name_button);
        removeQR = findViewById(R.id.qr_remove_button);
        backButton = findViewById(R.id.qr_edit_cancel_button);
        qrName = findViewById(R.id.qr_code_name_textView);
        qrScore = findViewById(R.id.qr_score_textView);
        qrCommentsListView = findViewById(R.id.qr_comments_ListView);
        qrImageView = findViewById(R.id.qr_imageView);

        hash = getIntent().getStringExtra("qrHash");

        // Query for the selected qrcode using hash
        db = FirebaseFirestore.getInstance();
        DocumentReference QRCodeReference = db.collection("QRCodes").document(hash);
        QRCodeReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        qrCode = document.toObject(QRCode.class);
                        updateTextView();
                        updateImageView();
                        commentsAdapter = new CommentAdapter(EditQRCodeScreenActivity.this, 0, qrCode.getComments());
                        qrCommentsListView.setAdapter(commentsAdapter);
                    } else {
                        Log.d("queryQR","QR does not exist");
                    }
                } else {
                    Log.d("queryQR","Error getting documents: ", task.getException());
                }
            } // end onComplete
        }); // end QRCodeReference.get().addOnCompleteListener

        // Query for the user using uniqueID, and construct Player object
        String playerID = loadData(getApplicationContext(), "uniqueID");
        CollectionReference accountsRef = db.collection("Accounts");
        DocumentReference myAccount = accountsRef.document(playerID);
        Source source = Source.CACHE;
        myAccount.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    player = document.toObject(Player.class);
                } else {
                    Toast.makeText(getApplicationContext(),"Load Failed",Toast.LENGTH_LONG).show();
                }
            }
        }); // end addOnCompleteListener

        // Add new comments
        changeComments.setOnClickListener((v) -> {
            new AddCommentFragment().show(getSupportFragmentManager(),"ADD_COMMENT");
        }); // end changeComments.setOnClickListener

        // Change the qr name
        changeName.setOnClickListener((v) -> {
            new QrChangeNameFragment().show(getSupportFragmentManager(),"CHANGE_QR_NAME");
        }); // end changeName.setOnClickListener

        // Remove the user from this qrcode, return to previous activity if successful
        removeQR.setOnClickListener((v) -> {
            if (qrCode.removeOwner(playerID)) {
                qrCode.saveToDatabase();
                finish();
            }
        }); // end removeQR.setOnClickListener

        // Back to previous activity
        backButton.setOnClickListener((v) -> {
            finish();
        }); // end backButton.setOnClickListener
    } // end onCreate

    /**
     * Update the Name and Score textView
     */
    private void updateTextView() {
        qrName.setText("Name: " + qrCode.getName());
        qrScore.setText("Score: " + qrCode.getScore());
    } // end updateTextView

    /**
     *  Return download the img if exist, and update imageView
     */
    private void updateImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://qrchaseredition2.appspot.com");
        StorageReference imgReference = storageReference.child(qrCode.getHash() + ".jpg");

        // Set max img size to ~100kb, most image size should be around 5kb
        imgReference.getBytes(100000)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //create Bitmap from data and set imageView
                Bitmap imgBM = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                qrImageView.setImageBitmap(imgBM);
                Log.d("LoadImg","Img Found");
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

    @Override
    public void onOkPressed(String returnedString, int returnCode) {
        // Change name fragment
        if (returnCode == 1) {
            String newName = returnedString;
            qrCode.setName(newName);
            qrCode.saveToDatabase();
            updateTextView();
        }
        // Add Comment fragment
        if (returnCode == 2) {
            String comment = returnedString;
            String username = player.getNickname();
            if(username == "") { username = "Guest"; }
            qrCode.addComment(username, comment);
            qrCode.saveToDatabase();
            commentsAdapter.notifyDataSetChanged();
        }
    } // end onOkPressed

} // end EditQRCodeScreenActivity Class
