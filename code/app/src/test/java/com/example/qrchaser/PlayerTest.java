package com.example.qrchaser;

import org.junit.Test;
import static org.junit.Assert.*;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrchaser.general.SaveANDLoad;
import com.example.qrchaser.oop.Player;
import com.example.qrchaser.oop.PlayerNumQRComparator;
import com.example.qrchaser.oop.PlayerSingleScoreComparator;
import com.example.qrchaser.oop.PlayerTotalScoreComparator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.Collections;

public class PlayerTest {

    private Player mockPlayer1(){
        Player player1 = new Player();
        return player1;
    }

    private Player mockPlayer2(){
        Player player2 = new Player();
        return player2;
    }
    private Player mockPlayer3(){
        Player player3 = new Player();
        return player3;
    }

    @Test
    public void setEmailTest(){
        Player player1 = mockPlayer1();

        player1.setEmail("Bob@gmail.com");
        assertEquals(player1.getEmail(),"Bob@gmail.com");
    }

    @Test
    public void getEmailTest(){
        Player player1 = mockPlayer1();
        player1.setEmail("Bob@gmail.com");
        Player player2 = mockPlayer2();
        player2.setEmail("Bob@gmail.com");
        Player player3 = mockPlayer3();
        player3.setEmail("Alice@gmail.com");

        assertEquals(player1.getEmail(),player2.getEmail());
        assertNotEquals(player1.getEmail(),player3.getEmail());
    }

    @Test
    public void setNicknameTest(){
        Player player1 = mockPlayer1();

        player1.setNickname("Bob");
        assertEquals(player1.getNickname(),"Bob");
    }

    @Test
    public void getNicknameTest(){
        Player player1 = mockPlayer1();
        player1.setNickname("Bob");
        Player player2 = mockPlayer2();
        player2.setNickname("Bob");
        Player player3 = mockPlayer3();
        player3.setNickname("Alice");

        assertEquals(player1.getNickname(),player2.getNickname());
        assertNotEquals(player1.getNickname(),player3.getNickname());
    }

    @Test
    public void setPhoneNumberTest(){
        Player player1 = mockPlayer1();

        player1.setPhoneNumber("1234567890");
        assertEquals(player1.getPhoneNumber(),"1234567890");
    }

    @Test
    public void getPhoneNumberTest(){
        Player player1 = mockPlayer1();
        player1.setPhoneNumber("1234567890");
        Player player2 = mockPlayer2();
        player2.setPhoneNumber("1234567890");
        Player player3 = mockPlayer3();
        player3.setPhoneNumber("1234567809");

        assertEquals(player1.getPhoneNumber(),player2.getPhoneNumber());
        assertNotEquals(player1.getPhoneNumber(),player3.getPhoneNumber());
    }

    @Test
    public void getUniqueIDTest(){
        Player player1 = mockPlayer1();
        Player player2 = mockPlayer2();

        assertNotEquals(player1.getUniqueID(),player2.getUniqueID());
    }

    @Test
    public void isAdminTest(){
        Player player1 = mockPlayer1();

        assertEquals(player1.isAdmin(),false);
    }

    @Test
    public void setAdminTest(){
        Player player1 = mockPlayer1();
        player1.setAdmin(true);

        assertEquals(player1.isAdmin(),true);
    }

    @Test
    public void setNumQRAndGetNumQRTest(){
        Player player1 = mockPlayer1();
        Player player2 = mockPlayer2();

        player1.setNumQR(10);
        player2.setNumQR(20);

        assertEquals(player1.getNumQR(),10);
        assertNotEquals(player1.getNumQR(),player2.getNumQR());

    }

    @Test
    public void setTotalScoreAndGetTotalScoreTest(){
        Player player1 = mockPlayer1();
        Player player2 = mockPlayer2();

        player1.setTotalScore(2000);
        player2.setTotalScore(1000);

        assertEquals(player1.getTotalScore(),2000);
        assertNotEquals(player1.getTotalScore(),player2.getTotalScore());

    }

    @Test
    public void setHighestScoreAndGetHighestScoreTest(){
        Player player1 = mockPlayer1();
        Player player2 = mockPlayer2();

        player1.setHighestScore(100);
        player2.setHighestScore(200);

        assertEquals(player1.getHighestScore(),100);
        assertNotEquals(player1.getHighestScore(),player2.getHighestScore());

    }

    @Test
    public void setLowestScoreAndGetLowestScoreTest(){
        Player player1 = mockPlayer1();
        Player player2 = mockPlayer2();

        player1.setLowestScore(10);
        player2.setLowestScore(15);

        assertEquals(player1.getLowestScore(),10);
        assertNotEquals(player1.getLowestScore(),player2.getLowestScore());

    }


} // end PlayerTest
