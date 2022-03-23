package com.example.qrchaser.player.profile;


import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.qrchaser.oop.QRCode;
import com.example.qrchaser.oop.QRCodeScoreComparator1;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.player.browse.BrowseQRActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;


public class PlayerProfileActivity extends SaveANDLoad {
    private Button buttonPlayerInfo;
    private BottomNavigationView bottomNavigationView;
    private TextView nicknameTV;
    FirebaseFirestore db;
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    TextView num_QR_text, total_score_text, single_score_text;
    final String TAG = "Sample";
    int numQR, totalScore, singleScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        num_QR_text = findViewById(R.id.num_qr_2);
        total_score_text = findViewById(R.id.total_score_2);
        single_score_text = findViewById(R.id.single_score_2);

        // Get the player email in order for the query
        String playerEmail = loadData(getApplicationContext(), "UserEmail");

        // Find all the QR codes that belong to this player, then add the name and score
        // to array lists.
        db = FirebaseFirestore.getInstance();
        CollectionReference QRCodesReference = db.collection("QRCodes");
        QRCodesReference.whereArrayContains("owners", playerEmail)
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
                            singleScore = qrCodes.get(0).getScore();

                            num_QR_text.setText(String.valueOf(numQR));
                            total_score_text.setText(String.valueOf(totalScore));
                            single_score_text.setText(String.valueOf(singleScore));

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });




        nicknameTV = findViewById(R.id.desired_player_nickname);
        nicknameTV.setText(playerEmail);
        // ************************** Still need to add actual activity functionality ****************************************


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
                    case R.id.browse_player:
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
            }
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
} // end PlayerProfileActivity Class