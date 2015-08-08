package com.reversedub.videostream;

import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class MediaRecorderWrapper {
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;

    public MediaRecorderWrapper(String fileName) {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/" + fileName;
    }

    public void onPause() {
        try
        {
            if (mRecorder != null) {
                mRecorder.release();
                mRecorder = null;
            }
        }
        catch (Exception ex)
        {
            Log.e("failed stop pausing:", ex.getStackTrace().toString());
            Log.e("failed stop pausing:", ex.getMessage());
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void onRecord(boolean start) {
        try {
            if (start) {
                startRecording();
            } else {
                stopRecording();
            }
        }
        catch (Exception ex)
        {
            Log.e("failed stop onrecord:", ex.getStackTrace().toString());
            Log.e("failed stop onrecord:", ex.getMessage());
        }
    }

    private void startRecording() {
        try
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
                Log.e("failed exception:", e.getMessage());
            }

            mRecorder.start();
        }
        catch (Exception ex)
        {
            Log.e("failed recording:", ex.getStackTrace().toString());
            Log.e("failed recording:", ex.getMessage());
        }
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        catch (Exception ex)
        {
            Log.e("failed stop recording:", ex.getStackTrace().toString());
            Log.e("failed stop recording:", ex.getMessage());
        }
    }
}
