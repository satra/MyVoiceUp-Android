package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import edu.mit.voicesurvey.androidapplication.model.Question;

/**
 * Created by Ashley on 2/22/2015.
 */
public class QRange extends Question {
    private int rangeMin;
    private int rangeMax;
    private double rangeStep;
    public QRange(String id, String questionText, boolean skippable, int rangeMin, int rangeMax, double rangeStep) {
        super(id, questionText, skippable);
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
    public boolean equals(Object other) {
        return super.equals(other) && other instanceof QRange && ((QRange)other).getRangeMin() == rangeMin && ((QRange)other).getRangeMax() == rangeMax && ((QRange)other).getRangeStep() == rangeStep;
    }

}
