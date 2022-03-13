package com.example.qrchaser;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Button email,qrCode,guest,createAccount;
    private String qrValue;

    Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.buttonUsernameLogin);
        createAccount = findViewById(R.id.buttonCreateAccount);
        qrCode = findViewById(R.id.buttonQRCodeLogin);
        guest = findViewById(R.id.buttonGuestLogin);

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
                                // TODO: 2022-03-13 Attempt to login with Username: qrDataArray[1] and Password qrDataArray[1]
                                Intent intent = new Intent(MainActivity.this, MyQRCodeScreenActivity.class);
                                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, MyQRCodeScreenActivity.class);
                startActivity(intent);
            } // end onClick
        }); // end guest.setOnClickListener
    } // end onCreate
} // end MainActivity Class

