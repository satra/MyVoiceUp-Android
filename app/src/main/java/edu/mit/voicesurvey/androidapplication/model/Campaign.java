package edu.mit.voicesurvey.androidapplication.model;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ashley on 2/22/2015.
 */
public class Campaign {
    private String id;
    private String author;

    private ArrayList<Survey> surveys;

    //var sink sinksetup????

    public Campaign (String id, String author, ArrayList<Survey> surveys) {
        this.id = id;
        this.author = author;
        this.surveys = surveys;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<Survey> getSurveys() { return surveys; }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Campaign) || ((Campaign) other).getSurveys().size() != surveys.size()) {
            return false;
        }
        for (int i=0; i < surveys.size(); i++) {
            if (! (((Campaign) other).getSurveys().get(i).equals(surveys.get(i)))) {
                return false;
            }
        }
        return ((Campaign)other).getId().equals(id) && ((Campaign) other).getAuthor().equals(author);
    }
}
