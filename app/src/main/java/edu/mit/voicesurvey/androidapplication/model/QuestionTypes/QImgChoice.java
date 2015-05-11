package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import android.util.Pair;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.model.Question;

public class QImgChoice extends Question {
    private ArrayList<Pair<String, String>> choices;

    public QImgChoice(String promptId, String id, String questionText, ArrayList<Pair<String, String>> choices) {
        super(promptId, id, questionText);
        this.choices = choices;
    }

    public ArrayList<Pair<String, String>> getChoices() {
        return choices;
    }

    @Override
    public String getAnswer() {
        return null;
    }
}
