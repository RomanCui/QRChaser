package com.example.qrchaser.oop;

import com.example.qrchaser.oop.QRCode;

import java.util.ArrayList;

/**
 * This abstract class is a base for all players / admin/ guests
 * Not used now, to be developed
 */
public abstract class User {
    private int totalScore;
    private ArrayList<QRCode> allPlayerQRCodes;

    /**
     * This finds the QR code with the lowest score that the user has
     * @return The QRCode with the lowest score
     */
    private QRCode getLowestScoreCode(){
        QRCode lowestQRCode = null;
        // for each loop
        for (QRCode code  : allPlayerQRCodes) {
            if (code.getScore() < lowestQRCode.getScore())
                lowestQRCode =  code;
        }
        return lowestQRCode;
    } // end getlowestScoreCode

    /**
     * This finds the QR code with the highest score that the user has
     * @return The QRCode with the highest score
     */
    private QRCode getHighestScoreCode(){
        QRCode highestQRCode = null;
        // for each loop
        for (QRCode code  : allPlayerQRCodes) {
            if (code.getScore() > highestQRCode.getScore())
                highestQRCode =  code;
        }
        return highestQRCode;
    } // end getHighestScoreCode

    /**
     * This calculates the total score that the player has
     * @return The total score
     */
    private int getTotalScore(){
        int totalScore = 0;
        // for each loop
        for (QRCode code  : allPlayerQRCodes) {
            totalScore += code.getScore();
        }
        return totalScore;
    } // end getTotalScore
} // end User Class
