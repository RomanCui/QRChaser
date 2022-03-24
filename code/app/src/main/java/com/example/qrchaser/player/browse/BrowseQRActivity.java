package com.example.qrchaser.player.browse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.qrchaser.QRcodeInfoActivity;
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

// This activity allows user to browse QR codes game wide
// A twin activity to browse players is to be developed
public class BrowseQRActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView,topNavigationView;
    final String TAG = "Sample";
    FirebaseFirestore db;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    ListView myQRCodeListView;
    ImageButton highToLowButton;
    ImageButton lowToHighButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_qr);

        myQRCodeListView = findViewById(R.id.browse_qr_listView);
        highToLowButton = findViewById(R.id.highToLow_button);
        lowToHighButton = findViewById(R.id.lowToHigh_button);

        // Read all QR codes in this game into an array of QRCode objects
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
                            Log.d(TAG, "Error getting documents: ", task.getException());
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

        // Click on the Name to see details about the code
        myQRCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QRCode selectedQrCode = qrCodeAdapter.getItem(position);
                Intent intent = new Intent(BrowseQRActivity.this, QRcodeInfoActivity.class);
                intent.putExtra("qrHash", selectedQrCode.getHash());
                startActivity(intent);
            } // end onItemClick
        }); // end myQRCodeListView.setOnItemClickListener

        // ************************** Page Selection ****************************************
        // Bottom Navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.browse_qr);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
                        startActivity(new Intent(getApplicationContext(),MyQRCodeScreenActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.browse_qr:
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
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            } // end onNavigationItemSelected
        }); // end topNavigationView.setOnItemSelectedListener

    } // end onCreate
} // end BrowseActivity Class