package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import edu.mit.voicesurvey.androidapplication.R;

public class EligibilityActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility);
    }

    public void yes(View view) {
        Intent i = new Intent(EligibilityActivity.this, ConsentActivity.class);
        startActivity(i);
    }

    public void no(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.not_eligible);
        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // close dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
