package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import edu.mit.voicesurvey.androidapplication.model.Question;

public class QBoolChoice extends Question {
    public static final String TRUE_CHOICE = "Yes";
    public static final String FALSE_CHOICE = "No";

    public QBoolChoice(String promptId, String id, String questionText){
        super(promptId, id, questionText);
    }

    @Override
    public String getAnswer() {
        return null;
    }
}
