package wecare.beecalm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Zhuosi on 4/20/16.
 */
public class SimonActivity extends Activity {

    private ArrayList<Integer> pattern = new ArrayList<Integer>();
    private ArrayList<Integer> tempPattern = new ArrayList<Integer>();
    private float initialX, initialY, finalX, finalY;
    private int counter = 1;
    private int MIN_DISTANCE = 150;
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private LinearLayout main;
    private Random rand = new Random();
    private String str = "";
    private DataContainer dc;
    private static String activityName = "Simon Swipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon);
        dc = DataContainer.getInstance();

        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        three = (TextView) findViewById(R.id.three);
        four = (TextView) findViewById(R.id.four);
        main = (LinearLayout) findViewById(R.id.main);

        main.setOnTouchListener(new View.OnTouchListener() {

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
                            str = dc.getPrevActivity(activityName);
                            Intent intent_right = dc.getIntent(getBaseContext(), str);
                            startActivity(intent_right);

                        } else if (initialX > finalX && initialX - finalX > MIN_DISTANCE) {
                            //right to left
                            str = dc.getNextActivity(activityName);
                            Intent intent_left = dc.getIntent(getBaseContext(), str);
                            startActivity(intent_left);
                        } else {
                            //we'll consider it a tap
                            if (initialX < 160) {
                                if (initialY < 160) {
                                    //Quadrant1
                                    showColor(one, "#EBA938", "#FFCF7C", 200);
                                    if (tempPattern.get(0) != 1) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }
                                } else {
                                    //Quadrant 3
                                    showColor(three, "#6FDAD7", "#A9F1EF", 200);
                                    if (tempPattern.get(0) != 3) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }
                                }
                            } else {
                                if (initialY < 160) {
                                    //Quadrant2
                                    showColor(two, "#F2E15F","#FFF6B0", 200);
                                    if (tempPattern.get(0) != 2) {
                                        startOver();
                                    } else {
                                        contGame();
                                    }
                                } else {
                                    //Quadrant 4
                                    showColor(four, "#0E8D8B", "#22AAA7", 200);
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
        Toast.makeText(this, "Let's get started!",
                Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPattern(pattern);
            }
        }, 1000);

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
                        showColor(two, "#F2E15F","#FFF6B0", 500);
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
        Log.d("T", "Changing color");
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
        Toast.makeText(this, "Oops! Wrong pattern. Let's start over.",
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
            Log.d("T", "Adding to old pattern: " + pattern);
            pattern.add(rand.nextInt(4) + 1);
            Log.d("T", "Added: " + pattern);
            tempPattern = new ArrayList<Integer>(pattern);
            showPattern(pattern);
        }
    }
}
