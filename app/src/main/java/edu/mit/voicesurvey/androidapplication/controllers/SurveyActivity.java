package edu.mit.voicesurvey.androidapplication.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QAudioFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QBooleanFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QImgChoiceFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QRangeFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QTextChoiceFragment;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;
import edu.mit.voicesurvey.androidapplication.model.Survey;
import edu.mit.voicesurvey.androidapplication.model.data.CampaignInformation;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.OhmageClient;


public class SurveyActivity extends ActionBarActivity implements AsyncResponse {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    Survey survey;
    ArrayList<Fragment> fragments;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        next = (Button) findViewById(R.id.next);

        CampaignInformation.init();
        boolean pastSurvey = getIntent().getBooleanExtra("PAST", false);
        if (pastSurvey) {
            survey = CampaignInformation.getMissedSurvey(this);
        } else {
            survey = CampaignInformation.getTodaysSurvey(this);
        }
        if (survey == null) {
            finish();
        }

        fragments = new ArrayList<>();
        for (Question question : survey.getQuestions()) {
            Fragment fragment;
            if (question instanceof QAudioRecording) {
                fragment = QAudioFragment.newInstance(question);
            } else if (question instanceof QBoolChoice) {
                fragment = QBooleanFragment.newInstance(question);
            } else if (question instanceof QImgChoice) {
                fragment = QImgChoiceFragment.newInstance(question);
            } else if (question instanceof QRange) {
                fragment = QRangeFragment.newInstance(question);
            } else {
                fragment = QTextChoiceFragment.newInstance(question);
            }
            fragments.add(fragment);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // do nothing
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mViewPager.getAdapter().getCount() - 1) {
                    next.setText("Submit");
                } else if (position == mViewPager.getAdapter().getCount() - 2) {
                    next.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // do nothing
            }
        });
    }

    @Override
    public void processFinish(int method, boolean success, String error) {
        if (method == AsyncResponse.UPLOAD_SURVEY) {
            GregorianCalendar today = new GregorianCalendar();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String date = formatter.format(today.getTime());
            if (success || error.equals("No internet")) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                int numQuestions = sharedPreferences.getInt(getString(R.string.num_questions), 0) + 1;
                int numDays = sharedPreferences.getInt(getString(R.string.num_days), 0) + 1;
                sharedPreferences.edit().putInt(getString(R.string.num_questions), numQuestions).commit();
                sharedPreferences.edit().putInt(getString(R.string.num_days), numDays).commit();
                sharedPreferences.edit().putString("LAST_DATE", date).commit();
                sharedPreferences.edit().putBoolean(date.substring(0, 8) + survey.getDate(), true).commit();
                finish();
            }
            if (!success) {
                next.setEnabled(true);
                if (error.equals("No internet")) {
                    error = "No internet. Will save survey for later submission";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(error);
                builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // close dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                String date1 = date.substring(0, 8) + survey.getDate();
                try {
                    JSONArray array = new JSONArray();
                    array.put(survey.getSurveyForUpload());
                    String campaignURN = CampaignInformation.campaign.getCampaignURN();
                    String campaignCreationTimestamp = CampaignInformation.campaign.getCampaignCreationTimestamp();
                    String responses = array.toString();
                    String audioUUID1 = survey.getAudioUUID1();
                    String audioUUID2 = survey.getAudioUUID2();
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(date1 + "-campaign", campaignURN);
                    editor.putString(date1 + "-date", campaignCreationTimestamp);
                    editor.putString(date1 + "-responses", responses);
                    if (audioUUID1 != null)
                        editor.putString(date1 + "-audioFileUUID1", audioUUID1);
                    if (audioUUID2 != null)
                        editor.putString(date1 + "-audioFileUUID2", audioUUID2);
                    String surveyList = sharedPreferences.getString("SURVEY_LIST", "[]");
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String[] surveyArr = gson.fromJson(surveyList, String[].class); // list of survey dates
                    List<String> surveyList1 = new ArrayList<String>(Arrays.asList(surveyArr));
                    surveyList1.add(date1);
                    surveyArr = new String[surveyList1.size()];
                    surveyArr = surveyList1.toArray(surveyArr);
                    String value = gson.toJson(surveyArr);
                    editor.putString("SURVEY_LIST", value);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void nextPage(View view) {
        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
            try {
                JSONArray array = new JSONArray();
                array.put(survey.getSurveyForUpload());
                String campaignURN = CampaignInformation.campaign.getCampaignURN();
                String campaignCreationTimestamp = CampaignInformation.campaign.getCampaignCreationTimestamp();
                next.setEnabled(false);
                OhmageClient.uploadSurvey(campaignURN, campaignCreationTimestamp, array.toString(), this, survey.getAudioUUID1(), survey.getAudioUUID2());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return survey.getQuestions().size();
        }

    }
}
