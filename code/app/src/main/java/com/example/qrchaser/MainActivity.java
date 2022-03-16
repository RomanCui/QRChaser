package com.example.qrchaser;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends SaveANDLoad {
    private Button email,qrCode,guest,createAccount;
    private String qrValue;
    private String passwordDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.buttonUsernameLogin);
        createAccount = findViewById(R.id.buttonCreateAccount);
        qrCode = findViewById(R.id.buttonQRCodeLogin);
        guest = findViewById(R.id.buttonGuestLogin);

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
                            Toast.makeText(getApplicationContext(), qrValue, Toast.LENGTH_SHORT).show();
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
                                                    passwordDB = documentSnapshot.getString("Password");
                                                }else {
                                                    Toast.makeText(getApplicationContext(),"Document does not exits",Toast.LENGTH_LONG).show();
                                                }
                                                if (qrDataArray[2].equals(passwordDB)){
                                                    // probably some data to be passed
                                                    saveData(getApplicationContext(), "UserEmail", qrDataArray[1]);
                                                    Intent intent = new Intent(MainActivity.this, MyQRCodeScreenActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    // To show a message if login unsuccessfully
                                                    Toast.makeText(getApplicationContext(),"FAIL: Please try again",Toast.LENGTH_LONG).show();
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
        );

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginEmailActivity.class);
                startActivity(intent);
            } // end onClick
        }); // end email.setOnClickListener

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            } // end onClick
        }); // end createAccount.setOnClickListener

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraScannerActivity.class);
                scannerResultLauncher.launch(intent);
            } // end onClick
        }); // end qrCode.setOnClickListener


        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player player = new Player();
                player.saveToDatabase();
                Intent intent = new Intent(MainActivity.this, MyQRCodeScreenActivity.class);
                startActivity(intent);
            } // end onClick
        }); // end guest.setOnClickListener
    } // end onCreate
} // end MainActivity Class

