package edu.mit.voicesurvey.androidapplication.controllers;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QAudioFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QBooleanFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QImgChoiceFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QRangeFragment;
import edu.mit.voicesurvey.androidapplication.controllers.surveyfragments.QTextChoiceFragment;
import edu.mit.voicesurvey.androidapplication.model.DataHolder;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QTextChoice;
import edu.mit.voicesurvey.androidapplication.model.Survey;


public class SurveyActivity extends ActionBarActivity {

    // TODO allow users to click next and back to go between questions
    // TODO when the user switches fragments, record their response to the question
    // TODO add progress bar to the top of the page
    // TODO end survey action

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        survey = DataHolder.getInstance().getTodaysSurvey();
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
