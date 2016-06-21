package wecare.beecalm;
        import android.app.FragmentManager;
        import android.app.ListFragment;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.GestureDetector;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Random;

/**
 * Created by Zhuosi on 4/16/16.
 */
public class SimonFragment  extends ListFragment {

    private ArrayList<Integer> pattern = new ArrayList<Integer>();
    private ArrayList<Integer> tempPattern = new ArrayList<Integer>();
    private float initialX, initialY, finalX, finalY;
    private ListFragment listfragment = null;
    private int counter = 1;
    private int MIN_DISTANCE = 150;
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private Random rand = new Random();
    private  DataContainer dc;
    private String title;
    private String activityName = "Simon Swipe";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.simon_view, container, false);

        one = (TextView) view.findViewById(R.id.one);
        two = (TextView) view.findViewById(R.id.two);
        three = (TextView) view.findViewById(R.id.three);
        four = (TextView) view.findViewById(R.id.four);

        dc = DataContainer.getInstance();

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        initialY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        finalX = event.getX();
                        finalY = event.getY();

                        if (initialX < finalX && finalX - initialX > MIN_DISTANCE) {
                            //left to right
                            //go back to previous screen
                            title = dc.getPrevActivity(activityName);
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


                        } else if (initialX > finalX && initialX - finalX > MIN_DISTANCE) {
                            //right to left
                            //go to next Ex
                            title = dc.getNextActivity(activityName);
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
                        } else {
                            //we'll consider it a tap
                            if (initialX < 530) {
                                if (initialY < 746) {
                                    //Quadrant1
                                    showColor(one, "#EBA938", "#FFCF7C", 200);
                                    if (tempPattern.get(0) != 1) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }

                                } else {
                                    //Quadrant 3
                                    showColor(three,"#6FDAD7", "#A9F1EF",  200);
                                    if (tempPattern.get(0) != 3) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }
                                }
                            } else {
                                if (initialY < 746) {
                                    //Quadrant2
                                    showColor(two,  "#F2E15F","#FFF6B0", 200);
                                    if (tempPattern.get(0) != 2) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }

                                } else {
                                    //Quadrant 4
                                    showColor(four,  "#0E8D8B", "#22AAA7",200);
                                    if (tempPattern.get(0) != 4) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }
                                }
                            }
                        }
                        break;
                }
                return true;
            }

        });


        pattern = makePattern(counter);
        tempPattern = new ArrayList<Integer>(pattern);
        final Handler handler = new Handler();
        Toast.makeText(getActivity(), "Let's get started!",
                Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPattern(pattern);
            }
        }, 1000);

         return view;
    }

    public ArrayList<Integer> makePattern(int count) {
        Log.d("T", "Making Pattern");
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i<count;i++ ) {
            int r = rand.nextInt(4) + 1;
            list.add(r);
        }
        return list;
    }

    public void showPattern(ArrayList<Integer> list) {
        Log.d("T", "Displaying pattern: " + list);
        final Handler handler = new Handler();
        for (int i = 1; i<=list.size();i++) {
            final int square = list.get(i-1);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (square == 1) {
                        showColor(one, "#EBA938", "#FFCF7C", 500);
                    } else if (square == 2) {
                        showColor(two, "#F2E15F", "#FFF6B0",500);
                    } else if (square == 3) {
                        showColor(three, "#6FDAD7", "#A9F1EF", 500);
                    } else {
                        showColor(four, "#0E8D8B", "#22AAA7", 500);
                    }
                }
            }, 1000 * i);
        }
    }

    public void showColor(final TextView b, String pressColor, final String normColor, int time) {
        b.setBackgroundColor(Color.parseColor(pressColor));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.setBackgroundColor(Color.parseColor(normColor));
            }
        }, time);
    }

    public void startOver() {
        Toast.makeText(getActivity(), "Oops! Wrong pattern. Let's start over.",
                Toast.LENGTH_SHORT).show();
        counter = 1;
        pattern = makePattern(counter);
        tempPattern = new ArrayList<Integer>(pattern);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPattern(pattern);
            }
        }, 1000);

    }

    public void contGame() {
        tempPattern.remove(0);
        if (tempPattern.size() == 0) {
            pattern.add(rand.nextInt(4) + 1);
            tempPattern = new ArrayList<Integer>(pattern);
            showPattern(pattern);
        }
    }

}