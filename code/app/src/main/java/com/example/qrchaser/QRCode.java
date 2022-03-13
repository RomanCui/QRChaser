package com.example.qrchaser;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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


    public QRCode(String qrCodeData, String name) {
        //this.hash = get the hash from the qrCodeData (passed in from the scanner);
        this.hash = Hashing.sha256().hashString(qrCodeData, StandardCharsets.UTF_8).toString();
        this.name = name;

        //this.score = .... calculated from the hash
        for (int i = 0; i < hash.length(); i++){
            asciiList.add(i);
        }

        this.score = 0;
        for (int i = 0; i < asciiList.toArray().length; i++){
            score += asciiList.get(i);
        }

        this.id =  name + "(" + hash + ")"; // Used for the Geolocation for sure
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public int getScore() {
        return score;
    }

    public String getId() {
        return id;
    }
}// end Class
