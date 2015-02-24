package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.model.Question;

/**
 * Created by Ashley on 2/22/2015.
 */
public class QTextChoice extends Question {
    private ArrayList<String> choices;
    public QTextChoice(String id, String questionText, boolean skippable, ArrayList<String> choices) {
        super(id, questionText, skippable);
        this.choices = choices;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    @Override
    public boolean equals(Object other) {
        if (!(super.equals(other) && other instanceof QTextChoice) || ((QTextChoice) other).getChoices().size() != choices.size()) {
            return false;
        }
        for (int i = 0; i < choices.size(); i++) {
            if (!((QTextChoice) other).getChoices().get(i).equals(choices.get(i))) {
                return false;
            }
        }
        return true;
    }

}
