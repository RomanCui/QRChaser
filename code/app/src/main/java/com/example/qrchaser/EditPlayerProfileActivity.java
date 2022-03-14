package com.example.qrchaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPlayerProfileActivity extends SaveANDLoad {
    private EditText emailET, passwordET, nicknameET, phoneNumberET;
    private Button buttonConfirm, buttonSignOut, buttonGenerateLoginQRCode, buttonGenerateInfoQRCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player_profile);

        // Get the player email in order to load the data from the database
        String playerEmail = loadData(getApplicationContext(), "UserEmail");
        // Get Player info from the database here

        // Using a dummy player for now
        // TODO: 2022-03-12 Pass In Actual Players
        Player currentPlayer = new Player(playerEmail, "TestPassword", "TestPlayer" );

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