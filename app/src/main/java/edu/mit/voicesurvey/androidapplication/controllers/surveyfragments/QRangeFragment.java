package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QRange;

/**
 * A simple fragment that displays a slider question.
 */
public class QRangeFragment extends Fragment {
    private QRange question;
    private View rootView;

    /**
     * The TextView displaying the sliders current value.
     */
    private TextView seekValue;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static QRangeFragment newInstance(Question question) {
        QRangeFragment fragment = new QRangeFragment();
        fragment.setQuestion(question);
        return fragment;
    }

    public QRangeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qrange, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.question_text_slider_fragment);
        textView.setText(question.getQuestionText());
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekValue = (TextView) rootView.findViewById(R.id.seekValue);

        TextView lowTextView = (TextView) rootView.findViewById(R.id.low_value);
        TextView highTextView = (TextView) rootView.findViewById(R.id.high_value);
        lowTextView.setText(question.getRangeMin()+"");
        highTextView.setText(question.getRangeMax()+"");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                seekValue.setText(""+getSeekerValueFromProgressChanged(progressChanged));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                seekValue.setText(""+getSeekerValueFromProgressChanged(progressChanged));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                seekValue.setText(""+getSeekerValueFromProgressChanged(progressChanged));
            }
        });

        return rootView;
    }

    /**
     * Finds the seeker value given the range min, range max, and progress changed
     * @param progressChanged
     * @return
     */
    private int getSeekerValueFromProgressChanged(int progressChanged) {
        return ((int)((question.getRangeMax()-question.getRangeMin())*(progressChanged/100.0)) + question.getRangeMin());
    }

    /**
     * Sets the question for this fragment
     *
     * @param question
     */
    private void setQuestion(Question question) {
        this.question = (QRange)question;
    }
}