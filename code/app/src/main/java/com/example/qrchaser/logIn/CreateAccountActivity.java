package com.example.qrchaser.logIn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;


// This activity allows user to create account with email address
// password, nickname and phone number
// After creating account, it takes the user back to the welcome screen to log in
// Most features complete
// Input format check to do
// Repeating email address check to do
public class CreateAccountActivity extends SaveANDLoad {

    EditText emailET;
    EditText passwordET;
    EditText nicknameET;
    EditText phoneET;
    Button confirmBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailET = findViewById(R.id.editTextEmailAddress1);
        passwordET = findViewById(R.id.editTextPassword1);
        nicknameET = findViewById(R.id.editTextNickname1);
        phoneET = findViewById(R.id.editTextPhone1);
        confirmBtn = findViewById(R.id.buttonConfirm1);
        cancelBtn = findViewById(R.id.buttonCancel1);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get user input from Edit Text
                String emailAddress = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String nickname = nicknameET.getText().toString();
                String phone = phoneET.getText().toString();

                int formatResult = formatCheck(emailAddress, password, phone);

                // If input format passed, the account is saved to database
                if(formatResult == 0){
                    Player player = new Player(emailAddress, password, nickname, phone);
                    player.saveToDatabase();
                    finish();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public int formatCheck(String email, String password, String phone){
        return 0;
    }
}
