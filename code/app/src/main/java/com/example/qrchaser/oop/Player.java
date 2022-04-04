package com.example.qrchaser.oop;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * This class holds all of the data for a single Player
 */
public class Player {
    private String uniqueID;
    private String nickname;
    private String email;
    private String phoneNumber;
    private boolean admin;
    private int numQR;
    private int totalScore;
    private int highestScore;

    /**
     * This constructor creates a new Player with the default data;
     */
    public Player() {
        this.uniqueID = "" + UUID.randomUUID();
        this.nickname = "Player_" + this.uniqueID.substring(0, 4);
        this.email = "";
        this.phoneNumber = "";
        this.admin = false;
        this.numQR = 0;
        this.totalScore = 0;
        this.highestScore = 0;
    }

    /**
     * This saves the Player object to the database for the first time (Used in CreatePlayer)
     */
    public void saveToDatabase() {
        // create Firestore collection
        final String TAG = "Error";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Accounts");

        collectionReference.document(uniqueID)
                .set(this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG,"Data has been added successfully!");
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG,"Data could not be added!" + e.toString());
                    } // end onSuccess
                });
    } // end saveToDatabase

    /**
     * This updates the database with the data contained in the current player object
     */
    public void updateDatabase() {
        // Create Firestore collection
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Accounts");
        DocumentReference myAccountRef = collectionReference.document(uniqueID);

        myAccountRef
                .update("email", email);
        myAccountRef
                .update("phoneNumber", phoneNumber);
        myAccountRef
                .update("nickname", nickname);
        myAccountRef
                .update("numQR", numQR);
        myAccountRef
                .update("totalScore", totalScore);
        myAccountRef
                .update("highestScore", highestScore);
    } // end updateDatabase

    /**
     * This gets the unique ID of the Player
     * @return uniqueID
     */
    public String getUniqueID() {
        return uniqueID;
    } // end uniqueID

    /**
     * This gets the nickname of the Player
     * @return The nickname of the Player
     */
    public String getNickname() {
        return nickname;
    } // end getNickname

    /**
     * This sets the nickname of the Player
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    } // end setNickname

    /**
     * This gets the email of the Player
     * @return The email of the Player
     */
    public String getEmail() {
        return email;
    } // end getEmail

    /**
     * This sets the email of the Player
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    } // end setEmail

    /**
     * This gets the phone number of the Player
     * @return The phone number of the Player
     */
    public String getPhoneNumber() {
        return phoneNumber;
    } // end getPhoneNumber

    /**
     * This sets the phone number of the Player
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    } // end setPhoneNumber

    /**
     * This gets the admin privilege of the Player
     * @return admin
     */
    public boolean isAdmin() {
        return this.admin;
    } // end isAdmin

    /**
     * This sets the admin privilege of the Player
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    } // end setAdmin

    /**
     * This gets the number of QR Codes that the Player has scanned
     * @return The number of QR Codes
     */
    public int getNumQR() {
        return numQR;
    } // end getNumQR

    /**
     * This sets the number of QR Codes that the Player has scanned
     * @param numQR
     */
    public void setNumQR(int numQR) {
        this.numQR = numQR;
    } // end setNumQR

    /**
     * This gets the total score that the Player has
     * @return The total score
     */
    public int getTotalScore() {
        return totalScore;
    } // end getTotalScore

    /**
     * This sets the total score that the Player has
     * @param totalScore
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    } // end setTotalScore

    /**
     * This gets the highest score that the Player has
     * @return The highest score
     */
    public int getHighestScore() {
        return highestScore;
    } // end getHighestScore

    /**
     * This sets the highest score that the Player has
     * @param highestScore
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    } // end setHighestScore

} // end Player Class
