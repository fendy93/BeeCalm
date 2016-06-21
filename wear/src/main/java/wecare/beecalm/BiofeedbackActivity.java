package wecare.beecalm;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import wecare.beecalm.SimpleGestureFilter.SimpleGestureListener;

/**
 * Created by Zhuosi on 4/20/16.
 */
public class BiofeedbackActivity extends Activity implements SimpleGestureListener, SensorEventListener {

    private SimpleGestureFilter detector;
    private TextView heartrate;
    private DataContainer dc;
    private static String activityName = "Biofeedback (Watch Only)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);

        // Detect touched area
        detector = new SimpleGestureFilter(this,this);
        dc = DataContainer.getInstance();

        heartrate = (TextView) findViewById(R.id.heartrate);
        SensorManager mSensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    ///for heartbeat sensor
    @Override
    public void onSensorChanged(SensorEvent event) {
        String msg = "" + (int)event.values[0];
        heartrate.setText(msg);
        Log.d("T", msg);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
