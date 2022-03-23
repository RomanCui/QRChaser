package com.example.qrchaser.player.profile;


import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.browse.BrowseQRActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


public class PlayerProfileActivity extends SaveANDLoad {
    private Button buttonPlayerInfo;
    private BottomNavigationView bottomNavigationView;
    private TextView nicknameTV;
    private FirebaseFirestore db;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

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
                    currentPlayer = new Player(document.getString("email"), document.getString("nickname"), document.getString("phoneNumber"), Boolean.parseBoolean(document.getString("admin")), playerID);
                } else {
                    Toast.makeText(getApplicationContext(),"Load Failed",Toast.LENGTH_LONG).show();
                }
            }
        }); // end addOnCompleteListener

        nicknameTV = findViewById(R.id.desired_player_nickname);
        nicknameTV.setText(currentPlayer.getNickname());
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
} // end PlayerProfileActivity Class