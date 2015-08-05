package com.example.srdaruru.videostream;

import android.media.MediaRecorder;
import android.net.Uri;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    private MediaController mediaController;
    private boolean audioPlayToggle = false;
    private VideoViewWrapper videoViewWrapper = null;
    private MediaRecorderWrapper mediaRecorderWrapper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        //String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        String vidAddress = "reversedub/video.mp4";
        videoViewWrapper = new VideoViewWrapper(vidView, vidAddress);

        if(!audioPlayToggle) {
            //videoViewWrapper.FirstRender();
        }

        mediaRecorderWrapper = new MediaRecorderWrapper("reversedub/audioout.3gp");

        final Button videoButton = (Button) findViewById(R.id.controlBtn);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayToggle = !audioPlayToggle;
                if (audioPlayToggle) {
                    mediaRecorderWrapper.onRecord(audioPlayToggle);
                    videoViewWrapper.onPlay(audioPlayToggle);
                    videoButton.setText("Cancel");
                } else {
                    mediaRecorderWrapper.onRecord(audioPlayToggle);
                    videoViewWrapper.onPlay(audioPlayToggle);
                    videoButton.setText("Record");
                }
            }
        });
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
}
