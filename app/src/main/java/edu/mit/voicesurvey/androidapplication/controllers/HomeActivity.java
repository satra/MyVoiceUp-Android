package edu.mit.voicesurvey.androidapplication.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.startup.SplashScreen;
import edu.mit.voicesurvey.androidapplication.model.data.CampaignInformation;
import edu.mit.voicesurvey.androidapplication.sinks.campaign.DownloadCampaign;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;


/**
 * The landing page of the application
 */
public class HomeActivity extends Activity implements AsyncResponse {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        displayUsual();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUsual();
    }

    public void displayUsual() {
        if (CampaignInformation.init(this)) {
            if (CampaignInformation.getMissedSurvey(this) == null) {
                Button button = (Button) findViewById(R.id.past_survey);
                button.setVisibility(View.GONE);
            } else {
                Button button = (Button) findViewById(R.id.past_survey);
                button.setVisibility(View.VISIBLE);
            }
            if (CampaignInformation.getTodaysSurvey(this) == null) {
                Button button = (Button) findViewById(R.id.todays_survey);
                button.setEnabled(false);
                button.setBackgroundColor(getResources().getColor(R.color.gray));
            } else {
                Button button = (Button) findViewById(R.id.todays_survey);
                button.setVisibility(View.VISIBLE);
            }
            TextView totalAnswered = (TextView) findViewById(R.id.total_answered);
            TextView currentStreak = (TextView) findViewById(R.id.current_streak);
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String date = sharedPreferences.getString("LAST_DATE", ""); // MM/DD/YYYY
            if (date.length() > 0) {
                String[] dates = date.split("/");
                int month = Integer.parseInt(dates[0]);
                int day = Integer.parseInt(dates[1]);
                int year = Integer.parseInt(dates[2]);
                GregorianCalendar lastDate = new GregorianCalendar(year, month, day);
                GregorianCalendar today = new GregorianCalendar();
                GregorianCalendar todaysDate = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                int daysBetween = (int) ((lastDate.getTime().getTime() - todaysDate.getTime().getTime()) / (1000 * 60 * 60 * 24));
                if (daysBetween > 1) {
                    sharedPreferences.edit().putInt(getString(R.string.num_days), 0).commit();
                }
            }
            totalAnswered.setText(sharedPreferences.getInt(getString(R.string.num_questions), 0) + "");
            currentStreak.setText(sharedPreferences.getInt(getString(R.string.num_days), 0) + "");
        } else {
            Button button1 = (Button) findViewById(R.id.past_survey);
            Button button2 = (Button) findViewById(R.id.todays_survey);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            Button button3 = (Button) findViewById(R.id.download_campaign);
            button3.setVisibility(View.VISIBLE);
        }
    }

    public void startSurvey(View view) {
        Intent i = new Intent(HomeActivity.this, SurveyActivity.class);
        i.putExtra("PAST", false);
        startActivity(i);
    }

    public void startPastSurvey(View view) {
        Intent i = new Intent(HomeActivity.this, SurveyActivity.class);
        i.putExtra("PAST", true);
        startActivity(i);
    }

    public void goToResources(View view) {
        Intent i = new Intent(HomeActivity.this, ResourcesActivity.class);
        startActivity(i);
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.saved_username), "").commit();
        Intent i = new Intent(HomeActivity.this, SplashScreen.class);
        startActivity(i);
        finish();
    }

    public void downloadCampaign(View view) {
        (new DownloadCampaign()).execute(this);
    }

    @Override
    public void processFinish(int method, boolean success, String error) {
        if (method == AsyncResponse.DOWNLOAD_CAMPAIGN) {
            if (success) {
                displayUsual();
            }
        }
    }
}
