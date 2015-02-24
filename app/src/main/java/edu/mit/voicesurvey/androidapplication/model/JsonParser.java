package edu.mit.voicesurvey.androidapplication.model;

import android.content.Context;
import android.util.JsonReader;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QTextChoice;

/**
 * Created by Ashley on 2/22/2015.
 */
public class JsonParser {

    public static Campaign parseCampaign(DataHolder dataHolder) {
        try {
           // InputStream ins = context.getResources().openRawResource(
             //       context.getResources().getIdentifier("campaign",
               //             "raw", context.getPackageName()));
            String file = "res/raw/campaign.json"; // res/raw/test.txt also work.
            InputStream in = dataHolder.getClass().getClassLoader().getResourceAsStream(file);
            JsonReader jsonReader = new JsonReader(new InputStreamReader(in));

            String id = null;
            String author = null;
            ArrayList<Survey> surveys = new ArrayList<>();
            ArrayList<Question> questions = new ArrayList<>();

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                switch(name) {
                    case "id": {
                        id = jsonReader.nextString();
                        break;
                    }
                    case "author": {
                        author = jsonReader.nextString();
                        break;
                    }
                    case "questions": {
                        questions = parseQuestions(jsonReader);
                        break;
                    }
                    case "surveys": {
                        surveys = parseSurveys(jsonReader, questions, author);
                        break;
                    }
                    default: {
                        jsonReader.skipValue();
                    }
                }
            }
            jsonReader.endObject();

            return new Campaign(id, author, surveys);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        String type = null;
        String text = null;
        int rangeMin =0;
        int rangeMax = 0;
        double rangeStep = 0;

        ArrayList<String> stringChoices = null;
        ArrayList<Pair<String, String>> pairChoices = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch(name) {
                case "question_id": {
                    id = jsonReader.nextString();
                    break;
                }
                case "question_type": {
                    type = jsonReader.nextString();
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
            case "audio_recording": {
                return new QAudioRecording(id, text, false);
            }
            case "range": {
                return new QRange(id, text, false, rangeMin, rangeMax, rangeStep);
            }
            case "image_choices": {
                return new QImgChoice(id, text, false, pairChoices);
            }
            case "text_choices": {
                return new QTextChoice(id, text, false, stringChoices);
            }
            case "boolean": {
                return new QBoolChoice(id, text, false);
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

    private static ArrayList<Pair<String,String>> parseImageChoices(JsonReader jsonReader) throws IOException
    {
        ArrayList<Pair<String,String>> imageChoices = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String item1 = "";
            String item2 = "";
            while (jsonReader.hasNext())
            {
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

    private static ArrayList<Survey> parseSurveys(JsonReader jsonReader, ArrayList<Question> questions, String author) throws IOException {
        ArrayList<Survey> surveys = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            surveys.add(parseSurvey(jsonReader, questions, author));
        }
        jsonReader.endArray();
        return surveys;
    }

    private static Survey parseSurvey(JsonReader jsonReader, ArrayList<Question> questions, String author) throws IOException {
        Date date = null;
        int ttl = 0;
        ArrayList<String> questionsIdList = new ArrayList<>();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch(name) {
                case "date": {
                    String dateStr = jsonReader.nextString();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = format.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "ttl": {
                    ttl = jsonReader.nextInt();
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

        return new Survey(ttl, date, author, questionsList);
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

}
