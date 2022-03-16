package com.example.qrchaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

public class EditPlayerProfileActivity extends SaveANDLoad {
    private EditText emailET, passwordET, nicknameET, phoneNumberET;
    private Button buttonConfirm, buttonSignOut, buttonGenerateLoginQRCode, buttonGenerateInfoQRCode;

    final String TAG = "Sample";
    FirebaseFirestore db;
    String passwordDB;
    String nicknameDB;
    String phoneDB;
    Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player_profile);

        emailET = findViewById(R.id.editTextEmailAddress);
        passwordET = findViewById(R.id.editTextPassword);
        nicknameET = findViewById(R.id.editTextNickname);
        phoneNumberET = findViewById(R.id.editTextPhone);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonGenerateLoginQRCode = findViewById(R.id.ButtonGenerateLoginQRCode);
        buttonGenerateInfoQRCode = findViewById(R.id.ButtonGenerateInfoQRCode);

        // Get the player email in order to load the data from the database
        String playerEmail = loadData(getApplicationContext(), "UserEmail");

        // Get Player info from the database here

        db = FirebaseFirestore.getInstance();
        CollectionReference accountsRef = db.collection("Accounts");
        DocumentReference myAccount = accountsRef.document(playerEmail);

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;

        // Get the document, forcing the SDK to use the offline cache
        myAccount.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    passwordDB = document.getString("Password");
                    nicknameDB = document.getString("Nickname");
                    phoneDB = document.getString("Phone");

                    // Using a dummy player for now
                    // TODO: 2022-03-12 Pass In Actual Players

                    // Using a dummy player for now
                    // TODO: 2022-03-12 Pass In Actual Players
                    Player currentPlayer = new Player(playerEmail, passwordDB,
                            nicknameDB, phoneDB );

                    // Initialize
                    emailET.setText(currentPlayer.getEmail());
                    passwordET.setText(currentPlayer.getPassword());
                    nicknameET.setText(currentPlayer.getNickname());
                    phoneNumberET.setText(currentPlayer.getPhoneNumber());


                } else {
                    Toast.makeText(getApplicationContext(),"Load Failed",Toast.LENGTH_LONG).show();
                }
            }
        });




        //Confirm all changes made (Push the changes to the database)
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailET.getText().toString())) {
                    currentPlayer.setEmail(emailET.getText().toString());
                }
                if(!TextUtils.isEmpty(passwordET.getText().toString())) {
                    currentPlayer.setPassword(passwordET.getText().toString());
                }
                if(!TextUtils.isEmpty(nicknameET.getText().toString())) {
                    currentPlayer.setNickname(nicknameET.getText().toString());
                }
                if(!TextUtils.isEmpty(phoneNumberET.getText().toString())) {
                    currentPlayer.setPhoneNumber(phoneNumberET.getText().toString());
                }
            } // end onClick
        });// end buttonConfirm.setOnClickListener

        // Sign Out
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2022-03-12 Implement
            } // end onClick
        });// end buttonSignOut.setOnClickListener

        // Generate Login QRCode
        buttonGenerateLoginQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPlayerProfileActivity.this, GeneratedQRCodeActivity.class);
                intent.putExtra("qrData", "QRCHASERLOGIN," + currentPlayer.getEmail() + "," + currentPlayer.getPassword());
                startActivity(intent);
            } // end onClick
        });// end buttonGenerateLoginQRCode.setOnClickListener

        // Generate Info QRCode
        buttonGenerateInfoQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPlayerProfileActivity.this, GeneratedQRCodeActivity.class);
                intent.putExtra("qrData", "QRCHASERINFO," + currentPlayer.getEmail());
                startActivity(intent);
            } // end onClick
        });// end buttonGenerateInfoQRCode.setOnClickListener

    } // end onCreate
} // end PlayerProfileInfoActivity Class