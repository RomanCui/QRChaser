package com.example.qrchaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPlayerProfileActivity extends AppCompatActivity {
    private EditText emailET, passwordET, nicknameET, phoneNumberET;
    private Button buttonConfirm, buttonSignOut, buttonGenerateLoginQRCode, buttonGenerateInfoQRCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Using a dummy player for now
        // TODO: 2022-03-12 Pass In Actual Players
        Player currentPlayer = new Player("TestPlayer@gmail.com", "TestPassword", "TestPlayer" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player_profile);

        // **************************** Get Player From DB*****************************************
        db = FirebaseFirestore.getInstance();

        CollectionReference accountsRef = db.collection("Accounts");
        DocumentReference myAccount = accountsRef.document(email);
        myAccount.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            passwordDB = documentSnapshot.getString("Password");
                        }else {
                            Toast.makeText(getApplicationContext(),"Document does not exits",Toast.LENGTH_LONG).show();
                        }
                        if (passWord.equals(passwordDB)){
                            // probably some data to be passed
                            Intent intent = new Intent(LoginEmailActivity.this, MyQRCodeScreenActivity.class);
                            startActivity(intent);
                        } else {
                            // To show a message if login unsuccessfully
                            Toast.makeText(getApplicationContext(),"FAIL: Please check your email or password",Toast.LENGTH_LONG).show();
                        }
                    } // end onSuccess
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            } // end onFailure
        }); // end myAccount.get().addOnSuccessListener

        // **************************** End Get Player From DB*****************************************

        // Initialize
        emailET = findViewById(R.id.editTextEmailAddress);
        emailET.setText(currentPlayer.getEmail());
        passwordET = findViewById(R.id.editTextPassword);
        passwordET.setText(currentPlayer.getPassword());
        nicknameET = findViewById(R.id.editTextNickname);
        nicknameET.setText(currentPlayer.getNickname());
        phoneNumberET = findViewById(R.id.editTextPhone);
        phoneNumberET.setText(currentPlayer.getPhoneNumber());
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonGenerateLoginQRCode = findViewById(R.id.ButtonGenerateLoginQRCode);
        buttonGenerateInfoQRCode = findViewById(R.id.ButtonGenerateInfoQRCode);

        //Confirm all changes made
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