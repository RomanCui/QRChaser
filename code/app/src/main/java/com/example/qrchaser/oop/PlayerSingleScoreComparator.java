package com.example.qrchaser.oop;

import java.util.Comparator;

public class PlayerSingleScoreComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getHighestScore(), o1.getHighestScore());
    }
}
