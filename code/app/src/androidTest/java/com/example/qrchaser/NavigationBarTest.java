package com.example.qrchaser;

import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrchaser.player.browse.BrowsePlayerActivity;
import com.example.qrchaser.player.browse.BrowseQRActivity;
import com.example.qrchaser.player.map.MapActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavigationBarTest {
    // rule attribute allows functional testing of activities
    // MAINTENANCE: change MainActivity to whatever the startup activity is.
    @Rule
    public ActivityTestRule<MyQRCodeScreenActivity> rule =
            new ActivityTestRule<>(MyQRCodeScreenActivity.class, true, true);
    private Solo solo;

    /**
     * Runs before all tests; initializes solo object.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // instrumentation allows programmatic control of UI/events (buttons, etc).
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Closes activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



    @Test
    public void testBrowseQR() {
        
        View spButton = solo.getView("browse_qr");
        solo.clickOnView(spButton);
        solo.assertCurrentActivity("Search Players button failed!", BrowseQRActivity.class);

    }

    @Test
    public void testBrowsePlayer() {

        View bqButton = solo.getView("browse_qr");
        solo.clickOnView(bqButton);
        View bpButton = solo.getView("browse_other_players");
        solo.clickOnView(bpButton);
        solo.assertCurrentActivity("Search Players button failed!", BrowsePlayerActivity.class);

    }

    @Test
    public void testMap() {

        View bqButton = solo.getView("map");
        solo.clickOnView(bqButton);
        solo.assertCurrentActivity("Search Players button failed!", MapActivity.class);

    }


    @Test
    public void testSelfProfile() {

        View bqButton = solo.getView("self_profile");
        solo.clickOnView(bqButton);
        solo.assertCurrentActivity("Search Players button failed!", PlayerProfileActivity.class);

    }

}
