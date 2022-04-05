package com.example.qrchaser;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrchaser.player.myQRCodes.EditQRCodeScreenActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.myQRCodes.QrAddScreenActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MyQRCodeScreenActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MyQRCodeScreenActivity> rule = new ActivityTestRule<>(MyQRCodeScreenActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity",MyQRCodeScreenActivity.class);
        View listview = solo.getView("listViewQRCode");
        solo.clickOnView(listview);
        solo.assertCurrentActivity("Not EditQRCodeScreenActivity", EditQRCodeScreenActivity.class);
    }


    @Test
    public void addButtonTest(){
        solo.assertCurrentActivity("Wrong Activity",MyQRCodeScreenActivity.class);
        View addButton = solo.getView("floatingActionButton");
        solo.clickOnView(addButton);
        solo.assertCurrentActivity("Not QrAddScreenActivity",QrAddScreenActivity.class);
    }
}
