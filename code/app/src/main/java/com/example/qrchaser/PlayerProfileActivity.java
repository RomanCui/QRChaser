package com.example.qrchaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerProfileActivity extends AppCompatActivity {
    private Button button1,button2,button3, buttonPlayerInfo;
    private TextView nicknameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        // Using a dummy player for now
        // TODO: 2022-03-12 Pass In Actual Players
        Player currentPlayer = new Player("TestPlayer@gmail.com", "TestPassword", "TestPlayer" );


        nicknameTV = findViewById(R.id.desired_player_nickname);
        nicknameTV.setText(currentPlayer.getNickname());
        // ************************** Still need to add actual activity functionality ****************************************


        // ************************** Page Selection ****************************************
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        buttonPlayerInfo = findViewById(R.id.desired_player_info_button);
        // Head to My QR Code Screen
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileActivity.this, MyQRCodeScreenActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button1.setOnClickListener

        // Head to Browse Players
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileActivity.this, BrowseActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button2.setOnClickListener

        // Head to Map Screen
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button3.setOnClickListener

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