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

    // Unsure as to how the rest of the implementation will go
    // Due to all of the requirements for this class, we should implement a
    // second class for the player login qr code, if one is even needed (Could just make it on the fly)

    public QRCode(int hash, String name) {
        this.hash = hash;
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
