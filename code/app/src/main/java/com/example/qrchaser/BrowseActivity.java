package com.example.qrchaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BrowseActivity extends AppCompatActivity {
    private Button button1,button3,button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        // ************************** Still need to add actual activity functionality ****************************************


        // ************************** Page Selection ****************************************
        button1 = findViewById(R.id.button1);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        // Head to My QR Code Screen
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseActivity.this, MyQRCodeScreenActivity.class);
                startActivity(intent);
                finish();
            }  // end onClick
        }); // end button1.setOnClickListener

        // Head to Map Screen
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }  // end onClick
        }); // end button3.setOnClickListener

        // Head to Player Profile
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseActivity.this, PlayerProfileActivity.class);
                startActivity(intent);
                finish();
            }  // end onClick
        }); // end button4.setOnClickListener

    } // end onCreate
} // end BrowseActivity Class