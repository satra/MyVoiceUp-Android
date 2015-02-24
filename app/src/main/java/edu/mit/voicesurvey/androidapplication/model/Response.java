package edu.mit.voicesurvey.androidapplication.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashley on 2/22/2015.
 */
public class Response {
    private Location location;
    private String surveyId;
    private Date timestamp;
    private ArrayList<Answer> answers;

    public Response(Location location, String surveyId) {
        this.location = location;
        this.surveyId = surveyId;
        this.timestamp = new Date();
        answers = new ArrayList<Answer>();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
    public Answer answerForQuestion(Question question) {
        for (Answer answer : answers) {
            if (answer.getQuestionId().equals(question.getId())) {
                return answer;
            }
        }
        return null;
    }

    public Location getLocation() {
        return location;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Response && ((Response)other).getLocation().equals(location) && ((Response) other).getSurveyId().equals(surveyId) && ((Response) other).getTimestamp().equals(timestamp) && ((Response) other).getAnswers().equals(answers);
    }
}
