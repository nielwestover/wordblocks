package com.afterglowapps.wordblocks;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import afterglowapps.com.wordblocks.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //WordBlocksView wordBlocksView = (WordBlocksView) findViewById(R.id.wordBlocksView);
    }
}
