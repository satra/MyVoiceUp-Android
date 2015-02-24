package edu.mit.voicesurvey.androidapplication.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.DataHolder;


public class HomeActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (DataHolder.getInstance().getTodaysSurvey() == null) {
            Button button = (Button) findViewById(R.id.todays_survey);
            button.setEnabled(false);
            button.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    public void startSurvey(View view) {
        Intent i = new Intent(HomeActivity.this, SurveyActivity.class);
        startActivity(i);
        finish();
    }
}
