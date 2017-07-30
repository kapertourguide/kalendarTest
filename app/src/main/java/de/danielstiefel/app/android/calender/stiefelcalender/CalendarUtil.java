package de.danielstiefel.app.android.calender.stiefelcalender;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by danis_000 on 14.07.2017.
 */

public class CalendarUtil {

    public static final String MY_CAL_ID_AS_INTENTPARAM = "KalenderID";
    static int calenderId;
    static final int MY_CAL_REQ = 11001;
    static final int MY_CAL_WRITE_REQ = 11002;

    static final String TEST_APP_CALENDAR_NAME = "TestApp Calendar";
    static final String TEST_APP_CALENDAR_DISPLAY_NAME = "TestApp Calendar";
    static final String TEST_APP_CALENDAR_ACCOUNT_NAME = "TestAppCalendar@Account.name";
    static final String TEST_APP_CALENDAR_ACCOUNT_TYPE = "testapp.calendar.accounttype";


    static public void addCalendar(Activity activity) {

        int calenderExist = checkIfCalenderExist(activity, TEST_APP_CALENDAR_ACCOUNT_NAME, TEST_APP_CALENDAR_ACCOUNT_TYPE);
        if( calenderExist > 0){
            String text = "Kalender " + TEST_APP_CALENDAR_ACCOUNT_NAME + " existiert bereits.";
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME,      TEST_APP_CALENDAR_ACCOUNT_NAME);
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE,      TEST_APP_CALENDAR_ACCOUNT_TYPE);
        contentValues.put(CalendarContract.Calendars.NAME,              TEST_APP_CALENDAR_NAME);
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, TEST_APP_CALENDAR_DISPLAY_NAME);
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR,    "232323");
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT,     TEST_APP_CALENDAR_ACCOUNT_NAME);
        contentValues.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT, METHOD_EMAIL, METHOD_ALARM");
        contentValues.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "TYPE_OPTIONAL, TYPE_REQUIRED, TYPE_RESOURCE");
        contentValues.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE");


        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        uri = uri.buildUpon().appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, TEST_APP_CALENDAR_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, TEST_APP_CALENDAR_ACCOUNT_TYPE).build();
        activity.getContentResolver().insert(uri, contentValues);
    }

    private static int checkIfCalenderExist(final Activity activity, String accountName, String accountType) {

        Cursor cur = findCalendar(activity, accountName, accountType);

        if(cur.getCount() > 0){
            return cur.getCount();
        } else {
            return -1;
        }
    }

    private static Cursor findCalendar(Activity activity, String accountName, String accountType) {

        ContentResolver cr = activity.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
        String[] selectionArgs = new String[]{accountName, accountType};

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }

        return cr.query(uri, null, selection, selectionArgs, null);
    }

    static public Cursor getAllEntriesFromCalenderTable(final Activity activity){

        ContentResolver cr = activity.getContentResolver();

        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = cr.query(uri, null, null, null, null);

        return cur;
    }

    @Deprecated
    static public void getDataFromCalendarTable_old(final Activity activity, ListView listView) {

        ContentResolver cr = activity.getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.CALENDAR_LOCATION,
                        CalendarContract.Calendars.CALENDAR_TIME_ZONE
                };

        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = cr.query(uri, null, null, null, null);


        final MyCalenderAdapter myCalenderAdapter = new MyCalenderAdapter(activity, cur, 0);
        listView.setAdapter(myCalenderAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyCalenderObject item = (MyCalenderObject) myCalenderAdapter.getItem(position);
                Intent myIntent = new Intent(activity, ActivityEvents.class);
                myIntent.putExtra(CalendarUtil.MY_CAL_ID_AS_INTENTPARAM, item.getId());
                activity.startActivity(myIntent);
            }
        });

        while (cur.moveToNext()) {
            String displayName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            String accountName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));

            int columnIndex = cur.getColumnIndex(CalendarContract.Calendars._ID);
            calenderId = cur.getInt(columnIndex);
            //ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayoutMain);
            //insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

            TextView tv1 = new TextView(activity);
            tv1.setText(displayName);
            Log.i("CALENDER_TEST", "Read - Result (id/displayName/accountName): ("+ calenderId+"/" + displayName+"/"+accountName+")");
            //insertPoint.addView(tv1);
        }

    }

    static public void addEvent(Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();
/*
Calendar beginTime = Calendar.getInstance();
beginTime.set(2017, 07, 15, 9, 30);
Calendar endTime = Calendar.getInstance();
endTime.set(2017, 07, 15, 10, 35);
*/

        Date beginTime = new Date();
        Date endTime = new Date((beginTime.getTime() + 60 * 60 * 1000));
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTime());
        values.put(CalendarContract.Events.DTEND, endTime.getTime());
        values.put(CalendarContract.Events.TITLE, "Tech Stores");
        values.put(CalendarContract.Events.DESCRIPTION, "Successful Startups");
        values.put(CalendarContract.Events.CALENDAR_ID, calenderId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/London");
        values.put(CalendarContract.Events.EVENT_LOCATION, "London");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        Uri newUri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.i("CALENDER_TEST", "Insert - ResultURI: " + newUri);
    }

    static public Cursor getDataFromEventTable(Activity activity, String accountNameOfCalender) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = null;
        ContentResolver cr = activity.getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = CalendarContract.Events.ACCOUNT_NAME + " = ? ";
        String[] selectionArgs = new String[]{accountNameOfCalender};

        return cur = cr.query(uri, null, selection, selectionArgs, null);

    }

    static public Cursor getDataFromEventTable(Activity activity, int calId) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }

        String selection = CalendarContract.Events.CALENDAR_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(calId)};

        return activity.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, selectionArgs, null);

    }

}
