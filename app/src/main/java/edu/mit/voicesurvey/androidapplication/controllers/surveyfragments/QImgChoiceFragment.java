package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.mit.voicesurvey.androidapplication.R;

/**
 * A simple fragment that displays a slider question.
 */
public class QImgChoiceFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static QImgChoiceFragment newInstance(int sectionNumber) {
        QImgChoiceFragment fragment = new QImgChoiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public QImgChoiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qtextchoice, container, false);

        return rootView;
    }
}