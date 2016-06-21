package wecare.beecalm;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class SideBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean viewIsAtHome;
    private  DataContainer dc;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bar);
        dc = DataContainer.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<String> activityList = dc.getActivityString();
        activityList.remove("Biofeedback (Watch Only)");

        displayView(R_id_title(activityList.get(0)));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ArrayList<String> newActivityList = dc.getActivityString();
                newActivityList.remove("Biofeedback (Watch Only)");
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                Menu menu = navigationView.getMenu();
                for (int i = 0; i < newActivityList.size(); i++) {
                    String nav_title =  newActivityList.get(i);
                    int nav_icon = getSideBarIcon(nav_title);
                    switch (i) {
                        case 0:
                            MenuItem nav_breathing = menu.findItem(R.id.nav_breathing);
                            nav_breathing.setTitle(nav_title);
                            nav_breathing.setIcon(nav_icon);
                            break;
                        case 1:
                            MenuItem nav_mantras = menu.findItem(R.id.nav_mantras);
                            nav_mantras.setTitle(nav_title);
                            nav_mantras.setIcon(nav_icon);
                            break;
                        case 2:
                            MenuItem nav_simon = menu.findItem(R.id.nav_simon);
                            nav_simon.setTitle(nav_title);
                            nav_simon.setIcon(nav_icon);
                            break;
                        case 3:
                            MenuItem nav_yoga = menu.findItem(R.id.nav_yoga);
                            nav_yoga.setTitle(nav_title);
                            nav_yoga.setIcon(nav_icon);
                            break;
                    }
                }
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView(R.id.nav_breathing); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }
        setNewView();
    }

    public void setNewView() {
        if (dc.getActivityOrderByIndex(0).equals("Biofeedback (Watch Only)")) {
            displayView(R_id_title(dc.getActivityOrderByIndex(1)));
        } else {
            displayView(R_id_title(dc.getActivityOrderByIndex(0)));
        }
    }

    public int getSideBarIcon(String nav_title) {
        if (nav_title.equals("Breathing")) {
            return R.mipmap.ic_breathing;
        } else if (nav_title.equals("Mantras")) {
            return R.mipmap.ic_mantras;
        } else if (nav_title.equals("Simon Swipe")) {
            return R.mipmap.ic_simon;
        } else if (nav_title.equals("Yoga")) {
            return R.mipmap.ic_yoga;
        }
        return 0;
    }

    public int R_id_title(String title) {
        if (title.equals("Breathing")) {
            return R.id.nav_breathing;
        } else if (title.equals("Mantras")) {
            return R.id.nav_mantras;
        } else if (title.equals("Simon Swipe")) {
            return R.id.nav_simon;
        } else if (title.equals("Yoga")) {
            return R.id.nav_yoga;
        }
        return 0;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String title = (String) item.getTitleCondensed();
        int item_id = R_id_title(title);
        if (item_id == 0) {
            displayView(R.id.nav_setting);
        }
        displayView(item_id);
        return true;
    }

    public void displayView(int viewId) {

        ListFragment listfragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_setting:
                listfragment = new SettingFragment();
                title  = "Settings";
                viewIsAtHome = false;
                break;
            case R.id.nav_breathing:
                listfragment = new BreathingFragment();
                title  = "Breathing";
                viewIsAtHome = true;
                break;
            case R.id.nav_mantras:
                listfragment = new MantrasFragment();
                title  = "Mantras";
                viewIsAtHome = false;
                break;
            case R.id.nav_simon:
                listfragment = new SimonFragment();
                title  = "Simon Swipe";
                viewIsAtHome = false;
                break;
            case R.id.nav_yoga:
                listfragment = new YogaFragment();
                title  = "Yoga";
                viewIsAtHome = false;
                break;
        }

        if (listfragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, listfragment).commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
