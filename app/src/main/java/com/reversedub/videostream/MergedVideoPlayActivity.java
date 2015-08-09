package com.reversedub.videostream;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;


public class MergedVideoPlayActivity extends ActionBarActivity {

    public static final String MERGED_FILE_KEYNAME = "MergedFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_video_play);
        Intent intent = getIntent();

        String vidAddress = intent.getStringExtra(MERGED_FILE_KEYNAME);
        //String vidAddress = "/sdcard/reversedub/videoTest.mp4";
        VideoView vidView = (VideoView)findViewById(R.id.mergedVideo);
        VideoViewWrapper videoViewWrapper = new VideoViewWrapper(vidView, vidAddress);
        videoViewWrapper.onPlay(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_merged_video_play, menu);
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
