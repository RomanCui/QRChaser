package com.example.qrchaser;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.profile.EditPlayerProfileActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PlayerProfileActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<PlayerProfileActivity> rule = new ActivityTestRule<>(PlayerProfileActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void infoButtonTest() {
        solo.assertCurrentActivity("Wrong Activity", PlayerProfileActivity.class);
        solo.clickOnButton("Info");
        solo.assertCurrentActivity("Not EditPlayerProfileActivity", EditPlayerProfileActivity.class);
    }
}
