package edu.mit.voicesurvey.androidapplication.model;

/**
 * Created by Ashley on 2/22/2015.
 */
public class Answer {
    private String questionId;
    public Answer(String questionId) {
        this.questionId = questionId;
    }
    public String getQuestionId() {
        return questionId;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Answer && ((Answer)other).getQuestionId().equals(questionId);
    }
}
