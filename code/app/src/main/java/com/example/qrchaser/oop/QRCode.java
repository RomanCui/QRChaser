package com.example.qrchaser.oop;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class holds all of the data for a single QR code
 */
public class QRCode {

    // use hash as id
    private String hash;
    private String name;
    private int score;
// Photo to be added

    // I am unsure as to how these two will be implemented
    private double latitude;
    private double longitude;
    //private image image;
    private List<String> comments = new ArrayList<>();
    private List<String> owners = new ArrayList<>();


    /**
     * A constructor for the QR code
     * @param qrCodeData
     * @param name
     */
    public QRCode(String qrCodeData, String name, String owner, String comment,
                  double latitude, double longitude) {

        this.hash = Hashing.sha256().hashString(qrCodeData, StandardCharsets.UTF_8).toString();
        this.name = name;
        this.owners.add(owner);

        // only add comments that are not empty
        if (!comments.equals("")){
            this.comments.add(comment);
        }
        this.latitude = latitude;
        this.longitude = longitude;
        // Calculate the score from the hash
        List<Integer> asciiList = new ArrayList<>();
        for (int i = 0; i < hash.length(); i++){
            asciiList.add((int) hash.charAt(i));
        }
        this.score = 0;

        // Iterate through asciiList for score
        for (int i = 0; i < asciiList.size(); i++){
            this.score += asciiList.get(i);
        }

        // Score calculation scheme
        int multiplier = this.score / 1000;
        this.score = this.score % 1000;
        this.score *= multiplier;
    } // end QRCode Constructor

    public void saveToDatabase(){

        final String TAG = "Sample";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference QRCodesReference = db.collection("QRCodes");


        QRCodesReference
                .document(hash)
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

    /**
     * This gets the name of the QRCode
     * @return The name of the QRCode
     */
    public String getName() {
        return name;
    } // end getName

    /**
     * This sets the name of the QRCode
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    } // end setName

    /**
     * This gets the hash of the QRCode
     * @return The hash of the QRCode
     */
    public String getHash() {
        return hash;
    } // end getHash

    /**
     * This gets the score of the QRCode
     * @return The score of the QRCode
     */
    public int getScore() {
        return score;
    } // end getScore

    /**
     * This gets the latitude of the QRCode
     * @return The latitude of the QRCode
     */
    public double getLatitude() {
        return latitude;
    } // end getLatitude

    /**
     * This gets the Longitude of the QRCode
     * @return The Longitude of the QRCode
     */
    public double getLongitude() {
        return longitude;
    } // end getLongitude

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }
}// end QRCode Class