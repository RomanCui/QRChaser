package com.example.qrchaser.oop;

/**
 * This class holds all of the data for a Comment on a QR code
 */
public class Comments {
    private String username;
    private String comment;

    /**
     * This constructor creates a new comment with the passed in data;
     */
    public Comments(String username, String comment) {
        this.username = username;
        this.comment = comment;
    } // end Comments Constructor

    /**
     * This gets the username of the player who made the comment
     * @return the username of the player who made the comment
     */
    public String getUsername() {
        return username;
    } // end getUsername

    /**
     * This gets the comment that was made
     * @return the comment
     */
    public String getComment() {
        return comment;
    } // end getComment
} // end Comments Class
