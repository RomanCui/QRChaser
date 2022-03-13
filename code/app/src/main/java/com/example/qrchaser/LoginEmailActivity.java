package com.example.qrchaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginEmailActivity extends AppCompatActivity {
    Button login;
    String email, passWord;
    EditText emailET, passWordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        emailET = findViewById(R.id.editTextEmailAddress);
        passWordET = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                passWord = passWordET.getText().toString();
                if (accountVerify(email, passWord)){
                    // probably some data to be passed
                    Intent intent = new Intent(LoginEmailActivity.this, MyQRCodeScreenActivity.class);
                    startActivity(intent);
                } else {
                    // To show a message if login unsuccessfully
                    Toast.makeText(getApplicationContext(),"FAIL: Please check your email or password",Toast.LENGTH_LONG).show();
                }
            }  // end onClick
        }); // end login.setOnClickListener
    } // end onCreate

    public boolean accountVerify(String email, String passWord){
        if(email.equals("yes") && passWord.equals("123")){
        // to be connected with database
            return true;
        }else
            return false;
    } // end accountVerify
} // end LoginEmailActivity Class