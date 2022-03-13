package com.example.qrchaser;

public class Admin extends Player{

    // An Admin must have a phone number for emergencies
    public Admin(String email, String password, String nickname, String phoneNumber) {
        super(email, password, nickname, phoneNumber);
    } // end Admin Constructor

   //There is not very many differences for the admin class, how do we want to implement this?

} // end Admin Class
