package com.example.qrchaser.oop;

import java.util.Comparator;

public class PlayerNumQRComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getNumQR(), o1.getNumQR());
    } // end compare

} // end PlayerNumQRComparator Class
