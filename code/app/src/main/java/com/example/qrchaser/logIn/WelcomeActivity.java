package com.example.qrchaser.logIn;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.qrchaser.player.CameraScannerActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Player;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// This activity allows user to go to login with a qr code or with a new account
public class WelcomeActivity extends SaveANDLoad {
    private Button qrCode, createAccount;
    private String qrValue;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Before doing anything check if you can login with the saved info
        String playerID = loadData(getApplicationContext(), "uniqueID");
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
                             } else {
                                 Toast.makeText(getApplicationContext(),"No Previous Login Info", Toast.LENGTH_LONG).show();
                             }
                         } // end onSuccess
                     }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                 } // end onFailure
             }); // end myAccount.get().addOnSuccessListener
         }

        createAccount = findViewById(R.id.buttonCreateAccount);
        qrCode = findViewById(R.id.buttonQRCodeLogin);

        // Activated when "Scan with QR code is completed"
        ActivityResultLauncher<Intent> scannerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent scannerResult = result.getData();
                            qrValue = scannerResult.getStringExtra("qrValue");
                            //for testing the result
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
                                                    saveData(getApplicationContext(), "uniqueID", qrDataArray[1]);
                                                    Intent intent = new Intent(WelcomeActivity.this, MyQRCodeScreenActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(),"Document does not exist. Please try again",Toast.LENGTH_LONG).show();
                                                }
                                            } // end onSuccess
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                    } // end onFailure
                                }); // end myAccount.get().addOnSuccessListener
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
                saveData(getApplicationContext(), "uniqueID", newPlayer.getNickname());
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
} // end MainActivity Class

