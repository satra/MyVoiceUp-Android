package edu.mit.voicesurvey.androidapplication.model;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ashley on 2/24/2015.
 */
public class DataHolder {
    private Campaign campaign;

    public Campaign getCampaign() {
        return campaign;
    }

    private static final DataHolder holder = new DataHolder();
    private DataHolder() {
        campaign = JsonParser.parseCampaign(this);
    }
    public static DataHolder getInstance() {
        return holder;
    }

    /**
     * Finds the survey to be answered today
     * TODO: update this to handle skipping questions and surveys
     * @return
     */
    public Survey getTodaysSurvey() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        for (Survey s : campaign.getSurveys()) {
            Calendar surveyDate = Calendar.getInstance();
            surveyDate.setTime(s.getDate());
            if (surveyDate.equals(today))
                return s;
        }
        return null;
    }
}
