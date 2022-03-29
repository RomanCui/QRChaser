package com.example.qrchaser.player.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.logIn.WelcomeActivity;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.PlayerNumQRComparator;
import com.example.qrchaser.oop.PlayerSingleScoreComparator;
import com.example.qrchaser.oop.PlayerTotalScoreComparator;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
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

import java.util.ArrayList;
import java.util.Collections;

/**
 * This Activity Class is opened when a user vists another players profile (Shows the data of the player who's profile is being visted)
 */
public class FoundPlayerProfileActivity extends SaveANDLoad {
    // UI
    private TextView nickname_text, num_QR_ranking_text, total_score_ranking_text, single_score_ranking_text, num_QR_text, total_score_text, single_score_text;
    private Button backButton, deleteButton;
    private ArrayList<Player> players = new ArrayList<>();
    // General Data
    private Player desiredPlayer, currentPlayer;
    private int num_QR_ranking, total_score_ranking, single_score_ranking;
    // Database
    private FirebaseFirestore db;
    private final String TAG = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_player_profile);

        nickname_text = findViewById(R.id.desired_player_nickname);
        num_QR_text = findViewById(R.id.num_qr_2);
        total_score_text = findViewById(R.id.total_score_2);
        single_score_text = findViewById(R.id.single_score_2);
        num_QR_ranking_text = findViewById(R.id.num_qr_ranking_2);
        total_score_ranking_text = findViewById(R.id.total_score_ranking_2);
        single_score_ranking_text = findViewById(R.id.single_score_ranking_2);
        backButton = findViewById(R.id.button_back);
        deleteButton = findViewById(R.id.button_delete);


        //Initialize database Access
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


        // Get Desired Player info from the database
        String desiredPlayerID = getIntent().getStringExtra("playerID");
        DocumentReference desiredAccount = accountsRef.document(desiredPlayerID);
        // Get the document, forcing the SDK to use the offline cache
        desiredAccount.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    desiredPlayer = documentSnapshot.toObject(Player.class);
                    nickname_text.setText(desiredPlayer.getNickname());
                    num_QR_text.setText(String.valueOf(desiredPlayer.getNumQR()));
                    total_score_text.setText(String.valueOf(desiredPlayer.getTotalScore()));
                    single_score_text.setText(String.valueOf(desiredPlayer.getHighestScore()));

                    accountsRef
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Player player = document.toObject(Player.class);
                                            players.add(player);
                                        }// Populate the listview

                                        Collections.sort(players, new PlayerNumQRComparator());
                                        for (int i = 0; i < players.size(); i++){
                                            if (desiredPlayer.getUniqueID().equals(players.get(i).getUniqueID())){
                                                num_QR_ranking = i + 1;
                                            }
                                        }

                                        Collections.sort(players, new PlayerTotalScoreComparator());
                                        for (int i = 0; i < players.size(); i++){
                                            if (desiredPlayer.getUniqueID().equals(players.get(i).getUniqueID())){
                                                total_score_ranking = i + 1;
                                            }
                                        }

                                        Collections.sort(players, new PlayerSingleScoreComparator());
                                        for (int i = 0; i < players.size(); i++){
                                            if (desiredPlayer.getUniqueID().equals(players.get(i).getUniqueID())){
                                                single_score_ranking = i + 1;
                                            }
                                        }

                                        num_QR_ranking_text.setText(String.valueOf(num_QR_ranking));
                                        total_score_ranking_text.setText(String.valueOf(total_score_ranking));
                                        single_score_ranking_text.setText(String.valueOf(single_score_ranking));

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(),"Document does not exist. Please try again",Toast.LENGTH_LONG).show();
                }
            } // end onSuccess
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                Toast.makeText(getApplicationContext(),"Player Not Found",Toast.LENGTH_LONG).show();
            } // end onFailure
        });

        // Back button - return to previous activity
        backButton.setOnClickListener( v -> {
            finish();
        });

        // Delete button - Delete player and return to previous activity
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FoundPlayerProfileActivity.this);
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
                        db.collection("Accounts").document(desiredPlayerID)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Player successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting Player", e);
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

            } // end onClick
        });// end deleteButton.setOnClickListener
    } // end onCreate
} // end PlayerProfileActivity Class