package com.example.qrchaser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    final String TAG = "Sample";
    FirebaseFirestore db;

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

        emailET = findViewById(R.id.editTextEmailAddress);
        passwordET = findViewById(R.id.editTextPassword);
        nicknameET = findViewById(R.id.editTextNickname);
        phoneET = findViewById(R.id.editTextPhone);
        confirmBtn = findViewById(R.id.buttonConfirm);
        cancelBtn = findViewById(R.id.buttonCancel);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String nickname = nicknameET.getText().toString();
                String phone = phoneET.getText().toString();

                int formatResult = formatCheck(emailAddress, password, phone);

                if( formatResult == 0){
                    // Access a Cloud Firestore instance from your Activity
                    db = FirebaseFirestore.getInstance();

                    // Get a top level reference to the collection
                    final CollectionReference collectionReference =
                            db.collection("Accounts");

                    HashMap<String, String> accounts = new HashMap<>();
                    if (emailAddress.length()>0 && password.length()>0 &&
                            nickname.length()>0 && phone.length()>0) {
                        // If there’s some data in the EditText field, then we create a new key-value pair.
                        accounts.put("EmailAddress", emailAddress);
                        accounts.put("Password", password);
                        accounts.put("Nickname", nickname);
                        accounts.put("Phone", phone);
                    }

                    collectionReference
                            .document(emailAddress)
                            .set(accounts)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });
                    emailET.setText("");
                    passwordET.setText("");
                    nicknameET.setText("");
                    phoneET.setText("");

                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {
                        }
                    });
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
