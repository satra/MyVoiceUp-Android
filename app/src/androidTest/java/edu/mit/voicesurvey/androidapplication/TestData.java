package edu.mit.voicesurvey.androidapplication;

import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.mit.voicesurvey.androidapplication.model.Campaign;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QTextChoice;
import edu.mit.voicesurvey.androidapplication.model.Survey;

/**
 * Created by Ashley on 2/22/2015.
 */
public class TestData {

    private static ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        questions.add(new QAudioRecording("audio1", "Sample audio recording test", false));

        questions.add(new QRange("range1", "Slider Question", false, 10, 20, 1));

        ArrayList<Pair<String, String>> choices = new ArrayList<>();
        choices.add(new Pair<>("sage_fatigue0.png", "ready to take on the world"));
        choices.add(new Pair<>("sage_fatigue1.png", "filled with energy"));
        choices.add(new Pair<>("sage_fatigue2.png", "energy to make it through the day"));
        choices.add(new Pair<>("sage_fatigue3.png", "energy to do basic functions"));
        choices.add(new Pair<>("sage_fatigue4.png", "no energy"));
        questions.add(new QImgChoice("imagechoice1", "How is your energy today?", false, choices));

        ArrayList<String> choices2 = new ArrayList<>();
        choices2.add("I never take longer than 30 minutes to fall asleep");
        choices2.add("I take at least 30 minutes to fall asleep, less than half the time");
        choices2.add("I take at least 30 minutes to fall asleep, more than half the time");
        choices2.add("I take more than 60 minutes to fall asleep, more than half the time");
        questions.add(new QTextChoice("textchoice1", "1) Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.", false, choices2));

        questions.add(new QBoolChoice("boolean1", "Do you like chicken?", false));

        return questions;
    }


    private static Survey getSurvey(String dateStr) {
        ArrayList<Question> questions = getQuestions();

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Survey survey = new Survey(1, date, "MIT Voice and Depression study team", questions);
        return survey;
    }

    public static Campaign getCampaign() {
        ArrayList<Survey> surveys = new ArrayList<>();
        surveys.add(getSurvey("2014-12-05"));
        surveys.add(getSurvey("2014-12-23"));
        surveys.add(getSurvey("2015-02-24"));
        return new Campaign("testios","MIT Voice and Depression study team", surveys);
    }
}
