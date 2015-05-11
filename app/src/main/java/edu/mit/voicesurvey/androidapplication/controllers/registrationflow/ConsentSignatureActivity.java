package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.mit.voicesurvey.androidapplication.R;

public class ConsentSignatureActivity extends ActionBarActivity {
    SignatureView signatureView;
    EditText inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_signature);
        signatureView = (SignatureView) findViewById(R.id.signature);
        inputView = (EditText) findViewById(R.id.name);
    }

    public void clear(View view) {
        signatureView.clear();
    }
    public void submit(View view) {
        signatureView.exportFile(this.getFilesDir().toString(),"signature.png");
        String input = inputView.getText().toString();
        if (input.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.name_required);
            builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // close dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Intent i = new Intent(ConsentSignatureActivity.this, AccountCreationActivity.class);
            startActivity(i);
        }
    }
}
