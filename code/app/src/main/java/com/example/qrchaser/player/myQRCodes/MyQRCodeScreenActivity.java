package com.example.qrchaser.player.myQRCodes;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.qrchaser.oop.QRCode;
import com.example.qrchaser.general.QRCodeAdapter;
import com.example.qrchaser.oop.QRCodeScoreComparator1;
import com.example.qrchaser.oop.QRCodeScoreComparator2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.player.browse.BrowseQRActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This Activity Class allows the Player to see their own scanned QR Codes
 */
public class MyQRCodeScreenActivity extends SaveANDLoad {
    // UI
    private BottomNavigationView bottomNavigationView;
    private ImageButton highToLowButton, lowToHighButton;
    private FloatingActionButton addQR;
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    private ListView myQRCodeListView;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    // Database
    private final String TAG = "Error";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode_screen);

        // Setup UI
        myQRCodeListView = findViewById(R.id.listViewQRCode);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        addQR = findViewById(R.id.floatingActionButton);
        highToLowButton = findViewById(R.id.highToLow_button);
        lowToHighButton = findViewById(R.id.lowToHigh_button);

        // Get the player email in order for the query
        String playerID = loadData(getApplicationContext(),"uniqueID");

        // Find all the QR codes that belong to this player, then add the name and score
        // to array lists.
        db = FirebaseFirestore.getInstance();
        CollectionReference QRCodesReference = db.collection("QRCodes");
        QRCodesReference.whereArrayContains("owners", playerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                QRCode qrCode = document.toObject(QRCode.class);
                                qrCodes.add(qrCode);
                            } // Populate the listview
                            Collections.sort(qrCodes);
                            qrCodeAdapter = new QRCodeAdapter(MyQRCodeScreenActivity.this,qrCodes);
                            myQRCodeListView.setAdapter(qrCodeAdapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
            } // end onComplete
        });

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
                Intent intent = new Intent(MyQRCodeScreenActivity.this, EditQRCodeScreenActivity.class);
                intent.putExtra("qrHash", selectedQrCode.getHash());
                startActivity(intent);
            } // end onItemClick
        }); // end myQRCodeListView.setOnItemClickListener

        bottomNavigationView.setSelectedItemId(R.id.my_qr_code);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
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
                        startActivity(new Intent(getApplicationContext(),PlayerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            } //end onNavigationItemSelected
        });

        // Head to QRAddScreen
        addQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyQRCodeScreenActivity.this, QrAddScreenActivity.class);
                startActivity(intent);
            } // end onClick
        }); // end addQR.setOnClickListener
    } // end onCreate

    /**
     * For testing purpose: Get the ArrayList of QRCode currently present on MyQRCodeScreen
     * @return qrCodes
     */
    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    } // end getQrCodes

} // end MyQRCodeScreenActivity Class
