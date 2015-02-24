package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import edu.mit.voicesurvey.androidapplication.model.Question;

/**
 * Created by Ashley on 2/22/2015.
 */
public class QAudioRecording extends Question {

    public QAudioRecording(String id, String questionText, boolean skippable){
        super(id, questionText, skippable);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && other instanceof QAudioRecording;
    }
}
