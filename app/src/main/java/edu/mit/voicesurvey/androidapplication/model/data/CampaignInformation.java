package edu.mit.voicesurvey.androidapplication.model.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.HomeActivity;
import edu.mit.voicesurvey.androidapplication.model.Campaign;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QTextChoice;
import edu.mit.voicesurvey.androidapplication.model.Survey;

public class CampaignInformation {
    public static Campaign campaign;
    private static boolean initialized = false;
    private static String latestCampaignName="notLatest";
    private static boolean mustDownloadCampaignFlag = false;

    // GAC variable to hold context (aka activity from which this instance of the class was created)
    /*
    public CampaignInformation(Context context){
        this.context=context;
    }
    */

    //private Context context;
    /**
     * Finds the survey to be answered today
     *
     * @return
     */
    public static Survey getTodaysSurvey(Context context) {
        Calendar today = Calendar.getInstance();
        String dayOfMonth = today.get(Calendar.DAY_OF_MONTH) + "";
        if (today.get(Calendar.DAY_OF_MONTH) < 10) {
            dayOfMonth = "0" + dayOfMonth;
        }

        //If first time user, default to day 7 which is the PHQ-9
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(context.getString(R.string.num_questions), 0)==0) {
            dayOfMonth = "07";
        }

        GregorianCalendar todayg = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String date = formatter.format(todayg.getTime());

        // GAC:  This might be what limits a person to one survey a day, 6/1/2015
        /*
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(date, false)) {
            return null;
        }
        */
        if (parseCampaign()){
            for (Survey s : campaign.getSurveys()) {
                if (s.getDate().equals(dayOfMonth))
                    return s;
            }
        }
        else {
            return null;
        }

        return null;
    }

    public static boolean init() {
        if (!initialized) {
            if (parseCampaign()) {
                initialized = true;
                return initialized;
            }
        }
        return initialized;
    }

    public static boolean getMustDownloadCampaignFlag(){
        return mustDownloadCampaignFlag;
    }


    // GAC Removed the "static" in order to have context be referenceable within it
    // Context is needed to make the toast
    public  static String parseLatestCampaignFileName(){
        /*
        String path = Environment.getExternalStorageDirectory().toString()+"/campaigns";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
        }
        /*
        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        String fileName = month + "-" + year + "-campaign.json";
        */
        String fileName = "latestCampaignFileName.json";
        try {
            File root = Environment.getExternalStorageDirectory();

            File dir = new File(root.getAbsolutePath() + "/campaigns");
            File file = new File(dir + "/" + fileName);

            if (file.exists()) {
                InputStream in = new FileInputStream(file);

                JsonReader jsonReader = new JsonReader(new InputStreamReader(in));

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    switch (name) {
                        case "latestCampaignFileName": {
                            latestCampaignName = jsonReader.nextString();
                            break;
                        }
                        default: {
                            jsonReader.skipValue();
                        }
                    }
                }
                jsonReader.endObject();


                // gac 5/30/2015, doesn't think this is necessary:  return latestCampaignName;
            }
            else {
                latestCampaignName = "No latestCampaignFileName file";
                /*
                //Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                */
                Log.e("CampaginInformation", "No latest campaign");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        //parseCampaign();

        return latestCampaignName;
    }
    public static boolean parseCampaign() {

        // Need this line to make sure always working with latest known state of campaign
        parseLatestCampaignFileName();

        /* Assumes latest file is the one with the current name
        However, instead, always check the web for the latest file
        If the latest file matches the current file, then do nothing.
        If the latest file does not match the current file, download latest
        If no internet connectivity, proceed with whatever file may or may not be present.

         */


        //Latest json file was retrieved
        //Now, parse that file for the latest string name
        // gac 5/30/2015 not needed if parseLatestCampaignFileName is the only function that calls parseCampaign
        //latestCampaignName = parseLatestCampaignFileName();
        //latestCampaignName = "urn:campaign:MobileSurveyPrompts_20150511";

        //////////////////////////////////////////////////////////////


        /*
        String path = Environment.getExternalStorageDirectory().toString()+"/campaigns";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File campaignList[] = f.listFiles();
        Log.d("Files", "Size: "+ campaignList.length);
        for (int i=0; i < campaignList.length; i++)
        {
            Log.d("Files", "FileName:" + campaignList[i].getName());
            // if latest campaign does not match any of the downloaded campaigns,
        }
        */
        /*
        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        String fileName = month + "-" + year + "-campaign.json";
        */
        //String fileName = latestCampaignName;
        try {
            File root = Environment.getExternalStorageDirectory();

            File dir = new File(root.getAbsolutePath() + "/campaigns");
            File file = new File(dir + "/" + "campaign.json");

            if (file.exists()) {
                InputStream in = new FileInputStream(file);

                JsonReader jsonReader = new JsonReader(new InputStreamReader(in));

                String campaignURN = "";
                String timestamp = "";
                ArrayList<Survey> surveys = new ArrayList<>();
                ArrayList<Question> questions = new ArrayList<>();

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    switch (name) {
                        case "urn": {
                            campaignURN = jsonReader.nextString();
                            break;
                        }
                        case "timestamp": {
                            timestamp = jsonReader.nextString();
                            break;
                        }
                        case "questions": {
                            questions = parseQuestions(jsonReader);
                            break;
                        }
                        case "surveys": {
                            surveys = parseSurveys(jsonReader, questions);
                            break;
                        }
                        default: {
                            jsonReader.skipValue();
                        }
                    }
                }
                jsonReader.endObject();

                // Check if the existing file is the latest file
                // If no latestCampaignFileName file, assume that whatever campaign exists is the
                // latest
                if (campaignURN.equals(latestCampaignName)  |
                        latestCampaignName.equals("No latestCampaignFileName file")){
                    campaign = new Campaign(campaignURN, timestamp, surveys);
                    initialized = true;
                    //return true;
                }
                else {
                    // campaign.json is not the latest, therefore async call to download campaign
                    // then, in onPostExecute of download campaign, call parseCampaign again
                    //new HomeActivity.DownloadFileFromURL().execute("file_url");
                    //HomeActivity.publicStaticDownloadCampaign();

                    initialized = false;
                    //return false;
                }
                // Silly to have the latest check after parsing the existing file, but ok.  gac.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initialized; //false;
    }

    private static ArrayList<Question> parseQuestions(JsonReader jsonReader) throws IOException {
        ArrayList<Question> questions = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            questions.add(parseQuestion(jsonReader));
        }
        jsonReader.endArray();
        return questions;
    }

    private static Question parseQuestion(JsonReader jsonReader) throws IOException {
        String id = null;
        String promptId = null;
        String type = null;
        String text = null;
        int rangeMin = 0;
        int rangeMax = 0;
        double rangeStep = 0;

        ArrayList<String> stringChoices = null;
        ArrayList<Pair<String, String>> pairChoices = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "question_id": {
                    id = jsonReader.nextString();
                    break;
                }
                case "question_type": {
                    type = jsonReader.nextString();
                    break;
                }
                case "question_prompt_id": {
                    promptId = jsonReader.nextString();
                    break;
                }
                case "question_text": {
                    text = jsonReader.nextString();
                    break;
                }
                case "range_min": {
                    rangeMin = jsonReader.nextInt();
                    break;
                }
                case "range_max": {
                    rangeMax = jsonReader.nextInt();
                    break;
                }
                case "range_step": {
                    rangeStep = jsonReader.nextDouble();
                    break;
                }
                case "choices": {
                    if (type != null) {
                        if (type.equals("image_choices")) {
                            pairChoices = parseImageChoices(jsonReader);
                        } else {
                            stringChoices = parseStringChoices(jsonReader);
                        }
                    }
                    break;
                }
                default: {
                    jsonReader.skipValue();
                }
            }
        }
        jsonReader.endObject();

        switch (type) {
            case "audio": {
                return new QAudioRecording(promptId, id, text);
            }
            case "range": {
                return new QRange(promptId, id, text, rangeMin, rangeMax, rangeStep);
            }
            case "image_choices": {
                return new QImgChoice(promptId, id, text, pairChoices);
            }
            case "single_choice": {
                return new QTextChoice(promptId, id, text, stringChoices);
            }
            case "boolean": {
                return new QBoolChoice(promptId, id, text);
            }
            case "": {
                return new QTextChoice(promptId, id, text, null);
            }
        }
        return null;
    }

    private static ArrayList<String> parseStringChoices(JsonReader jsonReader) throws IOException {
        ArrayList<String> stringChoices = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            stringChoices.add(jsonReader.nextString());
        }
        jsonReader.endArray();
        return stringChoices;
    }

    private static ArrayList<Pair<String, String>> parseImageChoices(JsonReader jsonReader) throws IOException {
        ArrayList<Pair<String, String>> imageChoices = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String item1 = "";
            String item2 = "";
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                switch (name) {
                    case "image": {
                        item1 = jsonReader.nextString();
                        break;
                    }
                    case "label": {
                        item2 = jsonReader.nextString();
                        break;
                    }
                }
            }
            imageChoices.add(new Pair<>(item1, item2));
            jsonReader.endObject();
        }
        jsonReader.endArray();
        return imageChoices;
    }

    private static ArrayList<Survey> parseSurveys(JsonReader jsonReader, ArrayList<Question> questions) throws IOException {
        ArrayList<Survey> surveys = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            surveys.add(parseSurvey(jsonReader, questions));
        }
        jsonReader.endArray();
        return surveys;
    }

    private static Survey parseSurvey(JsonReader jsonReader, ArrayList<Question> questions) throws IOException {
        String date = "";
        String surveyID = "";
        ArrayList<String> questionsIdList = new ArrayList<>();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "date": {
                    String dateStr = jsonReader.nextString();
                    date = dateStr.substring(10);
                    break;
                }
                case "id": {
                    surveyID = jsonReader.nextString();
                    break;
                }
                case "questions": {
                    questionsIdList = parseQuestionIdList(jsonReader);
                    break;
                }
                default: {
                    jsonReader.skipValue();
                }
            }
        }
        jsonReader.endObject();

        ArrayList<Question> questionsList = new ArrayList<>();
        for (String id : questionsIdList) {
            for (Question question : questions) {
                if (question.getId().equals(id)) {
                    questionsList.add(question);
                }
            }
        }

        return new Survey(surveyID, date, questionsList);
    }

    private static ArrayList<String> parseQuestionIdList(JsonReader jsonReader) throws IOException {
        ArrayList<String> questionList = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            questionList.add(jsonReader.nextString());
        }
        jsonReader.endArray();
        return questionList;
    }

    public static Survey getMissedSurvey(Context context) {
        GregorianCalendar todayg = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/");
        String date = formatter.format(todayg.getTime());
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // GAC commented out
        /*
        int day = todayg.get(Calendar.DAY_OF_MONTH);
        if (day > 7 && day < 11) {
            for (Survey s : campaign.getSurveys()) {
                if (s.getDate().equals("07")) {
                    if (sharedPreferences.getBoolean(date + "07", false)) {
                        return null;
                    }
                    return s;
                }
            }
        } else if (day > 21 && day < 25) {
            for (Survey s : campaign.getSurveys()) {
                if (s.getDate().equals("21")) {
                    if (sharedPreferences.getBoolean(date + "21", false)) {
                        return null;
                    }
                    return s;
                }
            }
        }
        */
        return null;
    }






}
