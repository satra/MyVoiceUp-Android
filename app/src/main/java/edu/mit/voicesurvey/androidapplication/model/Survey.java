package edu.mit.voicesurvey.androidapplication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;

public class Survey {
    private String uniqueId;
    private String id;
    private String date;
    private ArrayList<Question> questions;

    public Survey(String id, String date, ArrayList<Question> questions) {
        this.id = id;
        this.date = date;
        this.questions = questions;
        this.uniqueId = UUID.randomUUID().toString();
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public JSONObject getSurveyForUpload() throws JSONException {
        long time = System.currentTimeMillis();
        JSONObject survey = new JSONObject();
        survey.put("survey_key", uniqueId);
        survey.put("time", time);
        survey.put("timezone", "GMT");
        survey.put("location_status", "unavailable");
        JSONObject surveyLaunchContext = new JSONObject();
        surveyLaunchContext.put("launch_time", time);
        surveyLaunchContext.put("launch_timezone", "GMT");
        surveyLaunchContext.put("active_triggers", new JSONArray());
        survey.put("survey_launch_context", surveyLaunchContext);
        survey.put("survey_id", id);
        JSONArray responses = new JSONArray();
        survey.put("responses", responses);
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            JSONObject response = new JSONObject();
            String number = "0";
            if (i < 9) {
                number += "0";
            }
            number += (i + 1);
            String id = "iPromptNumericId" + q.getId() + "_" + q.getPromptId() + "_iSurvey0" + date + "_iPrompt" + number;
            response.put("prompt_id", id);
            if (q.getAnswer() == null || q.getAnswer().equals("0")) {
                response.put("value", "SKIPPED");
            } else {
                response.put("value", q.getAnswer());
            }
            responses.put(response);
        }
        return survey;
    }

    public String getAudioUUID1() {
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            if (q instanceof QAudioRecording) {
                if (((QAudioRecording) q).recorded) {
                    return ((QAudioRecording) q).getUUID();
                }
            }
        }
        return null;
    }

    public String getAudioUUID2() {
        boolean found1 = false;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            if (q instanceof QAudioRecording) {
                if (((QAudioRecording) q).recorded) {
                    if (found1)
                        return ((QAudioRecording) q).getUUID();
                    else
                        found1 = true;
                }
            }
        }
        return null;
    }
}
