package com.example.qrchaser;

import static org.junit.Assert.assertEquals;

import com.example.qrchaser.oop.Comments;

import org.junit.Test;

public class CommentsTest {

    private Comments mockComments(){
        Comments comments = new Comments("Bob","Bob's QR");
        return comments;
    }

    @Test
    public void getUserNameTest(){
        Comments comments = mockComments();
        assertEquals(comments.getUsername(),"Bob");
    }

    @Test
    public void getCommentTest(){
        Comments comments = mockComments();
        assertEquals(comments.getComment(),"Bob's QR");
    }


}
