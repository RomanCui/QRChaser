package com.example.qrchaser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import com.example.qrchaser.oop.QRCode;


public class QRCodeTest {

    private QRCode mockQRCode1(){
        QRCode myQRCode = new QRCode("XXX", "MyCode1",
                "ronggang1@ualberta.ca", "",
                200.0, 200.0);
        return myQRCode;
    }

    private QRCode mockQRCode2(){
        QRCode myQRCode = new QRCode("XXX", "MyCode2",
                "ronggang2@ualberta.ca", "",
                200.0, 200.0);
        return myQRCode;
    }

    private QRCode mockQRCode3(){
        QRCode myQRCode = new QRCode("YYY", "MyCode3",
                "ronggang3@ualberta.ca", "",
                200.0, 200.0);
        return myQRCode;
    }

    @Test
    public void testHash(){
        QRCode code1 = mockQRCode1();
        QRCode code2 = mockQRCode2();
        QRCode code3 = mockQRCode3();

        assertTrue(code1.getHash().equals(code2.getHash()));
        assertFalse(code1.getHash().equals(code3.getHash()));
    }


    @Test
    public void testScore(){
        QRCode code1 = mockQRCode1();
        QRCode code2 = mockQRCode2();
        QRCode code3 = mockQRCode3();

        assertEquals(code1.getScore(),code2.getScore());
        assertNotEquals(code1.getScore(),code3.getScore());

    }

}