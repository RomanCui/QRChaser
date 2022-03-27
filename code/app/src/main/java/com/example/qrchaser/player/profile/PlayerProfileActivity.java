package com.example.qrchaser.player.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.example.qrchaser.player.browse.BrowseQRActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
 * This Activity Class shows off the player profile and that data contained in their account (such as qr codes and total score)
 */
public class PlayerProfileActivity extends SaveANDLoad {
    // UI
    private Button buttonPlayerInfo;
    private BottomNavigationView bottomNavigationView;
    private TextView nickname_text, num_QR_text, total_score_text, single_score_text, num_QR_ranking_text, total_score_ranking_text, single_score_ranking_text;
    // Database
    private FirebaseFirestore db;
    final String TAG = "Error";
    // General Data
    private ArrayList<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private int num_QR_ranking, total_score_ranking, single_score_ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        nickname_text = findViewById(R.id.desired_player_nickname);
        num_QR_text = findViewById(R.id.num_qr_2);
        total_score_text = findViewById(R.id.total_score_2);
        single_score_text = findViewById(R.id.single_score_2);
        num_QR_ranking_text = findViewById(R.id.num_qr_ranking_2);
        total_score_ranking_text = findViewById(R.id.total_score_ranking_2);
        single_score_ranking_text = findViewById(R.id.single_score_ranking_2);

        // Get the player id in order to load the data from the database
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
                            } // end onComplete
                        }); // end accountsRef.get().addOnCompleteListener
            } // end onComplete
        }); // end myAccount.get(source).addOnCompleteListener


        // ************************** Page Selection ****************************************
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        buttonPlayerInfo = findViewById(R.id.desired_player_info_button);
        // Head to My QR Code Screen
        bottomNavigationView.setSelectedItemId(R.id.self_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
                        startActivity(new Intent(getApplicationContext(), MyQRCodeScreenActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.browse_qr:
                        startActivity(new Intent(getApplicationContext(), BrowseQRActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.self_profile:

                        return true;
                }
                return false;
            } // end onNavigationItemSelected
        });

        // Head to Player Profile Info
        buttonPlayerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileActivity.this, EditPlayerProfileActivity.class);
                startActivity(intent);
            } // end onClick
        }); // end buttonPlayerInfo.setOnClickListener

    } // end onCreate

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    } // end onRestart
} // end PlayerProfileActivity Class