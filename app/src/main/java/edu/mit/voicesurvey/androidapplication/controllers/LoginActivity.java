package edu.mit.voicesurvey.androidapplication.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.OhmageClient;

public class LoginActivity extends ActionBarActivity implements AsyncResponse {
    EditText username;
    EditText password;
    Button signIn;
    Button resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.sign_in);
        resetPassword = (Button) findViewById(R.id.reset_password);
    }

    public void signIn(View view) {
        signIn.setEnabled(false);
        OhmageClient.authorizeUser(username.getText().toString(), password.getText().toString(), this);
    }

    public void forgotPassword(View view) {
        final AsyncResponse caller = this;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Reset Password");
        alert.setMessage("Please input your email address associated with this account to reset your password.");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = input.getText().toString();
                OhmageClient.resetPassword(username.getText().toString(), email, caller);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    @Override
    public void processFinish(int method, boolean success, String error) {
        if (method == AsyncResponse.LOGIN) {
            if (success) {
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.saved_username), username.getText().toString());
                editor.putString(getString(R.string.saved_password), password.getText().toString());
                editor.commit();

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            } else {
                signIn.setEnabled(true);
                showMessage(error);
            }
        } else if (method == AsyncResponse.FORGOT_PASSWORD) {
            if (success) {
                showMessage("Password reset. Check your email for further instructions.");
            } else {
                resetPassword.setEnabled(true);
                showMessage(error);
            }
        }
    }
    public void showMessage(String message) {
        if (message.equals("The account is disabled.")) {
            message += " Have you verified your email yet?";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // close dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
