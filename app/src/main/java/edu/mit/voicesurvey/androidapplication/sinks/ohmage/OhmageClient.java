package edu.mit.voicesurvey.androidapplication.sinks.ohmage;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.POST;
import retrofit.http.Query;

public class OhmageClient {
    public static final String CLIENT = "ohmage-android";
    private static final String API_URL = "http://voiceapp.mit.edu";
    public static String authToken;
    public static Ohmage ohmage;
    private static boolean isWorking = false;
    private static boolean initialized = false;
    private static AsyncResponse delegate = null; // Callback interface
    static Callback<Token> tokenCallback = new Callback<Token>() {

        @Override
        public void success(Token token, Response response) {
            authToken = token.token;
            if (delegate != null) {
                if (token.result.equals("failure")) {
                    delegate.processFinish(AsyncResponse.LOGIN, false, token.errors.get(0).text);
                } else {
                    delegate.processFinish(AsyncResponse.LOGIN, true, null);
                }
            }
            isWorking = false;
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            authToken = null;
            if (delegate != null) {
                delegate.processFinish(AsyncResponse.LOGIN, false, retrofitError.toString());
            }
            isWorking = false;
        }
    };
    static Callback<CommonResponse> resetPasswordCallback = new Callback<CommonResponse>() {

        @Override
        public void success(CommonResponse response, Response response2) {
            if (delegate != null) {
                if (response.result.equals("failure")) {
                    delegate.processFinish(AsyncResponse.FORGOT_PASSWORD, false, response.errors.get(0).text);
                } else {
                    delegate.processFinish(AsyncResponse.FORGOT_PASSWORD, true, null);
                }
            }
            isWorking = false;
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (delegate != null) {
                delegate.processFinish(AsyncResponse.FORGOT_PASSWORD, false, retrofitError.toString());
            }
            isWorking = false;
        }
    };

    static Callback<CommonResponse> uploadSurveyCallback = new Callback<CommonResponse>() {

        @Override
        public void success(CommonResponse response, Response response2) {
            if (delegate != null) {
                if (response.result.equals("failure")) {
                    delegate.processFinish(AsyncResponse.UPLOAD_SURVEY, false, response.errors.get(0).text);
                } else {
                    delegate.processFinish(AsyncResponse.UPLOAD_SURVEY, true, null);
                }
            }
            isWorking = false;
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (delegate != null) {
                delegate.processFinish(AsyncResponse.UPLOAD_SURVEY, false, retrofitError.toString());
            }
            isWorking = false;
        }
    };

    public static void init() {
        if (!initialized) {
            // Create a very simple REST adapter which points the Ohmage API endpoint.
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            // Create an instance of our Ohmage API interface.
            ohmage = restAdapter.create(Ohmage.class);
            initialized = true;
        }
    }

   /* public void uploadFile(String localFile, String repo) {
        if (!working) {
            init();
            working = true;
        }
    }*/

    public static void uploadSurvey(String campaign, String date, String responses, AsyncResponse caller) {
        if (!isWorking) {
            isWorking = true;
            init();
            delegate = caller;
            ohmage.uploadSurvey(authToken, CLIENT, campaign, date, responses, uploadSurveyCallback);
        }
    }

    public static void resetPassword(String username, String email, AsyncResponse caller) {
        if (!isWorking) {
            isWorking = true;
            init();
            delegate = caller;
            ohmage.resetPassword(username, email, resetPasswordCallback);
        }
    }

    public static void authorizeUser(String user, String password, AsyncResponse caller) {
        if (!isWorking) {
            isWorking = true;
            init();
            delegate = caller;
            ohmage.authorize(user, password, CLIENT, tokenCallback);
        }
    }

    interface Ohmage {
        @POST("/app/user/auth_token")
        void authorize(
                @Query("user") String user,
                @Query("password") String password,
                @Query("client") String client,
                Callback<Token> token
        );

        @POST("/app/user/reset_password")
        void resetPassword(
                @Query("username") String username,
                @Query("email_address") String email,
                Callback<CommonResponse> response
        );

        @POST("/app/survey/upload")
        void uploadSurvey(
                @Query("auth_token") String authToken,
                @Query("client") String client,
                @Query("campaign_urn") String campaignURN,
                @Query("campaign_creation_timestamp") String creationTimestamp,
                @Query("surveys") String surveys,
                Callback<CommonResponse> response
        );
    }

    public static class Token {
        String token;
        String result;
        List<Errors> errors;
    }

    public static class Errors {
        String text;
    }

    public static class CommonResponse {
        String result;
        List<Errors> errors;
    }
}
