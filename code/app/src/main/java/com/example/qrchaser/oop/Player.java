package com.example.qrchaser.oop;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class holds all of the data for a single player
 */
public class Player extends User {
    private String uniqueID;
    private String email;
    private String nickname;
    private String phoneNumber;
    private boolean admin;

    // For creating a player from the database
    public Player(String email, String nickname, String phoneNumber, boolean admin, String uniqueID) {
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
        this.uniqueID = uniqueID;
    }

    // For new player (no details yet)
    public Player(){
        // todo repeating id to be checked
        double randomNumber = Math.random()*100000;
        this.uniqueID = "newSystem" + Integer.toString((int)randomNumber);
        this.email = "";
        this.nickname = "User:" + this.uniqueID;
        this.phoneNumber = "";
        this.admin = false;
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

        collectionReference.document(uniqueID)
                .set(this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    } // end onSuccess
                });
    } // end  saveToDatabase

    public void updateDatabase(){
        // Create Firestore collection
        final String TAG = "Sample";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Accounts");
        DocumentReference myAccountRef = collectionReference.document(uniqueID);

        myAccountRef
                .update("phoneNumber", phoneNumber)
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

        myAccountRef
                .update("email", email)
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

        myAccountRef
                .update("nickname", nickname)
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

        myAccountRef
                .update("admin", admin)
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
    } // end updateDatabase


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

    /**
     * This gets the admin privilege of the player
     * @return admin
     */
    public boolean isAdmin() {
        return this.admin;
    } // end isAdmin

    /**
     * This sets the admin privilege of the player
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    } // end setAdmin

    /**
     * This gets the unique ID of the player
     * @return uniqueID
     */
    public String getUniqueID() {
        return uniqueID;
    } // end uniqueID
} // end Player Class
