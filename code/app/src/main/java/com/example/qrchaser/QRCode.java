package com.example.qrchaser;

import java.util.ArrayList;

public class QRCode {
    private int hash;
    private String name;
    private int score;
    private String id;
    // I am unsure as to how these two will be implmented
    //private location;
    //private image image;
    private ArrayList<QRComment> allComments = new ArrayList<>();


    public QRCode(String qrCodeData, String name) {
        //this.hash = get the hash from the qrCodeData (passed in from the scanner);
        this.name = name;
        //this.score = .... calculated from the hash
        this.id =  name + "(" + hash + ")"; // Used for the Geolocation for sure
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHash() {
        return hash;
    }

    public int getScore() {
        return score;
    }

    public String getId() {
        return id;
    }
}// end Class
