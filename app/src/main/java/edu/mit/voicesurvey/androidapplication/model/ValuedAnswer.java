package edu.mit.voicesurvey.androidapplication.model;

/**
 * Created by Ashley on 2/22/2015.
 */
public class ValuedAnswer<T> extends Answer{
    private T value;
    public ValuedAnswer(String questionId, T value) {
        super(questionId);
        this.value = value;
    }
    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ValuedAnswer && ((ValuedAnswer)other).getValue().equals(value) && super.equals(other);
    }

}
