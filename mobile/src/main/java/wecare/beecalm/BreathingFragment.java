package wecare.beecalm;

import android.media.MediaPlayer;
import android.net.Uri;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Zhuosi on 4/16/16.
 */
public class BreathingFragment  extends ListFragment {
    private DataContainer dc;
    private static String activityName = "Breathing";
    private String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.breathing_view, container, false);

        MediaController mc= new MediaController(getActivity());

        VideoView view = (VideoView)rootView.findViewById(R.id.videoView);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.breathing;
        view.setVideoURI(Uri.parse(path));
        view.setMediaController(mc);
        view.start();

        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        dc = DataContainer.getInstance();

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
        return rootView;
    }
}

