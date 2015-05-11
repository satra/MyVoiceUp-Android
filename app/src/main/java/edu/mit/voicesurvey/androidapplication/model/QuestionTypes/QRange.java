package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import edu.mit.voicesurvey.androidapplication.model.Question;

public class QRange extends Question {
    private int rangeMin;
    private int rangeMax;
    private double rangeStep;
    public QRange(String promptId, String id, String questionText, int rangeMin, int rangeMax, double rangeStep) {
        super(promptId, id, questionText);
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.rangeStep = rangeStep;
    }

    public int getRangeMin() {
        return rangeMin;
    }

    public int getRangeMax() {
        return rangeMax;
    }

    public double getRangeStep() {
        return rangeStep;
    }

    @Override
    public String getAnswer() {
        return null;
    }
}
