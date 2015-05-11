package edu.mit.voicesurvey.androidapplication.model.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.ConsentStep;

public class ConsentInformation {
    public static List<ConsentStep> consentStepList = new ArrayList<>();
    public static String fullConsentForm;
    private static boolean initialized = false;

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    public static void init(Context context) {
        if (!initialized) {
            InputStream jsonStream = context.getResources().openRawResource(R.raw.consent_text);
            String str = readTextFile(jsonStream);
            try {
                JSONObject object = new JSONObject(str);
                fullConsentForm = object.getString("full_consent_text");
                JSONArray screens = object.getJSONArray("consent_screens");
                for (int i = 0; i < screens.length(); i++) {
                    JSONObject screen = screens.getJSONObject(i);
                    consentStepList.add(new ConsentStep(screen.getString("title"), screen.getString("description"), screen.getString("image"), screen.getString("additional_info")));
                }
                initialized = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
