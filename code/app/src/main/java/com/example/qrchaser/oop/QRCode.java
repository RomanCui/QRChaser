package com.example.qrchaser.oop;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all of the data for a single QR code
 */
public class QRCode implements Comparable<QRCode>{
    private String hash;
    private String name;
    private int score;
    private double latitude;
    private double longitude;
    private ArrayList<Comments> comments = new ArrayList<>();
    private List<String> owners = new ArrayList<>();
    // Photo to be added?


    /**
     * A constructor for the QR code
     * To do:
     * This constructor did not take into the consideration that two players scan the same QR code
     * @param qrCodeData
     * @param name
     */
    public QRCode(String qrCodeData, String name, String owner, Comments comment,
                  double latitude, double longitude) {

        this.hash = Hashing.sha256().hashString(qrCodeData, StandardCharsets.UTF_8).toString();
        this.name = name;
        this.owners.add(owner);

        if(comment != null) {
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


    /**
     * A no-argument constructor for the QR code, for creating QRCode from database query
     */
    public QRCode() {} // end QRCode Constructor

    /**
     * This saves the QR code object to the database
     */
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
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    } // end onFailure
                });
    } // end saveToDatabase

    /**
     * This compares 2 QR codes together so they can be ordered.
     * @param otherQR
     */
    @Override
    public int compareTo(QRCode otherQR) {
        return name.toLowerCase().compareTo(otherQR.getName().toLowerCase());
    } // end compareTo

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

    /**
     * This sets the hash value of the QRCode
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    } // end setHash

    /**
     * This gets the score of the QRCode
     * @return The score of the QRCode
     */
    public void setScore(int score) {
        this.score = score;
    } // end setScore

    /**
     * This sets the latitude of the QRCode
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    } // end setLatitude

    /**
     * This sets the longitude of the QRCode
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    } // end setLongitude

    /**
     * This gets all of the comments that were made on the QRCode
     * @return An arrayList of all of the comments
     */
    public ArrayList<Comments> getComments() {
        return comments;
    } // end getComments

    /**
     * This sets the ArrayList of comments on the QRCode
     * @param comments
     */
    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    } // end setComments

    /**
     * This gets the all of the owners of the QRCode
     * @return An ArrayList of all of the owners
     */
    public List<String> getOwners() {
        return owners;
    } // end getOwners

    /**
     * This sets the List of owners on the QRCode
     * @param owners
     */
    public void setOwners(List<String> owners) {
        this.owners = owners;
    } // end setOwners

    /**
     * This removes and owner from the List of owners on the QRCode
     * @param owner
     */
    public Boolean removeOwner(String owner) { return owners.remove(owner); } // end removeOwner

    /**
     * This adds a comment to ArrayList of comments on the QRCode
     * @param comment
     */
    public void addComment(String user, String comment) { comments.add(new Comments(user, comment)); } // end addComment
}// end QRCode Class
