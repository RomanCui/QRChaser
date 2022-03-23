package com.example.qrchaser.player.browse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.qrchaser.R;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BrowsePlayerActivity extends AppCompatActivity {

    BottomNavigationView topNavigationView,bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_player);

        //bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.browse_qr);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
                        startActivity(new Intent(getApplicationContext(), MyQRCodeScreenActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.browse_qr:
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.self_profile:
                        startActivity(new Intent(getApplicationContext(), PlayerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //top Navigation bar
        topNavigationView = findViewById(R.id.top_navigation);
        topNavigationView.setSelectedItemId(R.id.browse_other_players);
        topNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.browse_qr_code:
                        startActivity(new Intent(getApplicationContext(), BrowseQRActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.browse_other_players:
                        return true;
                }
                return false;
            }
        });
    }
}