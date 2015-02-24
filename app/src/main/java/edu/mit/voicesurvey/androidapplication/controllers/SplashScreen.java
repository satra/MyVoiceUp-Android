package edu.mit.voicesurvey.androidapplication.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import edu.mit.voicesurvey.androidapplication.R;


/**
 * This is the launch screen that is shown during the application startup.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}