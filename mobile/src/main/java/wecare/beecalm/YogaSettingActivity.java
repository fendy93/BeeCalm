package wecare.beecalm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Zhuosi on 4/20/16.
 */
public class YogaSettingActivity extends AppCompatActivity {

    private GridView mGrid;
    private String yogaName;
    private DataContainer dc;
    private static ArrayList<Boolean> chosenPoses;
    private ArrayList<String> yogaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Yoga Settings");

        mGrid = (GridView) findViewById(R.id.gridView);

        final YogaGridAdapter adapter = new YogaGridAdapter(this);

        mGrid.setAdapter(adapter);

        dc = DataContainer.getInstance();
        yogaList = dc.getYogaList();
        chosenPoses = dc.getYogaConfig();

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.getBackground() != null) {
                    v.setBackgroundResource(0);
                } else {
                    v.setBackgroundResource(dc.getFixedYogaColor(position));
                }

                yogaName = yogaList.get(position);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, yogaName, duration);
                toast.show();
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
        for (int i = 0; i < yogaList.size(); i++) {
            View viewItem = mGrid.getChildAt(i);
            if (viewItem.getBackground() != null) {
                dc.updateYogaConfig(i, true);
            } else {
                dc.updateYogaConfig(i, false);
            }
        }
        dc.writeConfigure(getApplicationContext());
        System.out.println("start transmitting to the watch");
        Intent intent = new Intent(YogaSettingActivity.this, PhoneToWatchService.class);
        startService(intent);
        System.out.println("finished transmitting to the watch");
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
                intent = new Intent(this, SideBarActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }


}
