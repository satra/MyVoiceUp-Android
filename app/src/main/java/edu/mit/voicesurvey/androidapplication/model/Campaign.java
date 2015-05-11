package edu.mit.voicesurvey.androidapplication.model;

import java.util.ArrayList;

public class Campaign {
    private String campaignURN;
    private String campaignCreationTimestamp;
    private ArrayList<Survey> surveys;

    public Campaign (String campaignURN, String campaignCreationTimestamp, ArrayList<Survey> surveys) {
        this.campaignURN = campaignURN;
        this.campaignCreationTimestamp = campaignCreationTimestamp;
        this.surveys = surveys;
    }

    public String getCampaignURN() {
        return campaignURN;
    }

    public String getCampaignCreationTimestamp() {
        return campaignCreationTimestamp;
    }

    public ArrayList<Survey> getSurveys() { return surveys; }
}
