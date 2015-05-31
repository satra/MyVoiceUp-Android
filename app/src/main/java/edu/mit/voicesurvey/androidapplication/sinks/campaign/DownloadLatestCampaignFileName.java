package edu.mit.voicesurvey.androidapplication.sinks.campaign;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.mit.voicesurvey.androidapplication.controllers.HomeActivity;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;

public class DownloadLatestCampaignFileName extends AsyncTask<String, Void, Boolean> {

    // Progress Dialog Object
   /// private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
   /// public static final int progress_bar_type = 0;

    private Activity activity;
    /** progress dialog to show user that the backup is processing. */
    private ProgressDialog dialog;
    public void DownloadLatestCampaignFileName(Activity activity) {
        this.activity = activity;
        context = activity;
        dialog = new ProgressDialog(context);
    }
    /** application context. */
    private Context context;



    protected void onPreExecute() {
        this.dialog.setMessage("Progress start");
        this.dialog.show();
    }



        @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }


        //MessageListAdapter adapter = new MessageListAdapter(activity, titles);
        //setListAdapter(adapter);
        //adapter.notifyDataSetChanged();


        if (success) {
            Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
        }
    }

    //protected Object doInBackground(Object[] params) {
    @Override
    protected Boolean doInBackground(final String... args) {
        boolean success = downloadFromUrl();
        //((AsyncResponse)params[0]).processFinish(AsyncResponse.DOWNLOAD_LATEST_CAMPAIGN_FILE_NAME, success, "");
        return null;
    }

    public boolean downloadFromUrl() {
        String fileName = "latestCampaignFileName.json";
        String downloadUrl = "https://voicesurvey.mit.edu/sites/default/files/documents/latestCampaignFileName.json";
        try {
            File root = Environment.getExternalStorageDirectory();

            File dir = new File (root.getAbsolutePath() + "/campaigns");
            if(dir.exists()==false) {
                dir.mkdirs();
            }

            URL url = new URL(downloadUrl);
            File file = new File(dir, fileName);

           /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

           /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

           /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }


           /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            return true;

        } catch (IOException e) {
        }
        return false;
    }



}
