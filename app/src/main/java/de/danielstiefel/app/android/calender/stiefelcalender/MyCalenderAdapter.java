package de.danielstiefel.app.android.calender.stiefelcalender;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyCalenderAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    Cursor cursor;

    // Default constructor
    public MyCalenderAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.cursor = cursor;
    }

    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewTitle = (TextView) view.findViewById(R.id.row_calendar_title);
        String title = cursor.getString( cursor.getColumnIndex( CalendarContract.Calendars.CALENDAR_DISPLAY_NAME ) );
        textViewTitle.setText(title);

        TextView textViewId = (TextView) view.findViewById(R.id.row_calendar_id);
        String calenderId = cursor.getString( cursor.getColumnIndex( CalendarContract.Calendars._ID ) );
        textViewId.setText(calenderId);

    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.row_calendar, parent, false);
    }

    @Override
    public Object getItem(int position) {
        Object item = super.getItem(position);

        MyCalenderObject calenderObject = new MyCalenderObject();
        calenderObject.setId(           cursor.getInt( cursor.getColumnIndex( CalendarContract.Calendars._ID)));
        calenderObject.setAccountName(  cursor.getString( cursor.getColumnIndex( CalendarContract.Calendars.ACCOUNT_NAME)));
        calenderObject.setDisplayName(  cursor.getString( cursor.getColumnIndex( CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)));
        calenderObject.setName(         cursor.getString( cursor.getColumnIndex( CalendarContract.Calendars.NAME)));

        return calenderObject;
    }
}