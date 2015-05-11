package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.LoginActivity;

public class AccountCreationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://voiceapp.mit.edu/ohmage/#REGISTER");
        myWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void signIn(View view) {
        Intent intent = new Intent(AccountCreationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
