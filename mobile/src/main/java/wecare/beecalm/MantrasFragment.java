package wecare.beecalm;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Zhuosi on 4/16/16.
 */
public class MantrasFragment  extends ListFragment {
    private  DataContainer dc;
    private static String activityName = "Mantras";
    private View rootView;
    private String title;
    private List<HashMap<String,String>> mantra_record = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mantras_view, container, false);
        dc = DataContainer.getInstance();

        mantra_record = dc.getMantrasHash();
        int[] to = {R.id.txt};
        String[] from = {"mantra"};
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), mantra_record, R.layout.mantras_view_layout, from, to){
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundResource(dc.getColor(position));
                TextView txtTitleMeeting = (TextView) view.findViewById(R.id.txt);
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

                ImageView audio = (ImageView) view.findViewById(R.id.audio);
                if (dc.getRecordHash(position) != null){
                    switch(position % 3){
                        case 0 :
                            audio.setVisibility(View.VISIBLE);
                            audio.setImageResource(R.drawable.v0);
                            break;
                        case 1 :
                            audio.setVisibility(View.VISIBLE);
                            audio.setImageResource(R.drawable.v1);
                            break;
                        case 2 :
                            audio.setVisibility(View.VISIBLE);
                            audio.setImageResource(R.drawable.v2);
                    }
                } else {
                    switch(position % 3){
                        case 0 :
                            audio.setVisibility(View.GONE);
                            break;
                        case 1 :
                            audio.setVisibility(View.GONE);
                            break;
                        case 2 :
                            audio.setVisibility(View.GONE);
                    }
                }
                return view;
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

        @Override
    public void onListItemClick(ListView l, View v, final int pos, long id) {
            super.onListItemClick(l, v, pos, id);
            final ImageView audio = (ImageView) v.findViewById(R.id.audio);
            if (dc.getRecordHash(pos) != null){
                switch(pos % 3){
                    case 0 :
                        audio.setImageResource(R.drawable.nv0);
                        break;
                    case 1 :
                        audio.setImageResource(R.drawable.nv1);
                        break;
                    case 2 :
                        audio.setImageResource(R.drawable.nv2);
                }
            }


            MediaPlayer myPlayer;

            String audioFile = dc.getRecordHash(pos);
            try{
                myPlayer = new MediaPlayer();
                myPlayer.setDataSource(audioFile);
                myPlayer.prepare();
                myPlayer.start();
                myPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        switch (pos % 3) {
                            case 0:
                                audio.setVisibility(View.VISIBLE);
                                audio.setImageResource(R.drawable.v0);
                                break;
                            case 1:
                                audio.setVisibility(View.VISIBLE);
                                audio.setImageResource(R.drawable.v1);
                                break;
                            case 2:
                                audio.setVisibility(View.VISIBLE);
                                audio.setImageResource(R.drawable.v2);
                        }
                    }
                });

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }


}