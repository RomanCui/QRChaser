package com.example.qrchaser;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.qrchaser.oop.Player;


public class PlayerTest {

    private Player mockPlayer1(){
        Player player1 = new Player("Roy@gmail.com","1234","Roy","1234");
        return player1;
    }

    private Player mockPlayer2(){
        Player player2 = new Player("Roy@gmail.com","1234","Roy","1234");
        return player2;
    }
    private Player mockPlayer3(){
        Player player3 = new Player("Lesley@gmail.com","123456","Lesley","1234567");
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
        Player player2 = mockPlayer2();
        Player player3 = mockPlayer3();

        assertEquals(player1.getEmail(),player2.getEmail());
        assertNotEquals(player1.getEmail(),player3.getEmail());

    }

    @Test
    public void setPasswordTest(){
        Player player1 = mockPlayer1();

        player1.setPassword("1234567");
        assertEquals(player1.getPassword(),"1234567");

    }

    @Test
    public void getPasswordTest(){
        Player player1 = mockPlayer1();
        Player player2 = mockPlayer2();
        Player player3 = mockPlayer3();

        assertEquals(player1.getPassword(),player2.getPassword());
        assertNotEquals(player1.getPassword(),player3.getPassword());

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
        Player player2 = mockPlayer2();
        Player player3 = mockPlayer3();

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
        Player player2 = mockPlayer2();
        Player player3 = mockPlayer3();

        assertEquals(player1.getPhoneNumber(),player2.getPhoneNumber());
        assertNotEquals(player1.getPhoneNumber(),player3.getPhoneNumber());

    }



}
