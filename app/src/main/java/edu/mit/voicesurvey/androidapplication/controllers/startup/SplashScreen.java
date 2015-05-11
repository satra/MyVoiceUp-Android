package edu.mit.voicesurvey.androidapplication.controllers.startup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.HomeActivity;
import edu.mit.voicesurvey.androidapplication.notifications.NotifyService;


/**
 * This is the launch screen that is shown during the application startup.
 */
public class SplashScreen extends Activity {
    Class newClass = PreviewActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(getString(R.string.saved_username), "").equals("")) {
            newClass = HomeActivity.class;
        }
        NotifyService.setAlarms(this);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, newClass);
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}