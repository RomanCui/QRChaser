package com.example.qrchaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MyQRCodeScreenActivity extends SaveANDLoad {

    private ListView myQRCodeListView;
    private BottomNavigationView bottomNavigationView;
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
        Player currentPlayer = new Player(playerEmail, "TestPassword", "TestPlayer" );

        // ************************** Page Selection ****************************************
        myQRCodeListView = findViewById(R.id.listViewQRCode);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        addQR = findViewById(R.id.floatingActionButton);

        myQRCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, EditQRCodeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });


        bottomNavigationView.setSelectedItemId(R.id.my_qr_code);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
                        return true;
                    case R.id.browse_player:
                        startActivity(new Intent(getApplicationContext(),BrowseActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.self_profile:
                        startActivity(new Intent(getApplicationContext(),PlayerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


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