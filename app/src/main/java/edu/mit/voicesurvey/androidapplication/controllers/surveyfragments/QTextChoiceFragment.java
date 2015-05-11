package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QTextChoice;

/**
 * A simple fragment that displays a multiple choice question.
 */
public class QTextChoiceFragment extends Fragment {
    private QTextChoice question;
    private View rootView;
    private ArrayList<Button> choices;
    private Drawable base;

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
        choices = new ArrayList<>();
        for (int i=0; i < question.getChoices().size(); i++) {
            String choice = question.getChoices().get(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            Button b = getButtonForQuestion(choice, i);
            int SAGE = isSAGE(question);
            if (SAGE >= 0) {
                b = getSAGEButtonForQuestion(choice, i, SAGE);
            }
            choices.add(b);
            layout.addView(b,params);
        }
        base = choices.get(0).getBackground();
        if (question.getSelectedAnswer() != -1) {
            selectAnswer(question.getSelectedAnswer());
        }
        return rootView;
    }

    private int isSAGE(Question question) {
        if (question.getQuestionText().equals("How is your concentration today?")) {
            return 0;
        } else if (question.getQuestionText().equals("How is your energy today?")) {
            return 1;
        } else if (question.getQuestionText().equals("How is your mood today?")) {
            return 2;
        } else if (question.getQuestionText().equals("How did you sleep?")) {
            return 3;
        } else if (question.getQuestionText().equals("How much did you move today?")) {
            return 4;
        }
        return -1;
    }

    private Button getSAGEButtonForQuestion(String choice, final int ind, int SAGE) {
        int drawable = R.drawable.sage_cognition0;
        if (SAGE == 0) {
            if (ind == 0) {
                drawable = R.drawable.sage_cognition0;
            } else if (ind == 1) {
                drawable = R.drawable.sage_cognition1;
            } else if (ind == 2) {
                drawable = R.drawable.sage_cognition2;
            } else if (ind == 3) {
                drawable = R.drawable.sage_cognition3;
            } else if (ind == 4) {
                drawable = R.drawable.sage_cognition4;
            }
        } else if (SAGE == 1) {
            if (ind == 0) {
                drawable = R.drawable.sage_fatigue0;
            } else if (ind == 1) {
                drawable = R.drawable.sage_fatigue1;
            } else if (ind == 2) {
                drawable = R.drawable.sage_fatigue2;
            } else if (ind == 3) {
                drawable = R.drawable.sage_fatigue3;
            } else if (ind == 4) {
                drawable = R.drawable.sage_fatigue4;
            }
        } else if (SAGE == 2) {
            if (ind == 0) {
                drawable = R.drawable.sage_mood0;
            } else if (ind == 1) {
                drawable = R.drawable.sage_mood1;
            } else if (ind == 2) {
                drawable = R.drawable.sage_mood2;
            } else if (ind == 3) {
                drawable = R.drawable.sage_mood3;
            } else if (ind == 4) {
                drawable = R.drawable.sage_mood4;
            }
        } else if (SAGE == 3) {
            if (ind == 0) {
                drawable = R.drawable.sage_sleep0;
            } else if (ind == 1) {
                drawable = R.drawable.sage_sleep1;
            } else if (ind == 2) {
                drawable = R.drawable.sage_sleep2;
            } else if (ind == 3) {
                drawable = R.drawable.sage_sleep3;
            } else if (ind == 4) {
                drawable = R.drawable.sage_sleep4;
            }
        } else if (SAGE == 4) {
            if (ind == 0) {
                drawable = R.drawable.sage_exercise0;
            } else if (ind == 1) {
                drawable = R.drawable.sage_exercise1;
            } else if (ind == 2) {
                drawable = R.drawable.sage_exercise2;
            } else if (ind == 3) {
                drawable = R.drawable.sage_exercise3;
            } else if (ind == 4) {
                drawable = R.drawable.sage_exercise4;
            }
        }
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button = new Button(getActivity());
        button.setText(choice);
        params.setMargins(0, 20, 0, 20);
        button.setLayoutParams(params);
        button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable)getResources().getDrawable(drawable)).getBitmap(), 75, 75, true));
        button.setCompoundDrawablesWithIntrinsicBounds(d,null, null, null);
        button.setCompoundDrawablePadding(20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAnswer(ind);
            }
        });
        return button;
    }

    /**
     * Creates a button given a choice
     * @param choice
     * @return
     */
    private Button getButtonForQuestion(String choice, final int ind) {
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button = new Button(getActivity());
        button.setText(choice);
        params.setMargins(0, 20, 0, 20);
        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAnswer(ind);
            }
        });
        return button;
    }
    private void selectAnswer(int index) {
        if(question.getSelectedAnswer() != -1) {
            choices.get(question.getSelectedAnswer()).setBackground(base);
        }
        choices.get(index).setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
        question.setSelectedAnswer(index);
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