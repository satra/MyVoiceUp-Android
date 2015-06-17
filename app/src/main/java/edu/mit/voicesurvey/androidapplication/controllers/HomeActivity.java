package edu.mit.voicesurvey.androidapplication.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.startup.SplashScreen;
import edu.mit.voicesurvey.androidapplication.model.Campaign;
import edu.mit.voicesurvey.androidapplication.model.data.CampaignInformation;
import edu.mit.voicesurvey.androidapplication.sinks.campaign.DownloadCampaign;
import edu.mit.voicesurvey.androidapplication.sinks.campaign.DownloadLatestCampaignFileName;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.OhmageClient;


/**
 * The landing page of the application
 */
public class HomeActivity extends Activity implements AsyncResponse {
    int uploadingPastSurvey = 0;

    // Progress Dialog, gac
    private ProgressDialog pDialogCampaign;
    private ProgressDialog pDialog;
    // Progress dialog type (0- for Horizontal progress bar)
    public static final int progress_bar_type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // GAC:  If DownloadFileFromURL is inside displayUsual,
        // the progress dialog does not get dismissed.  This can only be because the pDialog being
        // dismissed is different than the one that is created, but GAC does not know how this can
        // be the case.

        // On post execute displayUsual (even if not successful at downloading)
        new DownloadFileFromURL().execute("file_url");

        //Toast.makeText(this, "finished", Toast.LENGTH_SHORT);


        //if (true){ //(CampaignInformation.getMustDownloadCampaignFlag()){
        //    new DownloadCampaignFromURL().execute("file_url");
        //}


        displayUsual();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUsual();
    }


    public void displayUsual() {


                // This initialization doesn't do anything if already initialized.
                // If not initialized, it parses the campaign.
                // onCreate and onResume, poll for latest survey
                //doSomething();
                //
                //downloadLatestCampaignFileName();

                // Either no campaign.json, or out of date campaign.json
                //if (CampaignInformation.parseCampaign()){
                    /*
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    GregorianCalendar todayg = new GregorianCalendar();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    String date = formatter.format(todayg.getTime());
                    */
                    if (CampaignInformation.getMissedSurvey(HomeActivity.this) == null) {
                        Button button = (Button) findViewById(R.id.past_survey);
                        button.setVisibility(View.GONE);
                    } else {
                        Button button = (Button) findViewById(R.id.past_survey);
                        button.setVisibility(View.VISIBLE);
                    }
                    if (!CampaignInformation.parseCampaign() | CampaignInformation.getTodaysSurvey(HomeActivity.this) == null) {
                        // Survey can't be taken
                        Button button = (Button) findViewById(R.id.todays_survey);
                        button.setEnabled(false);
                        button.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                    }
                    else if ( CampaignInformation.doneToday(HomeActivity.this)) {
                        // Survey was taken
                        Button button = (Button) findViewById(R.id.todays_survey);
                        button.setVisibility(View.VISIBLE);
                        button.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    else {
                        // Survey was not taken
                        Button button = (Button) findViewById(R.id.todays_survey);
                        button.setVisibility(View.VISIBLE);
                        //button.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                    }

                    //

                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    TextView welcomeUserName = (TextView) findViewById(R.id.welcomeUserName);
                    welcomeUserName.setText("Welcome " + sharedPreferences.getString(getString(R.string.saved_username), "") + "!");
                    TextView totalAnswered = (TextView) findViewById(R.id.total_answered);
                    TextView currentStreak = (TextView) findViewById(R.id.current_streak);
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

                    if (hasSurveysToUpload()) {
                        Button button = (Button) findViewById(R.id.upload_past);
                        button.setVisibility(View.VISIBLE);
                        if (uploadingPastSurvey > 0) {
                            button.setClickable(false);
                        }
                    } else {
                        Button button = (Button) findViewById(R.id.upload_past);
                        button.setVisibility(View.GONE);
                    }

                    // http://www.mysamplecode.com/2013/04/android-display-application-version.html
                    PackageInfo pInfo = null;
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    TextView appVersionName = (TextView)findViewById(R.id.appVersionName);

                    //in your OnCreate() method
                    appVersionName.setText("Version: " + pInfo.versionName);

                //}

                //else {
                //    downloadCampaign();
                //}


                //CampaignInformation.init();




                /*else {
                    Button button1 = (Button) findViewById(R.id.past_survey);
                    Button button2 = (Button) findViewById(R.id.todays_survey);
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    Button button3 = (Button) findViewById(R.id.download_campaign);
                    button3.setVisibility(View.VISIBLE);
                }*/



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



    public void submitPastSurveys(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String surveyList = sharedPreferences.getString("SURVEY_LIST", null);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String[] surveyArr = gson.fromJson(surveyList, String[].class); // list of survey dates
        for (String s : surveyArr) {
            String campaign = sharedPreferences.getString(s+"-campaign", "");
            String date = sharedPreferences.getString(s+"-date","");
            String responses = sharedPreferences.getString(s+"-responses", "");
            String audioFileUUID1 = sharedPreferences.getString(s+"-audioFileUUID1", null);
            String audioFileUUID2 = sharedPreferences.getString(s+"-audioFileUUID2", null);
            uploadingPastSurvey ++;
            OhmageClient.uploadSurvey(campaign, date, responses, this, audioFileUUID1, audioFileUUID2);
        }
    }

    private boolean hasSurveysToUpload() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String surveyList = sharedPreferences.getString("SURVEY_LIST", null);
        return surveyList != null;
    }

    @Override
    public void processFinish(int method, boolean success, String error) {
        if (method == AsyncResponse.DOWNLOAD_CAMPAIGN) {
            if (success) {
                displayUsual();
            }
        } else if (method == AsyncResponse.UPLOAD_SURVEY) {
            uploadingPastSurvey --;
            Button button = (Button) findViewById(R.id.upload_past);
            button.setClickable(true);
            if (!success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(error);
                builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // close dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("SURVEY_LIST", null).commit();
            }
            displayUsual();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    // Logic:  onCreate/onResume, async task for downloading file
    // onPostExecute:  parseCampaignKey.  At the end of parseCampaignKey, call ParseCampaign.
    // At then end of ParseCampaign, if no keymatch, downloadCampaign async.
    // In downloadcampaign async, onpostexecute, parseCampaign.
    //
    //


    /**
     * Background Async Task to download file
     */
    // GAC:  Tutorial just had "class" without public, private, or static
    // adding static breaks the code
    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread
         * Show Progress Bar dialog
         */
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Downloading file. Please wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Downloading file in background thread
         */


        @Override
        protected String doInBackground(String... f_url) {

            try {

                //f_url[0]="https://voicesurvey.mit.edu/sites/default/files/documents/latestCampaignFileName.json";
                URL url = new URL("https://voicesurvey.mit.edu/sites/default/files/documents/campaignLatestFileName.json");
                URLConnection connection = url.openConnection();
                connection.connect();

                //this will be useful so that you can show a typical 0-100% progress
                int lengthOfFile = connection.getContentLength();


                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                File root = Environment.getExternalStorageDirectory();

                File dir = new File (root.getAbsolutePath() + "/campaigns");
                if(dir.exists()==false) {
                    dir.mkdirs();
                }
                String fileName = "campaignLatestFileName.json";
                File file = new File(dir, fileName);
                OutputStream output = new FileOutputStream(file);


                byte data[] = new byte [1024];

                long total = 0;
                int count = 0; //GAC added this

                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishing the progress ...
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);

                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();




            } catch (Exception e) {
                Log.e("error: ", e.getMessage());

            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String ... progress) {
            // Setting progress percentage

            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         *  After completing background task
         *  Dismiss the progress dialog
         */
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();

            CampaignInformation.parseLatestCampaignFileName();

            if (CampaignInformation.parseCampaign()){
                displayUsual();
            }
            else {
                // on post execute, parseCampaign, then displayUsual
                String campaignURL = "https://voicesurvey.mit.edu/sites/default/files/documents/campaign.json";
                new DownloadCampaignFromURL().execute(campaignURL);
            }
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Background Async Task to download file
     */
    // GAC:  Tutorial just had "class" without public, private, or static
    // adding static breaks the code
    public class DownloadCampaignFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread
         * Show Progress Bar dialog
         */
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialogCampaign = new ProgressDialog(HomeActivity.this);
            pDialogCampaign.getWindow().setGravity(Gravity.BOTTOM);
            pDialogCampaign.setMessage("Downloading campaign file. Please wait ...");
            pDialogCampaign.setIndeterminate(false);
            pDialogCampaign.setMax(100);
            pDialogCampaign.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialogCampaign.setCancelable(true);
            pDialogCampaign.show();

        }

        /**
         * Downloading file in background thread
         */


        @Override
        protected String doInBackground(String... f_url) {

            try {

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                //this will be useful so that you can show a typical 0-100% progress
                int lengthOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                File root = Environment.getExternalStorageDirectory();

                File dir = new File (root.getAbsolutePath() + "/campaigns");
                if(dir.exists()==false) {
                    dir.mkdirs();
                }
                String fileName = "campaign.json";
                File file = new File(dir, fileName);
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte [1024];
                long total = 0;
                int count = 0; //GAC added this

                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishing the progress ...
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);

                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("error: ", e.getMessage());

            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String ... progress) {
            // Setting progress percentage
            pDialogCampaign.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         *  After completing background task
         *  Dismiss the progress dialog
         */
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            /////dismissDialog(progress_bar_type);
            pDialogCampaign.dismiss();

            CampaignInformation.parseCampaign();

            // update display
            displayUsual();
        }

    }




    //@Override
    //public void onBackPressed(){
    //    Toast.makeText(getApplicationContext(), "Log out to switch users", Toast.LENGTH_LONG).show();
    //}




}
