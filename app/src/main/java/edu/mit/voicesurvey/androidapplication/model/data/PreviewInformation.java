package edu.mit.voicesurvey.androidapplication.model.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.mit.voicesurvey.androidapplication.R;

public class PreviewInformation {
    public static List<String> previewList = new ArrayList<>();
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
            InputStream jsonStream = context.getResources().openRawResource(R.raw.preview_text);
            String str = readTextFile(jsonStream);
            try {
                JSONArray screens = new JSONArray(str);
                for (int i = 0; i < screens.length(); i++) {
                    previewList.add(screens.getString(i));
                }
                initialized = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
