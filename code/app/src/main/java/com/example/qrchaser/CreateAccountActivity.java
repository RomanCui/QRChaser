package com.example.qrchaser;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                String emailAddress = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String nickname = nicknameET.getText().toString();
                String phone = phoneET.getText().toString();

                int formatResult = formatCheck(emailAddress, password, phone);

                if(formatResult == 0){
                    Player player = new Player(emailAddress, password, nickname, phone);
                    player.saveToDatabase();
                    finish();
                }
                else if(formatResult == 1){
                    Toast.makeText(getApplicationContext(),
                            "FAIL: Please check your email format",
                            Toast.LENGTH_LONG).show();
                }
                else if(formatResult == 2){
                    Toast.makeText(getApplicationContext(),
                            "FAIL: Please check your password format",
                            Toast.LENGTH_LONG).show();
                }
                else if(formatResult == 3){
                    Toast.makeText(getApplicationContext(),
                            "FAIL: Please check your phone number format",
                            Toast.LENGTH_LONG).show();
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
