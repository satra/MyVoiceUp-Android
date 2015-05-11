package edu.mit.voicesurvey.androidapplication.controllers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.webkit.WebView;

import edu.mit.voicesurvey.androidapplication.R;

public class ResourcesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://voicesurvey.mit.edu/depression-suicide-resources");
    }
}
