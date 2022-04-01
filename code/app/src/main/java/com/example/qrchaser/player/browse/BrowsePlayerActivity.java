package com.example.qrchaser.player.browse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrchaser.R;
import com.example.qrchaser.general.PlayerAdapter1;
import com.example.qrchaser.general.PlayerAdapter2;
import com.example.qrchaser.general.PlayerAdapter3;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.PlayerNumQRComparator;
import com.example.qrchaser.oop.PlayerSingleScoreComparator;
import com.example.qrchaser.oop.PlayerTotalScoreComparator;
import com.example.qrchaser.player.CameraScannerActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.myQRCodes.QrAddScreenActivity;
import com.example.qrchaser.player.profile.FoundPlayerProfileActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This Activity Class allows the user to browse players game wide
 */
public class BrowsePlayerActivity extends AppCompatActivity {
    // UI
    private BottomNavigationView topNavigationView,bottomNavigationView;
    private ImageButton numButton, totalButton, singleButton, searchButton;
    private TextView scoreType;
    private EditText searchEditText;
    private ArrayAdapter<Player> playersAdapter1, playersAdapter2, playersAdapter3;
    private ListView playersListView;
    private int currentAdapter;
    private FloatingActionButton scanPlayerQRButton;
    // General Data
    private ArrayList<Player> players = new ArrayList<>();
    // Database
    private final String TAG = "Error";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_player);
        numButton = findViewById(R.id.num_button);
        totalButton = findViewById(R.id.total_button);
        singleButton = findViewById(R.id.single_button);
        scoreType = findViewById(R.id.score_text);
        playersListView = findViewById(R.id.listViewPlayer);
        scanPlayerQRButton = findViewById(R.id.floatingActionButtonScanPlayerQR);

        db = FirebaseFirestore.getInstance();
        CollectionReference accountsRef = db.collection("Accounts");
        accountsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            players = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = document.toObject(Player.class);
                                players.add(player);
                            }// Populate the listview

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        scoreType.setText("Number of QR");
                        playersAdapter1 = new PlayerAdapter1(BrowsePlayerActivity.this, players);
                        playersAdapter2 = new PlayerAdapter2(BrowsePlayerActivity.this, players);
                        playersAdapter3 = new PlayerAdapter3(BrowsePlayerActivity.this, players);
                        Collections.sort(players, new PlayerNumQRComparator());
                        playersAdapter1.notifyDataSetChanged();
                        playersListView.setAdapter(playersAdapter1);
                        currentAdapter = 1;
                    } // end onComplete
                }); // end accountsRef.get().addOnCompleteListener

        numButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreType.setText("Number of QR");
                Collections.sort(players, new PlayerNumQRComparator());
                playersAdapter1.notifyDataSetChanged();
                playersListView.setAdapter(playersAdapter1);
                currentAdapter = 1;
            } // end onClick
        }); // end numButton.setOnClickListener

        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreType.setText("All QR Sum");
                Collections.sort(players, new PlayerTotalScoreComparator());
                playersAdapter1.notifyDataSetChanged();
                playersListView.setAdapter(playersAdapter2);
                currentAdapter = 2;
            } // end onClick
        }); // end totalButton.setOnClickListener

        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreType.setText("Single Highest");
                Collections.sort(players, new PlayerSingleScoreComparator());
                playersAdapter1.notifyDataSetChanged();
                playersListView.setAdapter(playersAdapter3);
                currentAdapter = 3;
            } // end onClick
        }); // end singleButton.setOnClickListener

        // Click on the Name to see details about the code
        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player selectedPlayer = playersAdapter1.getItem(0) ;
                assert currentAdapter <= 3 && currentAdapter >= 1;
                if (currentAdapter == 1) {
                    selectedPlayer = playersAdapter1.getItem(position);
                } else if (currentAdapter == 2) {
                    selectedPlayer = playersAdapter2.getItem(position);
                } else if (currentAdapter == 3) {
                    selectedPlayer = playersAdapter3.getItem(position);
                }

                Intent intent = new Intent(BrowsePlayerActivity.this, FoundPlayerProfileActivity.class);
                intent.putExtra("playerID", selectedPlayer.getUniqueID());
                startActivity(intent);
            } // end onItemClick
        }); // end myQRCodeListView.setOnItemClickListener


        // Scanner result receiver
        ActivityResultLauncher<Intent> scannerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent scannerResult = result.getData();
                            String qrValue = scannerResult.getStringExtra("qrValue");
                            String[] qrDataArray = qrValue.split(",");
                            if (qrDataArray[0].contentEquals("QRCHASERINFO")){
                                Intent intent = new Intent(BrowsePlayerActivity.this, FoundPlayerProfileActivity.class);
                                intent.putExtra("playerID", qrDataArray[1]);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"Invalid QR Code",Toast.LENGTH_LONG).show();
                            }
                        }
                    } // end onActivityResult
                }
        ); // end registerForActivityResult

        // Head to scan screen
        scanPlayerQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowsePlayerActivity.this, CameraScannerActivity.class);
                scannerResultLauncher.launch(intent);
            } // end onClick
        }); // end scanPlayerQRButton.setOnClickListener

        // ************************** Page Selection ****************************************
        // Bottom Navigation bar
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
            } // end onNavigationItemSelected
        }); // end bottomNavigationView.setOnItemSelectedListener

        // Top Navigation bar
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
            } // end onNavigationItemSelected
        }); // end topNavigationView.setOnItemSelectedListener

        // Search Implementation for players
        searchEditText = (EditText) findViewById(R.id.search_editText);
        searchButton = (ImageButton) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < players.size(); i++) {
                    if (searchEditText.getText().toString().equals(players.get(i).getNickname())) {
                        Intent intent = new Intent(BrowsePlayerActivity.this, FoundPlayerProfileActivity.class);
                        intent.putExtra("playerID", players.get(i).getUniqueID());
                        startActivity(intent);;
                        break;
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Player not found, enter valid nickname.",Toast.LENGTH_LONG).show();
                    }
                }
            } // end onClick
        }); // end searchButton.setOnClickListener




    } // end onCreate

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();
        CollectionReference accountsRef = db.collection("Accounts");
        accountsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            players = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = document.toObject(Player.class);
                                players.add(player);
                            }// Populate the listview

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        playersAdapter1 = new PlayerAdapter1(BrowsePlayerActivity.this, players);
                        playersAdapter2 = new PlayerAdapter2(BrowsePlayerActivity.this, players);
                        playersAdapter3 = new PlayerAdapter3(BrowsePlayerActivity.this, players);
                        Collections.sort(players, new PlayerNumQRComparator());
                        playersAdapter1.notifyDataSetChanged();
                        playersListView.setAdapter(playersAdapter1);
                        currentAdapter = 1;
                    } // end onComplete
                }); // end accountsRef.get().addOnCompleteListener
    } // end onResume

} // end BrowsePlayerActivity Class