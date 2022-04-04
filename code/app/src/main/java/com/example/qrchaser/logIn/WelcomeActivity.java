package com.example.qrchaser.logIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.player.CameraScannerActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This Activity Class allows a Player to login with a QR Code or with a new Account
 */
public class WelcomeActivity extends SaveANDLoad {
    // UI
    private Button qrCode, createAccount;
    // General Data
    private String qrValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Before doing anything, check if you can login with the saved info
        String playerID = loadData(getApplicationContext(),"uniqueID");
        if (playerID != ""){
             FirebaseFirestore db = FirebaseFirestore.getInstance();
             CollectionReference accountsRef = db.collection("Accounts");
             DocumentReference myAccount = accountsRef.document(playerID);
             myAccount.get()
                     .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                         @Override
                         public void onSuccess(DocumentSnapshot documentSnapshot) {
                             if (documentSnapshot.exists()) {
                                 Intent intent = new Intent(WelcomeActivity.this, MyQRCodeScreenActivity.class);
                                 startActivity(intent);
                                 Toast.makeText(getApplicationContext(),"Logged in", Toast.LENGTH_LONG).show();
                                 finish();
                             } else {
                                 Toast.makeText(getApplicationContext(),"No Previous Login Info", Toast.LENGTH_LONG).show();
                             }
                         } // end onSuccess
                     }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
                 } // end onFailure
             }); // end myAccount.get().addOnSuccessListener
         }

        createAccount = findViewById(R.id.buttonCreateAccount);
        qrCode = findViewById(R.id.buttonQRCodeLogin);

        // Activated when "Scan with QR Code is completed"
        ActivityResultLauncher<Intent> scannerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent scannerResult = result.getData();
                            qrValue = scannerResult.getStringExtra("qrValue");
                            // For testing the result
                            // Toast.makeText(getApplicationContext(), qrValue, Toast.LENGTH_SHORT).show();
                            // Use the data
                            String[] qrDataArray = qrValue.split(",");
                            if (qrDataArray[0].contentEquals("QRCHASERLOGIN")){
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference accountsRef = db.collection("Accounts");
                                DocumentReference myAccount = accountsRef.document(qrDataArray[1]);
                                myAccount.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    saveData(getApplicationContext(),"uniqueID", qrDataArray[1]);
                                                    Intent intent = new Intent(WelcomeActivity.this, MyQRCodeScreenActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(),"Document does not exist. Please try again", Toast.LENGTH_LONG).show();
                                                }
                                            } // end onSuccess
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Player not found", Toast.LENGTH_LONG).show();
                                    } // end onFailure
                                });
                            } else {
                                Toast.makeText(getApplicationContext(),"Invalid QR Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    } // end onActivityResult
                }
        ); // end ActivityResultLauncher

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Automatically Create a new account here and save it to the database:
                Player newPlayer = new Player();
                newPlayer.saveToDatabase();
                saveData(getApplicationContext(),"uniqueID", newPlayer.getUniqueID());
                Intent intent = new Intent(WelcomeActivity.this, MyQRCodeScreenActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end createAccount.setOnClickListener

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, CameraScannerActivity.class);
                scannerResultLauncher.launch(intent);
            } // end onClick
        }); // end qrCode.setOnClickListener

    } // end onCreate

} // end WelcomeActivity Class
