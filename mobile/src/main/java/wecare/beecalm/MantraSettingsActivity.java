package wecare.beecalm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MantraSettingsActivity extends AppCompatActivity {

    private boolean viewIsAtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayView();
    }

    public void displayView() {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        fragment = new MantraSettings();

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
            default:
                intent = new Intent(this, MantraSettingStep1Activity.class);
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        DataContainer dc = DataContainer.getInstance();
        dc.writeConfigure(getApplicationContext());
        System.out.println("start transmitting to the watch");
        Intent intent = new Intent(MantraSettingsActivity.this, PhoneToWatchService.class);
        startService(intent);
        System.out.println("finished transmitting to the watch");
    }
}
