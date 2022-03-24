package com.example.qrchaser.oop;

public class Comments {

    private String username;
    private String comment;

    public Comments(String username, String comment) {

        this.username = username;
        this.comment = comment;
    }

    //no argument constructor for creating object from database query
    public Comments() {

    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }
}
