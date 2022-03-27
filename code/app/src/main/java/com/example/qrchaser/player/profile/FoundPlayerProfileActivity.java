package com.example.qrchaser.player.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.PlayerNumQRComparator;
import com.example.qrchaser.oop.PlayerSingleScoreComparator;
import com.example.qrchaser.oop.PlayerTotalScoreComparator;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private Button backButton;
    private ArrayList<Player> players = new ArrayList<>();
    // General Data
    private Player currentPlayer;
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

        // Back button - return to previous activity
        backButton.setOnClickListener( v -> {
            finish();
        });

        // Get the player id in order to load the data from the database
        String playerID = getIntent().getStringExtra("playerID");


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
                } else {
                    Toast.makeText(getApplicationContext(),"Load Failed",Toast.LENGTH_LONG).show();
                }
                nickname_text.setText(currentPlayer.getNickname());
                num_QR_text.setText(String.valueOf(currentPlayer.getNumQR()));
                total_score_text.setText(String.valueOf(currentPlayer.getTotalScore()));
                single_score_text.setText(String.valueOf(currentPlayer.getHighestScore()));

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
                                        if (currentPlayer.getUniqueID().equals(players.get(i).getUniqueID())){
                                            num_QR_ranking = i + 1;
                                        }
                                    }

                                    Collections.sort(players, new PlayerTotalScoreComparator());
                                    for (int i = 0; i < players.size(); i++){
                                        if (currentPlayer.getUniqueID().equals(players.get(i).getUniqueID())){
                                            total_score_ranking = i + 1;
                                        }
                                    }

                                    Collections.sort(players, new PlayerSingleScoreComparator());
                                    for (int i = 0; i < players.size(); i++){
                                        if (currentPlayer.getUniqueID().equals(players.get(i).getUniqueID())){
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
            }
        }); // end addOnCompleteListener
    } // end onCreate

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    } // end onRestart
} // end PlayerProfileActivity Class