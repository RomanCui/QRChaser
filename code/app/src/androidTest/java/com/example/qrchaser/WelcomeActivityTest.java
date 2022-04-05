package com.example.qrchaser;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import static org.junit.Assert.assertEquals;
import com.example.qrchaser.logIn.WelcomeActivity;
import com.example.qrchaser.oop.QRCode;
import com.example.qrchaser.player.CameraScannerActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.myQRCodes.QrAddScreenActivity;
import com.robotium.solo.Solo;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;

public class WelcomeActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<WelcomeActivity> rule = new ActivityTestRule<>(WelcomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test if MyQRCode activity is launched successfully
     * Note: Required granted permission beforehand
     */
    @Test
    public void LoginWithQRCodeTest() {
        solo.assertCurrentActivity("Not WelcomeActivity", WelcomeActivity.class);
        solo.clickOnButton("Login With QR Code"); // Click LOGIN WITH QR CODE Button
        solo.assertCurrentActivity("Not MyQRCodeScreenActivity", MyQRCodeScreenActivity.class);
    }

    /**
     * Test if login with new account works
     */
    @Test
    public void newAccountButtonTest() {
        solo.assertCurrentActivity("Not WelcomeActivity", WelcomeActivity.class);
        solo.clickOnButton("Continue with new Account");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);
    }
}