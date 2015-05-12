package edu.mit.voicesurvey.androidapplication.controllers.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.LoginActivity;
import edu.mit.voicesurvey.androidapplication.controllers.registrationflow.EligibilityActivity;
import edu.mit.voicesurvey.androidapplication.model.data.PreviewInformation;


public class PreviewActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections of the preview. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    PreviewSectionsPagerAdapter previewSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the sections of the preview, one at a time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreviewInformation.init(this);
        setContentView(R.layout.activity_preview);
        TextView tv = (TextView) findViewById(R.id.header);
        tv.setText(Html.fromHtml("<h1>Welcome to VoiceUp</h1> <h3>A Depression Research Study</h3>"));

        // Create the adapter that will return a fragment for each of the sections.
        previewSectionsPagerAdapter = new PreviewSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(previewSectionsPagerAdapter);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circles);
        circlePageIndicator.setViewPager(mViewPager);
    }

    public void join(View view) {
        Intent intent = new Intent(PreviewActivity.this, EligibilityActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(PreviewActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the
     * sections.
     */
    public static class PreviewSectionsPagerAdapter extends FragmentPagerAdapter {

        public PreviewSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new PreviewSectionFragment();
            Bundle args = new Bundle();
            args.putInt(PreviewSectionFragment.ARG_SECTION_NUMBER, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return PreviewInformation.previewList.size();
        }
    }

    /**
     * A fragment representing a section of the consent form.
     */
    public static class PreviewSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_preview, container, false);
            Bundle args = getArguments();
            int id = args.getInt(ARG_SECTION_NUMBER);
            String text = PreviewInformation.previewList.get(id);
            ((TextView) rootView.findViewById(R.id.preview_text)).setText(Html.fromHtml(text));
            return rootView;
        }
    }
}
