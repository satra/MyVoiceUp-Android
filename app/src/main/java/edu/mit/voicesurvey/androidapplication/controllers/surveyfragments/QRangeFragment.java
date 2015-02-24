package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.mit.voicesurvey.androidapplication.R;

/**
 * A simple fragment that displays a slider question.
 */
public class QRangeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * The TextView displaying the sliders current value.
     */
    private TextView seekValue;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static QRangeFragment newInstance(int sectionNumber) {
        QRangeFragment fragment = new QRangeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public QRangeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qrange, container, false);

        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekValue = (TextView) rootView.findViewById(R.id.seekValue);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                seekValue.setText(progressChanged+"");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                seekValue.setText(progressChanged+"");
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                seekValue.setText(progressChanged+"");
            }
        });

        return rootView;
    }
}