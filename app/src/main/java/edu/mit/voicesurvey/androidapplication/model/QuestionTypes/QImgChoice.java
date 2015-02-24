package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import android.util.Pair;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.model.Question;

/**
 * Created by Ashley on 2/22/2015.
 */
public class QImgChoice extends Question {
    private ArrayList<Pair<String, String>> choices;

    public QImgChoice(String id, String questionText, boolean skippable, ArrayList<Pair<String, String>> choices) {
        super(id, questionText, skippable);
        this.choices = choices;
    }

    public ArrayList<Pair<String, String>> getChoices() {
        return choices;
    }

    @Override
    public boolean equals(Object other) {
        if (!(super.equals(other) && other instanceof QImgChoice) || ((QImgChoice) other).getChoices().size() != choices.size()) {
            return false;
        }
        for (int i = 0; i < choices.size(); i++) {
            if (!((QImgChoice) other).getChoices().get(i).equals(choices.get(i))) {
                return false;
            }
        }
        return true;
    }
}
