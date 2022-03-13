package com.example.qrchaser;

/**
 * This class holds all of the data for a comment on a QR Code
 */
public class QRComment {
    private String user;
    private String comment;

    /**
     * The constructor for the QR code
     * @param user
     * @param comment
     */
    public QRComment(String user, String comment) {
        this.user = user;
        this.comment = comment;
    } // end QRComment Constructor

    /**
     * This gets the user who made the comment
     * @return The user who made the comment
     */
    public String getUser() {
        return user;
    } // end getUser

    /**
     * This gets the comment itself
     * @return The comment itself
     */
    public String getComment() {
        return comment;
    } // end getComment
} // end QRComment Class
