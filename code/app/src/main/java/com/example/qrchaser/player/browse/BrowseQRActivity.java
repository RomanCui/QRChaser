package com.example.qrchaser.player.browse;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.qrchaser.general.QRCodeAdapter;
import com.example.qrchaser.oop.QRCode;
import com.example.qrchaser.oop.QRCodeScoreComparator1;
import com.example.qrchaser.oop.QRCodeScoreComparator2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.qrchaser.R;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;

// TODO: 2022-03-27  A twin activity to browse players is to be developed? (Who wrote this and what does it mean)
/**
 * This Activity Class allows the user to browse QR codes game wide
 */
public class BrowseQRActivity extends AppCompatActivity {
    // UI
    private BottomNavigationView bottomNavigationView,topNavigationView;
    private ImageButton highToLowButton, lowToHighButton;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    private ListView myQRCodeListView;
    // General Data
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    // Database
    private final String TAG = "Error";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qr);

        myQRCodeListView = findViewById(R.id.browse_qr_listView);
        highToLowButton = findViewById(R.id.highToLow_button);
        lowToHighButton = findViewById(R.id.lowToHigh_button);

        // Read all QR Codes in this game into an array of QRCode objects
        db = FirebaseFirestore.getInstance();
        CollectionReference QRCodesReference = db.collection("QRCodes");
        QRCodesReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                QRCode qrCode = document.toObject(QRCode.class);
                                qrCodes.add(qrCode);
                            }
                            Collections.sort(qrCodes);
                            // Populate the listview
                            qrCodeAdapter = new QRCodeAdapter(BrowseQRActivity.this,qrCodes);
                            myQRCodeListView.setAdapter(qrCodeAdapter);
                        } else {
                            Log.d(TAG,"Error getting documents: ", task.getException());
                        }
                    } // end onComplete
                }); // end QRCodesReference.get().addOnCompleteListener

        highToLowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(qrCodes, new QRCodeScoreComparator1());
                qrCodeAdapter.notifyDataSetChanged();
            } // end onClick
        }); // end highToLowButton.setOnClickListener

        lowToHighButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(qrCodes, new QRCodeScoreComparator2());
                qrCodeAdapter.notifyDataSetChanged();
            } // end onClick
        }); // end lowToHighButton.setOnClickListener

        // Click on the Name to see details about the QR Code
        myQRCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QRCode selectedQrCode = qrCodeAdapter.getItem(position);
                Intent intent = new Intent(BrowseQRActivity.this, QRCodeInfoActivity.class);
                intent.putExtra("qrHash", selectedQrCode.getHash());
                startActivity(intent);
            } // end onItemClick
        }); // end myQRCodeListView.setOnItemClickListener

        // ******************************** Page Selection ****************************************
        // Bottom Navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.browse_qr);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_qr_code:
                        startActivity(new Intent(getApplicationContext(),MyQRCodeScreenActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.browse_qr:
                        return true;
                    case R.id.map:
                        if(checkCoarseLocationPermission() && checkFineLocationPermission() && checkInternetPermission() && checkWritePermission()) {
                            launchMap();
                        } else {
                            requestMapPermissions();
                        }
                        return true;
                    case R.id.self_profile:
                        startActivity(new Intent(getApplicationContext(),PlayerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            } // end onNavigationItemSelected
        }); // end bottomNavigationView.setOnItemSelectedListener

        topNavigationView = findViewById(R.id.top_navigation);
        topNavigationView.setSelectedItemId(R.id.browse_qr_code);
        topNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.browse_qr_code:
                        return true;
                    case R.id.browse_other_players:
                        startActivity(new Intent(getApplicationContext(), BrowsePlayerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            } // end onNavigationItemSelected
        }); // end topNavigationView.setOnItemSelectedListener

    } // end onCreate

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkCoarseLocationPermission() {
        return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    } // end checkCoarseLocationPermission

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkFineLocationPermission() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    } // end checkFineLocationPermission

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkWritePermission() {
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    } // end checkWritePermission

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkInternetPermission() {
        return checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    } // end checkInternetPermission

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMapPermissions() {
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);
    } // end requestMapPermissions

    private void launchMap() {
        startActivity(new Intent(getApplicationContext(),MapActivity.class));
        overridePendingTransition(0,0);
    } // end launchMap

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(checkCoarseLocationPermission() && checkFineLocationPermission() && checkWritePermission()) {
            launchMap();
        }
    } // end onRequestPermissionsResult
} // end BrowseActivity Class
