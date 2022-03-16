package com.example.qrchaser.player.myQRCodes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.player.browse.BrowseActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyQRCodeScreenActivity extends SaveANDLoad {
    private Button button2,button3,button4;
    private FloatingActionButton addQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode_screen);
        // ************************** Still need to add actual activity functionality ****************************************
        // Get the player email in order to load the data from the database
        String playerEmail = loadData(getApplicationContext(), "UserEmail");
        // Get Player info from the database here

        // Using a dummy player for now
        // TODO: 2022-03-12 Pass In Actual Players
        Player currentPlayer = new Player(playerEmail,
                "TestPassword", "TestPlayer", "123" );

        // ************************** Page Selection ****************************************
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        addQR = findViewById(R.id.floatingActionButton);

        // Head to Browse Players
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, BrowseActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button2.setOnClickListener

        // Head to Map Screen
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button3.setOnClickListener

        // Head to Player Profile
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, PlayerProfileActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button4.setOnClickListener

        // Head to qr add screen
        addQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, QrAddScreenActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end addQR.setOnClickListener
    } // end onCreate
} // end MyQRCodeScreenActivity Class