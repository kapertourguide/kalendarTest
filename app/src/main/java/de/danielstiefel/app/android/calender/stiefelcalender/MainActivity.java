package de.danielstiefel.app.android.calender.stiefelcalender;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

//http://www.zoftino.com/how-to-read-and-write-calendar-data-in-android

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView listView;
    private MainActivity context;
    private MyCalenderAdapter myCalenderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.calendarListView);
        setSupportActionBar(toolbar);
        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                initCalendar();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initCalendarList();
    }

    private void initCalendarList() {

        Cursor cursor = CalendarUtil.getAllEntriesFromCalenderTable(context);
        myCalenderAdapter = new MyCalenderAdapter(this, cursor, 0);
        listView.setAdapter(myCalenderAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyCalenderObject item = (MyCalenderObject) myCalenderAdapter.getItem(position);
                Intent myIntent = new Intent(context, ActivityEvents.class);
                int calId = item.getId();
                myIntent.putExtra( CalendarUtil.MY_CAL_ID_AS_INTENTPARAM, calId);
                context.startActivity(myIntent);
            }
        });

        while (cursor.moveToNext()) {
            String displayName = cursor.getString( cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            String accountName = cursor.getString( cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));

            int columnIndex = cursor.getColumnIndex(CalendarContract.Calendars._ID);
            int calenderId = cursor.getInt(columnIndex);

            TextView tv1 = new TextView(this);
            tv1.setText(displayName);
            Log.i("CALENDER_TEST", "Read - Result (id/displayName/accountName): ("+ calenderId+"/" + displayName+"/"+accountName+")");
        }



    }

    private void initCalendar() {


        //CalendarUtil.addCalendar(this);
        CalendarUtil.addEvent(this);
        CalendarUtil.getDataFromCalendarTable_old(this, listView);


        //CalendarUtil.getDataFromEventTable(this, "cal.zoftino.com");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            CalendarUtil.addCalendar(this);
            this.myCalenderAdapter.swapCursor(CalendarUtil.getAllEntriesFromCalenderTable(context));

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
