package wecare.beecalm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

public class WelcomeActivity extends Activity {
    private DataContainer dc;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        dc = DataContainer.getInstance();

        System.out.println("in the welcomeActivity, prepare to call loadData");
        dc.loadData(getApplicationContext());

        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                title = dc.getActivityOrderByIndex(0);
                Log.d("welcome title", title);
                Intent startActivity = dc.getIntent(getBaseContext(), title);
                startActivity(startActivity);
                finish();
            }

            public void onTick(long millisUntilFinished) {
            }

        }.start();
    }
}
