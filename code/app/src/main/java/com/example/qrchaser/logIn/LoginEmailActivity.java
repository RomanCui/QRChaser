package com.example.qrchaser.logIn;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.R;
import com.example.qrchaser.general.SaveANDLoad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginEmailActivity extends SaveANDLoad {

    final String TAG = "Sample";
    FirebaseFirestore db;

    Button login;
    String email, passWord;
    EditText emailET, passWordET;
    String passwordDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        emailET = findViewById(R.id.editTextEmailAddress2);
        passWordET = findViewById(R.id.editTextPassword2);
        login = findViewById(R.id.buttonLogin2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailET.getText().toString();
                passWord = passWordET.getText().toString();


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
                                    saveData(getApplicationContext(), "UserEmail", email);
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


            }  // end onClick
        }); // end login.setOnClickListener
    } // end onCreate

} // end LoginEmailActivity Class