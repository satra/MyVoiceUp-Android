package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

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
import edu.mit.voicesurvey.androidapplication.model.data.ConsentInformation;
import edu.mit.voicesurvey.androidapplication.model.ConsentStep;

public class ConsentActivity extends FragmentActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections of the consent form. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    ConsentFormSectionsPagerAdapter mConsentFormSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the sections of the consent form, one at a time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConsentInformation.init(this);
        setContentView(R.layout.activity_consent);

        // Create the adapter that will return a fragment for each of the sections of the consent
        // form.
        mConsentFormSectionsPagerAdapter = new ConsentFormSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mConsentFormSectionsPagerAdapter);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circles);
        circlePageIndicator.setViewPager(mViewPager);
    }

    /**
     * Killed the LearnMore button, so additional info is no longer needed and that activity is not
     * needed.  This was the layout in fragment_section_consent.xml
     *     <Button
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     android:text="Learn More"
     android:textSize="14sp"
     android:onClick="learnMore"/>
     * @param view
     */
    public void learnMore(View view) {
        int i = mViewPager.getCurrentItem();
        Intent intent = new Intent(this, ConsentStepDetailActivity.class);
        intent.putExtra(ConsentStepDetailActivity.STEP_DETAIL_VALUE, i);
        startActivity(intent);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the
     * sections of the consent form.
     */
    public static class ConsentFormSectionsPagerAdapter extends FragmentPagerAdapter {

        public ConsentFormSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            int count = ConsentInformation.consentStepList.size();
            if (i == count) {
                return new ConsentFormReviewFragment();
            } else {
                Fragment fragment = new ConsentFormSectionFragment();
                Bundle args = new Bundle();
                args.putInt(ConsentFormSectionFragment.ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return ConsentInformation.consentStepList.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < ConsentInformation.consentStepList.size())
                return ConsentInformation.consentStepList.get(position).title;
            else if (position == ConsentInformation.consentStepList.size() + 1) {
                return "Review";
            }
            return "";
        }
    }

    /**
     * A fragment representing a section of the consent form.
     */
    public static class ConsentFormSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_consent, container, false);
            Bundle args = getArguments();
            int id = args.getInt(ARG_SECTION_NUMBER);
            ConsentStep consentStep = ConsentInformation.consentStepList.get(id);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(consentStep.title);
            ((TextView) rootView.findViewById(android.R.id.text2)).setText(Html.fromHtml(
                    consentStep.description));
            return rootView;
        }
    }

    /**
     * A fragment representing the review of the consent form.
     */
    public static class ConsentFormReviewFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_consent_review, container,
                    false);
            ((TextView)rootView.findViewById(R.id.review)).setText(Html.fromHtml(
                    ConsentInformation.fullConsentForm));
            return rootView;
        }
    }

    public void goToSignature(View view) {
        Intent intent = new Intent(ConsentActivity.this, ConsentSignatureActivity.class);
        startActivity(intent);
    }
}
