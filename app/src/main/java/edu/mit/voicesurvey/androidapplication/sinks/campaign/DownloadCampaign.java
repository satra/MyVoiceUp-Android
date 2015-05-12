package edu.mit.voicesurvey.androidapplication.sinks.campaign;

import android.os.AsyncTask;
import android.os.Environment;

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

import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;

public class DownloadCampaign extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        boolean success = downloadFromUrl();
        ((AsyncResponse)params[0]).processFinish(AsyncResponse.DOWNLOAD_CAMPAIGN, success, "");
        return null;
    }

    public boolean downloadFromUrl() {
        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        String fileName = month+"-"+year+"-campaign.json";
        String downloadUrl = "https://voicesurvey.mit.edu/sites/default/files/documents/campaign.json";
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
