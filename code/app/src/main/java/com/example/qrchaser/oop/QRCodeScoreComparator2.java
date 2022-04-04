package com.example.qrchaser.oop;

import java.util.Comparator;

/**
 * This class is used for ordering QRCodes based on scores from Low to High
 */
public class QRCodeScoreComparator2 implements Comparator<QRCode> {

    @Override
    public int compare(QRCode o1, QRCode o2) {
        return Integer.compare(o1.getScore(), o2.getScore());
    } // end compare

} // end QRCodeScoreComparator1 Class
