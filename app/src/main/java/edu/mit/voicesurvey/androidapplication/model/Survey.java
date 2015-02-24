package edu.mit.voicesurvey.androidapplication.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashley on 2/22/2015.
 */
public class Survey {
    private int id;
    private Date date;
    private String author;
    private ArrayList<Question> questions;

    public Survey(int id, Date date, String author, ArrayList<Question> questions) {
        this.id = id;
        this.date = date;
        this.author = author;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Survey) || ((Survey) other).getQuestions().size() != questions.size() ) {
            return false;
        }
        for (int i = 0; i < questions.size(); i++) {
            if (!((Survey) other).getQuestions().get(i).equals(questions.get(i))) {
                return false;
            }
        }
        return ((Survey)other).getId()==id && ((Survey) other).getDate().equals(date) && ((Survey) other).getAuthor().equals(author);
    }

}
