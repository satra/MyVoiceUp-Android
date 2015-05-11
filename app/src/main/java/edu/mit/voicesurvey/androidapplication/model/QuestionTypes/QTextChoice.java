package edu.mit.voicesurvey.androidapplication.model.QuestionTypes;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.model.Question;

public class QTextChoice extends Question {
    private ArrayList<String> choices;
    private int selectedAnswer;

    public QTextChoice(String promptId, String id, String questionText, ArrayList<String> choices) {
        super(promptId, id, questionText);
        this.choices = choices;
        selectedAnswer = -1;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getSelectedAnswer() { return selectedAnswer; }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public String getAnswer() {
        return (getSelectedAnswer()+1)+"";
    }
}
