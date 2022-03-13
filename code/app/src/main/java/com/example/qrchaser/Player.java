package com.example.qrchaser;

/**
 * This class holds all of the data for a single player
 */
public class Player extends User {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;

    /**
     * A constructor for the QR code
     * @param email
     * @param password
     * @param nickname
     * @param phoneNumber
     */
    public Player(String email, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    } // end Player Constructor

    /**
     * A constructor for the QR code (No phone number)
     * @param email
     * @param password
     * @param nickname
     */
    public Player(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = "";
    } // end Player Constructor

    /**
     * This gets the email of the player
     * @return The email of the QRCode
     */
    public String getEmail() {
        return email;
    } // end getEmail

    /**
     * This sets the email of the player
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    } // end setEmail

    /**
     * This gets the password of the player
     * @return The password of the QRCode
     */
    public String getPassword() {
        return password;
    } // end getPassword

    /**
     * This sets the password of the player
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    } // end setPassword

    /**
     * This gets the nickname of the player
     * @return The nickname of the QRCode
     */
    public String getNickname() {
        return nickname;
    } // end getNickname

    /**
     * This sets the nickname of the player
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    } // end setNickname

    /**
     * This gets the phone number of the player
     * @return The phone number of the QRCode
     */
    public String getPhoneNumber() {
        return phoneNumber;
    } // end getPhoneNumber

    /**
     * This sets the phone number of the player
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    } // end setPhoneNumber
} // end Player Class
