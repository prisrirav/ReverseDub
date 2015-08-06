package com.example.srdaruru.videostream;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends ActionBarActivity {

    private MediaController mediaController;
    private boolean audioPlayToggle = false;
    private VideoViewWrapper videoViewWrapper = null;
    private MediaRecorderWrapper mediaRecorderWrapper = null;
    Button videoButton = null;
    Intent mServiceIntent;
    Context context;
    String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        String vidAddress = "reversedub/video.mp4";
        videoViewWrapper = new VideoViewWrapper(vidView, vidAddress);

        if(!audioPlayToggle) {
            //videoViewWrapper.FirstRender();
        }

        context = this;
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String fileName = String.format("reversedub/audioout{0}.m4a", uuid);
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/" + fileName;
        File file = new File(mFileName);
        if(file.exists())
        {
            file.delete();
        }

        mediaRecorderWrapper = new MediaRecorderWrapper(fileName);

        videoButton = (Button) findViewById(R.id.controlBtn);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayToggle = !audioPlayToggle;
                if (audioPlayToggle) {
                    mediaRecorderWrapper.onRecord(audioPlayToggle);
                    videoViewWrapper.onPlay(audioPlayToggle);
                    videoButton.setText("Cancel");
                } else {
                    mediaRecorderWrapper.onRecord(false);
                    videoViewWrapper.onPlay(audioPlayToggle);
                    videoButton.setText("Record");
                    mediaRecorderWrapper.onPlay(true);
                }
            }
        });

        vidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaRecorderWrapper.onRecord(false);
                    videoButton.setText("Merge");
                    mediaRecorderWrapper.onPlay(true);
                }
            }
        );

        // Add muxing here after you click on merge
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        mediaRecorderWrapper.onPause();

        // enable the following after mux integration is done.
        /*if (mFileName != null)
        {
            File file = new File(mFileName);
            if(file.exists())
            {
                file.delete();
            }
        }*/
    }
}
