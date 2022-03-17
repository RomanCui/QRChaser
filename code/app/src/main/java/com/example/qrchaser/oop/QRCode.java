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
    private ArrayList<String> comments = new ArrayList<>();
    private ArrayList<String> owners = new ArrayList<>();


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

        /*
        db = FirebaseFirestore.getInstance();
        CollectionReference codesRef = db.collection("QRCodes");

        HashMap<String, String> qrCodes = new HashMap<>();
        // If there’s some data in the EditText field, then we create a new key-value pair.
        qrCodes.put("HashValue", scannedQR.getHash());
        qrCodes.put("Name", scannedQR.getName());
        qrCodes.put("Lat", Double.toString(scannedQR.getLatitude()));
        qrCodes.put("Lon", Double.toString(scannedQR.getLongitude()));
        qrCodes.put("Lon", Double.toString(scannedQR.getLongitude()));
        qrCodes.put("Score", Double.toString(scannedQR.getScore()));

        QRCodesReference
                .document(scannedQR.getId())
                .set(qrCodes)
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

        QRCodesReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
            }
        });

         */
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
}// end QRCode Class
