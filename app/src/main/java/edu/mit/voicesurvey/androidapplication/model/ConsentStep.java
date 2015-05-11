package edu.mit.voicesurvey.androidapplication.model;

public class ConsentStep {
    public String title = "";
    public String description = "";
    public String image = "";
    public String additionalInfo = "";

    public ConsentStep(String t, String d, String i, String aI) {
        title = t;
        description = d;
        image = i;
        additionalInfo = aI;
    }
}
