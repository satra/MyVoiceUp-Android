package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QTextChoice;

/**
 * A simple fragment that displays a multiple choice question.
 */
public class QTextChoiceFragment extends Fragment {
    private QTextChoice question;
    private View rootView;

    /**
     * Returns a new instance of this fragment for the given question.
     */
    public static QTextChoiceFragment newInstance(Question question) {
        QTextChoiceFragment fragment = new QTextChoiceFragment();
        fragment.setQuestion(question);
        return fragment;
    }

    public QTextChoiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qtextchoice, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.question_text_text_choice_fragment);
        textView.setText(question.getQuestionText());
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.question_choice_list);

        for (String choice: question.getChoices()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layout.addView(getButtonForQuestion(choice),params);
        }
        return rootView;
    }

    /**
     * Creates a button given a choice
     * @param choice
     * @return
     */
    private View getButtonForQuestion(String choice) {
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button = new Button(getActivity());
        button.setText(choice);
        params.setMargins(0, 20, 0, 20);
        button.setLayoutParams(params);
        return button;
    }

    /**
     * Sets the question for this fragment
     *
     * @param question
     */
    private void setQuestion(Question question) {
        this.question = (QTextChoice) question;
    }
}