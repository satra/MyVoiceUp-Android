package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.TextView;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.data.ConsentInformation;


public class ConsentStepDetailActivity extends ActionBarActivity {
    public static final String STEP_DETAIL_VALUE = "STEP_DETAIL_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_step_detail);
        int i = this.getIntent().getIntExtra(STEP_DETAIL_VALUE, 0);
        ((TextView) findViewById(R.id.text)).setText(Html.fromHtml(ConsentInformation.consentStepList.get(i).additionalInfo));
    }
}
