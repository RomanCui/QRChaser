package com.example.qrchaser.oop;

import java.util.Comparator;

public class PlayerTotalScoreComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getTotalScore(), o1.getTotalScore());
    } // end compare

} // end PlayerTotalScoreComparator Class
