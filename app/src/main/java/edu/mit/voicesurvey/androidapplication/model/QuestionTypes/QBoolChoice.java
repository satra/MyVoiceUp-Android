package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import edu.mit.voicesurvey.androidapplication.model.Question;

/**
 * Created by Ashley on 2/22/2015.
 */
public class QBoolChoice extends Question {
    public static final String TRUE_CHOICE = "Yes";
    public static final String FALSE_CHOICE = "No";

    public QBoolChoice(String id, String questionText, boolean skippable){
        super(id, questionText, skippable);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && other instanceof QBoolChoice;
    }
}
