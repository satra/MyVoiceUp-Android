package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import java.util.UUID;

import edu.mit.voicesurvey.androidapplication.model.Question;

public class QAudioRecording extends Question {
    private String uniqueId;
    public boolean recorded;

    public QAudioRecording(String promptId, String id, String questionText){
        super(promptId, id, questionText);
        uniqueId = UUID.randomUUID().toString();
        recorded = false;
    }

    public String getUUID() { return uniqueId; }
    @Override
    public String getAnswer() {
        if (recorded)return uniqueId;
        return "SKIPPED";
    }
}
