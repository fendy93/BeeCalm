package wecare.beecalm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import wecare.beecalm.SimpleGestureFilter.SimpleGestureListener;

/**
 * Created by Zhuosi on 4/20/16.
 */
public class MantrasActivity extends Activity implements SimpleGestureListener {

    private SimpleGestureFilter detector;
    private DataContainer dc;
    private static String activityName = "Mantras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantras);
        dc = DataContainer.getInstance();

        // Detect touched area
        detector = new SimpleGestureFilter(this,this);

        ListView listView = (ListView) findViewById(R.id.listView_mantras);
        List<HashMap<String,String>> list = dc.getMantrasHash();
        int[] to = {R.id.txt_mantras};
        String[] from = {"mantra"};
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.mantras_list_view_layout, from, to){
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                View v = convertView;
                if(v== null){
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v=vi.inflate(R.layout.mantras_list_view_layout, null);
                }

                TextView txtTitleMeeting = (TextView) v.findViewById(R.id.txt_mantras);
                switch(position % 3){
                    case 0 :
                        txtTitleMeeting.setTextColor(Color.parseColor("#C77B00"));
                        break;
                    case 1 :
                        txtTitleMeeting.setTextColor(Color.parseColor("#1FC6C4"));
                        break;
                    case 2 :
                        txtTitleMeeting.setTextColor(Color.parseColor("#FBDB00"));
                }

                v.setBackgroundResource(dc.getColor(position));

                return v;
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
