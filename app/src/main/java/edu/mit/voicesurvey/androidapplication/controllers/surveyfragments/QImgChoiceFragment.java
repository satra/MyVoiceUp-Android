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
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QImgChoice;

/**
 * A simple fragment that displays an image choice question.
 */
public class QImgChoiceFragment extends Fragment {
    private QImgChoice question;
    private View rootView;

    /**
     * Returns a new instance of this fragment for the given question.
     */
    public static QImgChoiceFragment newInstance(Question question) {
        QImgChoiceFragment fragment = new QImgChoiceFragment();
        fragment.setQuestion(question);
        return fragment;
    }

    public QImgChoiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qimgchoice, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.question_text_img_choice_fragment);
        textView.setText(question.getQuestionText());

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.question_image_list);

        for (Pair<String, String> questionPair : question.getChoices()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layout.addView(getButtonForQuestion(questionPair),params);
        }
        return rootView;
    }

    /**
     * Creates a row containing an image and the choice
     * @param pair
     * @return
     */
    private View getButtonForQuestion(Pair<String, String> pair) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageButton image = new ImageButton(getActivity());
        image.setBackgroundResource(R.drawable.mit_logo);
        Button button = new Button(getActivity());
        button.setText(pair.second);
        params.setMargins(0, 20, 0, 20);
        image.setLayoutParams(params);
        button.setLayoutParams(params);
        layout.addView(image);
        layout.addView(button);
        return layout;
    }

    /**
     * Sets the question for this fragment
     *
     * @param question
     */
    private void setQuestion(Question question) {
        this.question = (QImgChoice)question;
    }
}