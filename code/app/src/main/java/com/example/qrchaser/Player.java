package com.example.qrchaser;

public class Player extends User {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;

    public Player(String email, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    } // end Constructor

    // A player doesn't need an actual phone number
    public Player(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = "";
    } // end Constructor

    //Should output a QRCode image only when needed based on the email and password
    private void generateLoginQRCode(){

    } // end generateLoginQRCode

    public String getEmail() {
        return email;
    } // end getEmail

    public void setEmail(String email) {
        this.email = email;
    } // end setEmail

    public String getPassword() {
        return password;
    } // end getPassword

    public void setPassword(String password) {
        this.password = password;
    } // end setPassword

    public String getNickname() {
        return nickname;
    } // end getNickname

    public void setNickname(String nickname) {
        this.nickname = nickname;
    } // end setNickname

    public String getPhoneNumber() {
        return phoneNumber;
    } // end getPhoneNumber

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    } // end setPhoneNumber
} // end Class
