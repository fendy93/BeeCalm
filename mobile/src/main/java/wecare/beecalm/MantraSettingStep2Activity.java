package wecare.beecalm;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Zhuosi on 4/20/16.
 */
public class MantraSettingStep2Activity extends AppCompatActivity {
    private DataContainer dc;
    private String position;
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;
    private String outputFilePlay = null;
    private Button startBtn;
    private Button playBtn;
    private TextView text1;
    private long timeRemaining;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_setting_step2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mantra Settings");

        Intent intent = getIntent();
        dc = DataContainer.getInstance();

        position = intent.getStringExtra("position");
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/" + position +".3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);

        startBtn = (Button)findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (startBtn.getText().equals("start")) {
                    start(v);
                    isPaused = false;
                    startBtn.setText("stop");
                    timer();
                } else {
                    stop(v);
                    isPaused = true;
                    timer();
                    startBtn.setText("start");
                    if (dc.getRecordHash(Integer.parseInt(position)) == null) {
                        dc.saveRecordHash(Integer.parseInt(position), outputFile);
                    }
                }


            }
        });

        playBtn = (Button)findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
                playBtn.setEnabled(false);
                try {
                    Thread.sleep(2000);                 //1000 milliseconds is one second.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                stopPlay(v);
                playBtn.setEnabled(true);
            }
        });
    }

    public void timer() {
        text1=(TextView)findViewById(R.id.textView1);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(isPaused)
                {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                }
                else {
                    text1.setText("" + millisUntilFinished /2000);
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                }

            }

            public void onFinish() {
                text1.setText("0");
            }
        }.start();
    }

    public void start(View view){
        try {
            outputFile = Environment.getExternalStorageDirectory().
                    getAbsolutePath() + "/" + position +".3gpp";

            myRecorder = new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myRecorder.setOutputFile(outputFile);
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stop(View view){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    public void play(View view) {
        try{
            outputFilePlay = dc.getRecordHash(Integer.parseInt(position));
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(outputFilePlay);
            myPlayer.prepare();
            myPlayer.start();

            Toast.makeText(getApplicationContext(), "Start play the recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;

                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.side_bar, menu);
        super.onCreateOptionsMenu(menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        this.closeOptionsMenu();
        Intent intent = null;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                intent = new Intent(this, MantraSettingsActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }
}
