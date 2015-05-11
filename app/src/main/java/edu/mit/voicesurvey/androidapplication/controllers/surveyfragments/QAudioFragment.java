package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;

/**
 * A simple fragment that displays an audio question.
 */
public class QAudioFragment extends Fragment {
    private static String mFileName = null;
    private QAudioRecording question;
    private TextView timeTextView;
    private ProgressBar progressBar;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    private boolean recording = false;
    private boolean playing = false;
    private int recordTime = 0;

    private Runnable playTimer;
    private Runnable recordTimer;
    private Handler handler;

    /**
     * Returns a new instance of this fragment for the given question.
     */
    public static QAudioFragment newInstance(Question question) {
        QAudioFragment fragment = new QAudioFragment();
        fragment.setQuestion(question);
        return fragment;
    }

    public QAudioFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qaudio, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.question_text_audio_fragment);
        textView.setText(question.getQuestionText());
        timeTextView = (TextView) rootView.findViewById(R.id.time_text_view);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        ImageButton recordButton = (ImageButton) rootView.findViewById(R.id.record_button);
        ImageButton playButton = (ImageButton) rootView.findViewById(R.id.play_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                record();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                play();
            }
        });

        handler = new Handler();
        playTimer = new Runnable() {
            @Override
            public void run() {
                timeTextView.setText(millisecondsToString(mPlayer.getCurrentPosition()));
                if (playing && !mPlayer.isPlaying()) {
                    play();
                } else {
                    if (mPlayer.getCurrentPosition() > recordTime) {
                        stopPlaying();
                    } else {
                        progressBar.incrementProgressBy(1000);
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };
        recordTimer = new Runnable() {
            @Override
            public void run() {
                recordTime += 1000;
                timeTextView.setText(millisecondsToString(recordTime));
                handler.postDelayed(this, 1000);
            }
        };

        return rootView;
    }

    /**
     * Changes milliseconds to the form m:ss
     *
     * @param milliseconds
     * @return a String of the form m:ss
     */
    private String millisecondsToString(int milliseconds) {
        int seconds = (int) (milliseconds / 1000 + 0.5);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String strSeconds = seconds / 10 == 0 ? "0" : "";
        strSeconds += seconds;
        return minutes + ":" + strSeconds;
    }

    /**
     * Sets the question for this fragment
     *
     * @param question
     */
    private void setQuestion(Question question) {
        this.question = (QAudioRecording) question;
    }

    /**
     * If the app is currently recording, it stops recording.
     * Else, it starts recording.
     */
    private void record() {
        if (recording) stopRecording();
        else startRecording();
    }

    /**
     * If the app is currently playing, it pauses.
     * Else, it starts playing.
     */
    private void play() {
        if (playing) stopPlaying();
        else startPlaying();
    }

    /**
     * Start playing the recorded file
     */
    private void startPlaying() {
        playing = true;
        progressBar.setProgress(0);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            handler.postDelayed(playTimer, 1000);
        } catch (IOException e) {
            playing = false;
        }
    }

    /**
     * Stop playing the recorded file
     */
    private void stopPlaying() {
        playing = false;
        handler.removeCallbacks(playTimer);
        mPlayer.release();
        mPlayer = null;
    }

    /**
     * Start recording
     */
    private void startRecording() {
        recording = true;
        recordTime = 0;
        validateMicAvailability();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            recording = false;
        }

        mRecorder.start();
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        timeTextView.setText(millisecondsToString(0));
        handler.postDelayed(recordTimer, 1000);
    }

    /**
     * Stop recording
     */
    private void stopRecording() {
        recording = false;
        mRecorder.stop();
        handler.removeCallbacks(recordTimer);
        mRecorder.release();
        mRecorder = null;
        progressBar.setIndeterminate(false);
        progressBar.setMax(recordTime);
    }

    /**
     * Checks to make sure the phone's mic is available
     */
    private void validateMicAvailability() {
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try {
            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                recorder.stop();
            }
            recorder.stop();
        } finally {
            recorder.release();
        }
    }
}