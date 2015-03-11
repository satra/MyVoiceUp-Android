package edu.mit.voicesurvey.androidapplication.controllers.surveyfragments;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.model.Question;
import edu.mit.voicesurvey.androidapplication.model.QuestionTypes.QAudioRecording;

/**
 * A simple fragment that displays an audio question.
 * FIXME: I was in the process of debugging when I realized that MediaRecorder can't record wav files; working on finding a solution right now
 */
public class QAudioFragment extends Fragment {
    private static final String LOG_TAG = "Audio Record Fragment";

    private static String mFileName = null;
    private QAudioRecording question;
    private View rootView;
    private TextView timeTextView;
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
        rootView = inflater.inflate(R.layout.fragment_qaudio, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.question_text_audio_fragment);
        textView.setText(question.getQuestionText());
        timeTextView = (TextView) rootView.findViewById(R.id.time_text_view);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp"; //TODO: change to record wav file

        ImageButton recordButton = (ImageButton) rootView.findViewById(R.id.record_button);
        ImageButton rewindButton = (ImageButton) rootView.findViewById(R.id.rewind_button);
        ImageButton playButton = (ImageButton) rootView.findViewById(R.id.play_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                record();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rewind();
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
                    handler.postDelayed(this, 1000);
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
     * TODO: Start playing from the beginning
     */
    private void rewind() {

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
    /*    playing = true;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            handler.postDelayed(playTimer, 0);
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        } */
    }

    /**
     * Stop playing the recorded file
     */
    private void stopPlaying() {
     /*   playing = false;
        handler.removeCallbacks(playTimer);
        mPlayer.release();
        mPlayer = null; */
    }

    /**
     * Start recording
     */
    private void startRecording() {
     /*   recording = true;
        validateMicAvailability();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
        timeTextView.setText(millisecondsToString(0));
        handler.postDelayed(recordTimer, 1000); */
    }

    /**
     * Stop recording
     */
    private void stopRecording() {
     /*   recording = false;
        mRecorder.stop();
        handler.removeCallbacks(recordTimer);
        recordTime = 0;
        mRecorder.release();
        mRecorder = null; */
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
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                Log.e(LOG_TAG, "Mic didn't successfully initialized");
            }

            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                recorder.stop();
                Log.e(LOG_TAG, "Mic is in use and can't be accessed");
            }
            recorder.stop();
        } finally {
            recorder.release();
        }
    }
}