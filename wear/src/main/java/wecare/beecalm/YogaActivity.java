package wecare.beecalm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wecare.beecalm.SimpleGestureFilter.SimpleGestureListener;

/**
 * Created by Zhuosi on 4/20/16.
 */
public class YogaActivity extends Activity implements SimpleGestureListener {

    private SimpleGestureFilter detector;
    private DataContainer dc;
    private static String activityName = "Yoga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
        dc = DataContainer.getInstance();
        ListView listView = (ListView) findViewById(R.id.listView_yoga);
        // Detect touched area
        detector = new SimpleGestureFilter(this,this);

        List<HashMap<String,String>> list = dc.getYogaHash();
        int[] to = {R.id.imageView_yoga};
        String[] from = {"image"};
        Log.d("yoga list", String.valueOf(list));
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.yoga_list_view_layout, from, to){
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundResource(dc.getYogaColor(position));
                return view;
            }
        };
        listView.setAdapter(adapter);
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
