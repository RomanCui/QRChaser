package com.example.qrchaser.oop;

import java.util.Comparator;

/**
 * This class is used for ordering QRCodes based on scores from high to low
 */
public class QRCodeScoreComparator1 implements Comparator<QRCode> {

    @Override
    public int compare(QRCode o1, QRCode o2) {
        return Integer.compare(o2.getScore(), o1.getScore());
    } // end compare
} // end QRCodeScoreComparator1 Class
