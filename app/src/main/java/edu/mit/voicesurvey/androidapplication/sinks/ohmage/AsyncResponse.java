package edu.mit.voicesurvey.androidapplication.sinks.ohmage;

public interface AsyncResponse {
    public static final int LOGIN = 0;
    public static final int FORGOT_PASSWORD = 1;
    public static final int UPLOAD_SURVEY = 2;
    public static final int DOWNLOAD_CAMPAIGN = 3;
    public static final int DOWNLOAD_LATEST_CAMPAIGN_FILE_NAME = 4;

    void processFinish(int method, boolean success, String error);
}
