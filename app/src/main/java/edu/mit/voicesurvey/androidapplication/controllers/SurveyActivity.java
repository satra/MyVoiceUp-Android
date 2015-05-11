package edu.mit.voicesurvey.androidapplication.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QAudioFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QBooleanFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QImgChoiceFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QRangeFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QTextChoiceFragment;
import edu.mit.voicesurvey.androidapplication.model.data.CampaignInformation;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;
import edu.mit.voicesurvey.androidapplication.model.Survey;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.AsyncResponse;
import edu.mit.voicesurvey.androidapplication.sinks.ohmage.OhmageClient;


public class SurveyActivity extends ActionBarActivity implements AsyncResponse{

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

        CampaignInformation.init(this);
        survey = CampaignInformation.getTodaysSurvey();
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
        if (success) {
            finish();
        } else {
            next.setEnabled(true);
            // TODO: display error to user
        }
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

    public void nextPage(View view) {
        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount()-1) {
            try {
                JSONArray array = new JSONArray();
                array.put(survey.getSurveyForUpload());
                String campaignURN = CampaignInformation.campaign.getCampaignURN();
                String campaignCreationTimestamp = CampaignInformation.campaign.getCampaignCreationTimestamp();
                next.setEnabled(false);
                OhmageClient.uploadSurvey(campaignURN, campaignCreationTimestamp, array.toString(), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }
}
