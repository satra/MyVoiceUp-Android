package edu.mit.voicesurvey.androidapplication.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.data.CampaignInformation;


/**
 * The landing page of the application
 */
public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CampaignInformation.init(this);
        if (CampaignInformation.getTodaysSurvey() == null) {
            Button button = (Button) findViewById(R.id.todays_survey);
            button.setEnabled(false);
            button.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        TextView totalAnswered = (TextView) findViewById(R.id.total_answered);
        TextView currentStreak = (TextView) findViewById(R.id.current_streak);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        totalAnswered.setText(sharedPreferences.getInt(getString(R.string.num_questions),0)+"");
        currentStreak.setText(sharedPreferences.getInt(getString(R.string.num_days),0)+"");
    }

    public void startSurvey(View view) {
        Intent i = new Intent(HomeActivity.this, SurveyActivity.class);
        startActivity(i);
    }

    public void goToResources(View view) {
        Intent i = new Intent(HomeActivity.this, ResourcesActivity.class);
        startActivity(i);
    }
}
