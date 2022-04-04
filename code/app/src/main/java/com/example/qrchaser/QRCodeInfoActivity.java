package com.example.qrchaser;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrchaser.general.CommentAdapter;
import com.example.qrchaser.general.PlayerAdapter;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Comments;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.QRCode;
import com.example.qrchaser.player.profile.FoundPlayerProfileActivity;
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

import java.util.ArrayList;
import java.util.List;

public class QRCodeInfoActivity extends SaveANDLoad implements DeleteCommentFragment.OnFragmentInteractionListener {
    // UI
    private TextView qrName, score, location;
    private ListView commentsListView, playersListView;
    private ImageView imageView;
    private Button backButton, deleteButton;
    // General Data
    private String hash;
    private QRCode qrCode;
    private ArrayAdapter<Comments> commentsAdapter;
    private ArrayAdapter<Player> playersAdapter;
    private Player currentPlayer;
    // Database
    private FirebaseFirestore db;
    private final String TAG = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_info);

        hash = getIntent().getStringExtra("qrHash");

        qrName = findViewById(R.id.qrcode_info_qrname_textView);
        score = findViewById(R.id.qrcode_info_score_textView);
        commentsListView = findViewById(R.id.qrcode_info_comment_listView);
        playersListView = findViewById(R.id.qrcode_info_players_listView);
        location = findViewById(R.id.qrcode_info_location_textView);
        imageView = findViewById(R.id.qrcode_info_imageView);
        backButton = findViewById(R.id.qrcode_info_back_button);
        deleteButton = findViewById(R.id.qrcode_info_delete_button);

        // back button -> return to previous activity
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

                        commentsAdapter = new CommentAdapter(QRCodeInfoActivity.this, 0, qrCode.getComments());
                        commentsListView.setAdapter(commentsAdapter);


                        ArrayList<Player> playerNames = new ArrayList<Player>();
                        // Initialize database Access
                        CollectionReference accountsRef = db.collection("Accounts");
                        // Source can be CACHE, SERVER, or DEFAULT.
                        Source source = Source.CACHE;

                        for (String player: qrCode.getOwners()) {
                            DocumentReference account = accountsRef.document(player);
                            // Get the document, forcing the SDK to use the offline cache
                            account.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        // Document found in the offline cache
                                        DocumentSnapshot document = task.getResult();
                                        Player tempPlayer = document.toObject(Player.class);
                                        if (tempPlayer != null) {
                                            playerNames.add(tempPlayer);
                                            Log.d("Player*****", ""+ tempPlayer.getNickname());

                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Owner List Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }); // end addOnCompleteListener
                        }
                        playersAdapter = new PlayerAdapter(QRCodeInfoActivity.this, 0, playerNames);
                        playersListView.setAdapter(playersAdapter);


                    } else {
                        Log.d("queryQR","QR does not exist");
                    }
                } else {
                    Log.d("queryQR","Error getting documents: ", task.getException());
                }
            }
        });

        // Check If the player is an admin
        // Initialize database Access
        db = FirebaseFirestore.getInstance();
        CollectionReference accountsRef = db.collection("Accounts");
        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;

        // Get the desired player id in order to load the data from the database
        String currentPlayerID = loadData(getApplicationContext(), "uniqueID");
        DocumentReference myAccount = accountsRef.document(currentPlayerID);
        // Get the document, forcing the SDK to use the offline cache
        myAccount.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    currentPlayer = document.toObject(Player.class);
                    if (currentPlayer.isAdmin()){
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Admin Authentication Failed",Toast.LENGTH_LONG).show();
                }
            }
        }); // end addOnCompleteListener

        //Launch fragment that prompt the admin to delete the comment
        commentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentPlayer.isAdmin()){
                    new DeleteCommentFragment(i).show(getSupportFragmentManager(), "DELETE_COMMENT");
                }
            } // end onItemClick
        });

        // Delete button - Delete QR code and return to previous activity
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QRCodeInfoActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.delete_dialog, null);
                Button confirm = (Button) mView.findViewById(R.id.button_confirm);
                Button cancel = (Button) mView.findViewById(R.id.button_cancel);
                dialogBuilder.setView(mView);

                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        db.collection("QRCodes").document(hash)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "QR code successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting QR code", e);
                                    }
                                });
                        finish();
                    } // end onClick
                }); // end confirm.setOnClickListener(new View.OnClickListener()

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    } // end onClick
                }); // end confirm.setOnClickListener(new View.OnClickListener()
            }
        });
    } // end onCreate

    private List<String> getAllPlayerNames(List<String> playerOwners) {
        List<String> playerNames = new ArrayList<String>();

        return playerNames;
    } // end getAllPlayerNames

    /**
     * Use QRCode information to update textViews
     */
    private void updateViewData() {
        qrName.setText("Name: " + qrCode.getName());
        score.setText("Score: " + qrCode.getScore());
        location.setText("Latitude: " + qrCode.getLatitude() + " Longitude: " + qrCode.getLongitude());
    } // end updateViewData

    // Same code as EditQRCodeScreenActivity
    // TODO: find a way to reuse this method
    private void updateImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://qrchaseredition2.appspot.com");
        StorageReference imgReference = storageReference.child(qrCode.getHash() + ".jpg");

        //set max img size to ~100kb, most image size should be around 5kb
        imgReference.getBytes(100000)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //create Bitmap from data and set imageView
                        Bitmap imgBM = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(imgBM);
                        Log.d("LoadImg","Img Found");
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.d("LoadImg","Img not found");
                    } // end onFailure
                });
    } // end updateImageView

    /**
     * Delete the comment from the QR Code, update the database and ListView
     * @param index
     */
    @Override
    public void onDeletePressed(int index) {
        qrCode.deleteCommentAt(index);
        qrCode.saveToDatabase();
        commentsAdapter.notifyDataSetChanged();
    }

} // end QRCodeInfoActivity Class
