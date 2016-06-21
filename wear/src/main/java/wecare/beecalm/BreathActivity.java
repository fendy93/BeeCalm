package wecare.beecalm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wecare.beecalm.SimpleGestureFilter.SimpleGestureListener;
import io.vov.vitamio.Vitamio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Zhuosi on 4/20/16.
 */

public class BreathActivity extends Activity implements SimpleGestureListener {

    private SimpleGestureFilter detector;
    private DataContainer dc;
    private static String activityName = "Breathing";
    io.vov.vitamio.widget.VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);
        dc = DataContainer.getInstance();

        Vitamio.isInitialized(this);

        InputStream ins = this.getResources().openRawResource (R.raw.breathing_watch);
        File tmpFile = null;
        OutputStream output;

        try {
            tmpFile = File.createTempFile("video","mp4");
            output = new FileOutputStream(tmpFile);

            final byte[] buffer = new byte[102400];
            int read;

            while ((read = ins.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
            output.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mVideoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.videoView);

        mVideoView.setVideoPath(tmpFile.getPath());


        //Setting main focus on video view
        mVideoView.requestFocus();
        mVideoView.start();

        //Registering a callback to be invoked when the media file is loaded and ready to go.
        mVideoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(io.vov.vitamio.MediaPlayer arg0) {
                //Starting the player after getting information from url.
                arg0.setLooping(true);
            }
        });


        // Detect touched area
        detector = new SimpleGestureFilter(this,this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT :
                str = dc.getPrevActivity(activityName);
                break;
            case SimpleGestureFilter.SWIPE_LEFT :
                str = dc.getNextActivity(activityName);
                break;
        }
        Intent intent = dc.getIntent(this, str);
        startActivity(intent);
    }

    @Override
    public void onDoubleTap() {
    }

}
