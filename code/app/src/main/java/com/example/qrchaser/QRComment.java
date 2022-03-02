package com.example.qrchaser;

public class QRComment {
    private String user;
    private String comment;

    public QRComment(String user, String comment) {
        this.user = user;
        this.comment = comment;
    } // end Constructor

    public String getUser() {
        return user;
    } // end getUser

    public String getComment() {
        return comment;
    } // end getComment
} // end Class
