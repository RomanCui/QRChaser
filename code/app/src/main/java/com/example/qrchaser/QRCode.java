package com.example.qrchaser;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all of the data for a single QR code
 */
public class QRCode {
    private String hash;
    private String name;
    private int score;
    private String id;
    // I am unsure as to how these two will be implemented
    //private location;
    //private image image;
    private ArrayList<QRComment> allComments = new ArrayList<>();
    private List<Integer> asciiList = new ArrayList<>();

    /**
     * The constructor for the QR code
     * @param qrCodeData
     * @param name
     */
    public QRCode(String qrCodeData, String name) {
        this.hash = Hashing.sha256().hashString(qrCodeData, StandardCharsets.UTF_8).toString();
        this.name = name;

        // Calculate the score from the hash
        for (int i = 0; i < hash.length(); i++){
            asciiList.add((int) hash.charAt(i));
        }
        this.score = 0;

        // Iterate through asciiList for score
        for (int i = 0; i < asciiList.size(); i++){
            this.score += asciiList.get(i);
        }

        // Score calculation scheme
        int multiplier = score / 1000;
        score = score % 1000;
        score *= multiplier;

        this.id =  name + "(" + hash + ")"; // Used for the Geolocation for sure
    } // end QRCode Constructor

    /**
     * This gets the name of the QRCode
     * @return The name of the QRCode
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the name of the QRCode
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This gets the hash of the QRCode
     * @return The hash of the QRCode
     */
    public String getHash() {
        return hash;
    }

    /**
     * This gets the score of the QRCode
     * @return The score of the QRCode
     */
    public int getScore() {
        return score;
    }

    /**
     * This gets the ID of the QRCode
     * @return The ID of the QRCode
     */
    public String getId() {
        return id;
    }
}// end QRCode Class
