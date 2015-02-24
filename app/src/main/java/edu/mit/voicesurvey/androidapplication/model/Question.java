package edu.mit.voicesurvey.androidapplication.model;

/**
 * Created by Ashley on 2/22/2015.
 */
public class Question {
    private String id;
    private String questionText;
    private boolean skippable = false;

    public Question(String id, String questionText, boolean skippable) {
        this.id = id;
        this.questionText = questionText;
        this.skippable = skippable;
    }

    public String getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean isSkippable() {
        return skippable;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Question && ((Question)other).getId().equals(id) && ((Question) other).getQuestionText().equals(questionText) && ((Question) other).isSkippable()==(skippable);
    }
}
