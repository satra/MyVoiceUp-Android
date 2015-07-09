package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.HomeActivity;
import edu.mit.voicesurvey.androidapplication.controllers.LoginActivity;

public class AccountCreationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://voiceapp.mit.edu/ohmage/#register");
        myWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void signIn(View view) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    AccountCreationActivity.this);

            // set title
            alertDialogBuilder.setTitle("Did you make an Ohmage online account?");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Click yes to confirm you have created an account and activated" +
                            " it via the 'I agree' link at the bottom of the registration email." +
                            " You will be redirected to the app Login screen where you can enter " +
                    "your user name and password.")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(AccountCreationActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                            Toast.makeText(AccountCreationActivity.this,
                                    "Tip:  If using autocomplete, make sure there is no space "
                                            + "at the end of your email", Toast.LENGTH_LONG).show();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

    }

    public void helpDialog(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AccountCreationActivity.this);

        // set title
        alertDialogBuilder.setTitle("Account Creation Help");

        // set dialog message
        alertDialogBuilder
                .setMessage("(1) If using autocomplete, make sure there is no space "
                        + "at the end of your email address.  " +
                        " (2) Do NOT use Safari to click the email 'I agree' activation link.")
                .setPositiveButton("Ok",null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
