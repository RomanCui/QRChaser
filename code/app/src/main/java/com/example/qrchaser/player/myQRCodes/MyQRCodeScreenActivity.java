package com.example.qrchaser.player.myQRCodes;


import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.qrchaser.general.QRCodeAdapter;
import com.example.qrchaser.oop.QRCode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.player.browse.BrowseActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyQRCodeScreenActivity extends SaveANDLoad {

    final String TAG = "Sample";
    FirebaseFirestore db;

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton addQR;

    private ListView myQRCodeListView;
    private ArrayAdapter<QRCode> QRCodeAdapter;
    private ArrayList<QRCode> QRCodeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode_screen);

        // Get the player email in order to load the data from the database
        String playerEmail = loadData(getApplicationContext(), "UserEmail");

        myQRCodeListView = findViewById(R.id.listViewQRCode);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        addQR = findViewById(R.id.floatingActionButton);

        //ListView of this activity
        QRCodeList = new ArrayList<>();
        QRCode newQR = new QRCode("aaa","qr1","zhendgao@ualberta.ca","no",10.5,10.5);
        QRCodeList.add(newQR);
        QRCodeAdapter = new QRCodeAdapter(this,QRCodeList);
        myQRCodeListView.setAdapter(QRCodeAdapter);
        

        myQRCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, EditQRCodeScreenActivity.class);
                startActivity(intent);
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