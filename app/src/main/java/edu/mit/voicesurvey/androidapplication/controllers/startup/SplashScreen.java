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
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.OhmageClient;


/**
 * This is the launch screen that is shown during the application startup.
 */
public class SplashScreen extends Activity implements AsyncResponse {
    Class newClass = PreviewActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(getString(R.string.saved_username), "").equals("")) {
            OhmageClient.authorizeUser(sharedPreferences.getString(getString(R.string.saved_username), ""), sharedPreferences.getString(getString(R.string.saved_password), ""), this);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, PreviewActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 4000);
        }
        NotifyService.setAlarms(this);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    public void processFinish(int method, boolean success, String error) {
        if (method == AsyncResponse.LOGIN) {
            if (success) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 4000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, PreviewActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 4000);
            }
        }
    }
}