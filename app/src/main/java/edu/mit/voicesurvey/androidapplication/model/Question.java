package edu.mit.voicesurvey.androidapplication.model;

public abstract class Question {
    private String promptId;
    private String id;
    private String questionText;

    public Question(String promptId, String id, String questionText) {
        this.promptId = promptId;
        this.id = id;
        this.questionText = questionText;
    }

    public String getPromptId() {
        return promptId;
    }
    public String getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }
    public abstract String getAnswer();
}
