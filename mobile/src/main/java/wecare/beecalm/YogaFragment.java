package wecare.beecalm;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Zhuosi on 4/16/16.
 */
public class YogaFragment extends ListFragment {
    private String activityName = "Yoga";
    private DataContainer dc;
    private String title;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.yoga_view, container, false);
        dc = DataContainer.getInstance();

        List<HashMap<String,String>> list = dc.getYogaHash();
        int[] to = {R.id.txt, R.id.imageView};
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), list, R.layout.listview_layout, dc.from, to){
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
//                view.setBackgroundResource(dc.getYogaColor(position));
//                return view;
                View v = convertView;
                if(v== null){
                    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v=vi.inflate(R.layout.listview_layout, null);
                }

                TextView txtTitleMeeting = (TextView) v.findViewById(R.id.txt);

                switch(dc.getYogaTextColor(position)){
                    case 0 :
                        txtTitleMeeting.setTextColor(Color.parseColor("#C77B00"));
                        break;
                    case 1 :
                        txtTitleMeeting.setTextColor(Color.parseColor("#1FC6C4"));
                        break;
                    case 2 :
                        txtTitleMeeting.setTextColor(Color.parseColor("#FBDB00"));
                }
                v.setBackgroundResource(dc.getYogaColor(position));

                return v;
            }
        };
        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    ListFragment listfragment = null;
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > dc.SWIPE_MAX_OFF_PATH ||  Math.abs(velocityX) < dc.SWIPE_THRESHOLD_VELOCITY) {
                                return false;
                            }

                            if (e1.getX() - e2.getX() > dc.SWIPE_MIN_DISTANCE) {
                                title = dc.getNextActivity(activityName);
                            }else if (e2.getX() - e1.getX() > dc.SWIPE_MIN_DISTANCE){
                                title = dc.getPrevActivity(activityName);
                            }

                            listfragment = dc.getFragment(title);
                            if (listfragment != null) {
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, listfragment).commit();
                            }

                            // set the toolbar title
                            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        ListView layout = getListView();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

    }

//    @Override
//    public void onListItemClick(ListView l, View v, int pos, long id) {
//        super.onListItemClick(l, v, pos, id);
//        Toast.makeText(getActivity(), "Item " + pos + " was clicked", Toast.LENGTH_SHORT).show();
//    }

}