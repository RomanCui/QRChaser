package com.example.qrchaser;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.example.qrchaser.logIn.LoginEmailActivity;
import com.example.qrchaser.logIn.WelcomeActivity;
import com.example.qrchaser.player.browse.BrowseActivity;
import com.example.qrchaser.player.CameraScannerActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.example.qrchaser.player.myQRCodes.QrAddScreenActivity;
import com.robotium.solo.Solo;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
     * Test if scanner activity is launched successfully
     * Note: Required granted permission beforehand
     */
    @Test
    public void scannerTest() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("QR Code"); //Click QR CODE Button
        solo.assertCurrentActivity("Not scanner Activity", CameraScannerActivity.class);

    }

    /**
     *  Test if guest login works
     */
    @Test
    public void guestTest() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);
    }

    /**
     *  Test email login with fake email/pw, the activity should not change
     */
    @Test
    public void testEmailLoginFail() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Email");
        solo.assertCurrentActivity("Not Login Email Activity", LoginEmailActivity.class);

        //enter email and password
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress2), "testemail");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword2), "testPW");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Not Login Email Activity", LoginEmailActivity.class);

    }

    /**
     *  Test email login with fake email/pw, the activity should not change
     */
    @Test
    public void testEmailLoginPass() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Email");
        solo.assertCurrentActivity("Not Login Email Activity", LoginEmailActivity.class);

        //enter email and password
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress2), "ronggang@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword2), "123456");
        solo.clickOnButton("Login");

        solo.waitForText("MY QR codes");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);

    }

    /**
     *  Test navigation bar to access MyQRCodeScreen
     */
    @Test
    public void testNavigation1() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);

        solo.clickOnButton("1");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);
    }

    /**
     *  Test navigation bar to access Browse qr code screen
     */
    @Test
    public void testNavigation2() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);

        solo.clickOnButton("2");
        solo.assertCurrentActivity("Not Browse Activity", BrowseActivity.class);
    }

    /**
     *  Test navigation bar to Map
     *  Note : Location Permission required beforehand
     */
    @Test
    public void testNavigation3() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);

        solo.clickOnButton("3");
        solo.assertCurrentActivity("Not Map Activity", MapActivity.class);
    }

    /**
     *  Test navigation bar to player profile
     */
    @Test
    public void testNavigation4() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);

        solo.clickOnButton("4");
        solo.assertCurrentActivity("Not player profile Activity", PlayerProfileActivity.class);
    }


    /**
     *  Test floating button to go to QrAddScreen
     */
    @Test
    public void testFloatingButton() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);
        solo.waitForText("MY QR codes");

        View qrAddButton = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(qrAddButton);
        solo.assertCurrentActivity("Not QrAddScreen Activity", QrAddScreenActivity.class);
    }

    /**
     *  Test launching scanner inside QrAddScreen
     */
    @Test
    public void testAddQRScanner() {
        solo.assertCurrentActivity("Not main Activity", WelcomeActivity.class);
        solo.clickOnButton("Guest");
        solo.assertCurrentActivity("Not MyQRCodeScreen Activity", MyQRCodeScreenActivity.class);
        solo.waitForText("MY QR codes");

        View qrAddButton = solo.getCurrentActivity().findViewById(R.id.floatingActionButton);
        solo.clickOnView(qrAddButton);
        solo.assertCurrentActivity("Not QrAddScreen Activity", QrAddScreenActivity.class);

        solo.clickOnButton("Scan");
        solo.assertCurrentActivity("Not Scanner Activity", CameraScannerActivity.class);
    }

    @After
    public void teatDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
