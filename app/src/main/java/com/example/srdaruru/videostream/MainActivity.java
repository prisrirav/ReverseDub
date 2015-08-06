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

public class MainActivity extends ActionBarActivity {

    private MediaController mediaController;
    private boolean audioPlayToggle = false;
    private VideoViewWrapper videoViewWrapper = null;
    private MediaRecorderWrapper mediaRecorderWrapper = null;
    Button videoButton = null;
    Intent mServiceIntent;
    Context context;

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

        context = this;
        String fileName = "reversedub/audioout.m4a";
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
                    //new StartMediaRecorderTask().execute(null, null, null);
                    /*mServiceIntent = new Intent(context, MediaRecorderService.class);
                    mServiceIntent.putExtra("Path", "reversedub/audioout7.3gp");
                    mServiceIntent.putExtra("RecordToggle", audioPlayToggle);
                    startService(mServiceIntent);*/
                    mediaRecorderWrapper.onRecord(audioPlayToggle);
                    videoViewWrapper.onPlay(audioPlayToggle);
                    videoButton.setText("Cancel");
                } else {
                   /* mServiceIntent = new Intent(context, MediaRecorderService.class);
                    mServiceIntent.putExtra("Path", "reversedub/audioout7.3gp");
                    mServiceIntent.putExtra("RecordToggle", "false");
                    startService(mServiceIntent);*/
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
                    /*mServiceIntent = new Intent(context, MediaRecorderService.class);
                    mServiceIntent.putExtra("Path", "reversedub/audioout6.3gp");
                    mServiceIntent.putExtra("RecordToggle", "false");
                    startService(mServiceIntent);*/
                    videoButton.setText("Merge");
                    mediaRecorderWrapper.onPlay(true);
                }
            }
        );
    }

    private class StartMediaRecorderTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... voids) {
            if (mediaRecorderWrapper != null) {
                mediaRecorderWrapper.onRecord(true);
                return true;
            }

            return false;
        }


        protected void onPostExecute(Boolean result) {
            if (videoButton != null) {
                videoButton.setText("Cancel");
            }
        }
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
    }
}
