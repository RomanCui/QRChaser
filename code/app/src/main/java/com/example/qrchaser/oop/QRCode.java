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
 * This class holds all of the data for a single QR Code
 */
public class QRCode implements Comparable<QRCode> {
    private String hash;
    private String name;
    private int score;
    private double latitude;
    private double longitude;
    private ArrayList<Comments> comments = new ArrayList<>();
    private List<String> owners = new ArrayList<>();

    /**
     * A constructor for the QR Code
     * To do:
     * This constructor did not take into the consideration that two Players scan the same QR Code
     * @param qrCodeData
     * @param name
     * @param owner
     * @param comment
     * @param latitude
     * @param longitude
     */
    public QRCode(String qrCodeData, String name, String owner, Comments comment,
                  double latitude, double longitude) {

        this.hash = Hashing.sha256().hashString(qrCodeData, StandardCharsets.UTF_8).toString();
        this.name = name;
        this.owners.add(owner);

        if (comment != null) {
            this.comments.add(comment);
        }

        this.latitude = latitude;
        this.longitude = longitude;

        // Calculate the score from the hash
        List<Integer> asciiList = new ArrayList<>();
        for (int i = 0; i < hash.length(); i++) {
            asciiList.add((int)hash.charAt(i));
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
     * A no-argument constructor for the QR Code, for creating a QR Code from the database query
     */
    public QRCode() {} // end QRCode Constructor

    /**
     * This saves the QR Code object to the database
     */
    public void saveToDatabase() {
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
                        Log.d(TAG,"Data has been added successfully!");
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG,"Data could not be added!" + e.toString());
                    } // end onFailure
                });
    } // end saveToDatabase

    /**
     * This compares 2 QR Codes together so they can be ordered.
     * @param otherQR
     */
    @Override
    public int compareTo(QRCode otherQR) {
        return name.toLowerCase().compareTo(otherQR.getName().toLowerCase());
    } // end compareTo

    /**
     * This gets the hash value of the QRCode
     * @return The hash of the QRCode
     */
    public String getHash() {
        return hash;
    } // end getHash

    /**
     * This sets the hash value of the QRCode
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    } // end setHash

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
     * This gets the score of the QRCode
     * @return The score of the QRCode
     */
    public int getScore() {
        return score;
    } // end getScore

    /**
     * This sets the score of the QRCode
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    } // end setScore

    /**
     * This gets the latitude of the QRCode
     * @return The latitude of the QRCode
     */
    public double getLatitude() {
        return latitude;
    } // end getLatitude

    /**
     * This sets the latitude of the QRCode
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    } // end setLatitude

    /**
     * This gets the longitude of the QRCode
     * @return The longitude of the QRCode
     */
    public double getLongitude() {
        return longitude;
    } // end getLongitude

    /**
     * This sets the longitude of the QRCode
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    } // end setLongitude

    /**
     * This gets all of the Comments that were made on the QRCode
     * @return An ArrayList of all Comments
     */
    public ArrayList<Comments> getComments() {
        return comments;
    } // end getComments

    /**
     * This sets the ArrayList of Comments on the QRCode
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
     * This sets the List of owners of the QRCode
     * @param owners
     */
    public void setOwners(List<String> owners) {
        this.owners = owners;
    } // end setOwners

    /**
     * Add owner to the list of owners
     * @param owner
     */
    public void addOwner(String owner) { this.owners.add(owner); } // end addOwner

    /**
     * This removes and owner from the List of owners on the QRCode
     * @param owner
     * @return Returns true if the owner was deleted
     */
    public Boolean removeOwner(String owner) { return owners.remove(owner); } // end removeOwner

    /**
     * Find if the QRCode contains the owner or not
     * @param owner
     * @return true if the owner was deleted
     */
    public Boolean containOwner(String owner) { return owners.contains(owner); } // end containOwner

    /**
     * This adds a comment to ArrayList of comments on the QRCode
     * @param user
     * @param comment
     */
    public void addComment(String user, String comment) { comments.add(new Comments(user, comment)); } // end addComment

    /**
     * Remove the comment in the comments ArrayList given the index
     * @param index
     */
    public void deleteCommentAt(int index) {
        comments.remove(index);
    } // end deleteComment

}// end QRCode Class
