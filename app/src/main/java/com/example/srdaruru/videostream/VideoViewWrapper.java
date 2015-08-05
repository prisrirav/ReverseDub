package com.example.srdaruru.videostream;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.VideoView;

/**
 * Created by srdaruru on 8/4/2015.
 */
public class VideoViewWrapper {
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private VideoView vidView = null;

    public VideoViewWrapper(VideoView videoView, String fileName)
    {
        if (videoView == null)
        {
            return;
        }

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/" + fileName;
        vidView = videoView;
        String vidAddress = mFileName;
        /*Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);*/
        vidView.setVideoPath(mFileName);
    }

    public void FirstRender()
    {
        if(vidView == null) {
            return;
        }

        vidView.start();

        // To avoid blank screen in the beginning
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                vidView.pause();
            }
        }, 2000);
    }

    public void onPlay(boolean playState) {
        if(vidView == null)
        {
            return;
        }

        if (playState) {
            vidView.seekTo(0);
            vidView.start();
        } else {
            vidView.seekTo(0);
            vidView.pause();
        }
    }
}
