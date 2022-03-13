package com.example.qrchaser;

import java.util.ArrayList;

public abstract class User {
    private int totalScore;
    private ArrayList<QRCode> allPlayerQRCodes;

    private QRCode getLowestScoreCode(){
        QRCode lowestQRCode = null;
        // for each loop
        for (QRCode code  : allPlayerQRCodes) {
            if (code.getScore() < lowestQRCode.getScore())
                lowestQRCode =  code;
        }
        return lowestQRCode;
    } // end getlowestScoreCode

    private QRCode getHighestScoreCode(){
        QRCode highestQRCode = null;
        // for each loop
        for (QRCode code  : allPlayerQRCodes) {
            if (code.getScore() > highestQRCode.getScore())
                highestQRCode =  code;
        }
        return highestQRCode;
    } // end getHighestScoreCode

    private int getTotalScore(){
        int totalScore = 0;
        // for each loop
        for (QRCode code  : allPlayerQRCodes) {
            totalScore += code.getScore();
        }
        return totalScore;
    } // end getTotalScore
} // end Class
