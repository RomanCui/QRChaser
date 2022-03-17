package com.example.qrchaser.oop;


import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;


/**
 * This class holds all of the data for a single player
 */
public class Player extends User {

    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String guest;

    /**
     * A constructor for the Player
     * @param email
     * @param password
     * @param nickname
     * @param phoneNumber
     */
    public Player(String email, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.guest = "0";
    } // end Player Constructor

    // For guest
    public Player(){
        // guest email to be changed
        this.email = "";
        this.password = "";
        this.nickname = "";
        this.phoneNumber = "";
        this.guest = "1";
    }

    // This method saves a player to the database
    // used in create account
    public void saveToDatabase(){

        // create Firestore collection
        final String TAG = "Sample";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Accounts");

        collectionReference.document(email)
                .set(this)
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

    }

    // This method saves a player to the database
    // used in create account
    public void updateDatabase(){

        // create Firestore collection
        final String TAG = "Sample";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Accounts");
        DocumentReference myAccountRef = collectionReference.document(email);

        myAccountRef
                .update("Phone", phoneNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


    /**
     * This gets the email of the player
     * @return The email of the QRCode
     */
    public String getEmail() {
        return email;
    } // end getEmail

    /**
     * This sets the email of the player
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    } // end setEmail

    /**
     * This gets the password of the player
     * @return The password of the QRCode
     */
    public String getPassword() {
        return password;
    } // end getPassword

    /**
     * This sets the password of the player
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    } // end setPassword

    /**
     * This gets the nickname of the player
     * @return The nickname of the QRCode
     */
    public String getNickname() {
        return nickname;
    } // end getNickname

    /**
     * This sets the nickname of the player
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    } // end setNickname

    /**
     * This gets the phone number of the player
     * @return The phone number of the QRCode
     */
    public String getPhoneNumber() {
        return phoneNumber;
    } // end getPhoneNumber

    /**
     * This sets the phone number of the player
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    } // end setPhoneNumber

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

} // end Player Class
