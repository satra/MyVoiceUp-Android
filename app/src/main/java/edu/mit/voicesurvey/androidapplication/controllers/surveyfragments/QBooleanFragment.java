package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QBoolChoice;

/**
 * A simple fragment that displays a boolean question.
 */
public class QBooleanFragment extends Fragment {
    private QBoolChoice question;

    /**
     * Returns a new instance of this fragment for the given question.
     */
    public static QBooleanFragment newInstance(Question question) {
        QBooleanFragment fragment = new QBooleanFragment();
        fragment.setQuestion(question);
        return fragment;
    }

    public QBooleanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qboolean, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.question_text_boolean_fragment);
        textView.setText(question.getQuestionText());
        return rootView;
    }

    /**
     * Sets the question for this fragment
     *
     * @param question
     */
    private void setQuestion(Question question) {
        this.question = (QBoolChoice) question;
    }
}