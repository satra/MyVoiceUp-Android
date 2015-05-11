package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import edu.mit.voicesurvey.androidapplication.model.Question;

public class QAudioRecording extends Question {

    public QAudioRecording(String promptId, String id, String questionText){
        super(promptId, id, questionText);
    }

    @Override
    public String getAnswer() {
        return null;
    }
}
