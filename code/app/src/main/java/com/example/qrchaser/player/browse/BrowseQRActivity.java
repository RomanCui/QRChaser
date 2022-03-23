package com.example.qrchaser.player.browse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

// This activity allows user to browse QR codes game wide
// A twin activity to browse players is to be developed
public class BrowseQRActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
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

        myQRCodeListView = findViewById(R.id.listViewQRCode);
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
                    }
                });

        highToLowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(qrCodes, new QRCodeScoreComparator1());
                qrCodeAdapter.notifyDataSetChanged();
            }
        });

        lowToHighButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(qrCodes, new QRCodeScoreComparator2());
                qrCodeAdapter.notifyDataSetChanged();
            }
        });

        // The navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.browse_player);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
                        startActivity(new Intent(getApplicationContext(),MyQRCodeScreenActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.browse_player:
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



    } // end onCreate
} // end BrowseActivity Class