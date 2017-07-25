package de.danielstiefel.app.android.calender.stiefelcalender;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyEventAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    // Default constructor
    public MyEventAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewTitle = (TextView) view.findViewById(R.id.row_event_title);
        String title = cursor.getString( cursor.getColumnIndex( CalendarContract.Events.TITLE ) );
        textViewTitle.setText(title);

        TextView textViewId = (TextView) view.findViewById(R.id.row_event_id);
        String eventId = cursor.getString( cursor.getColumnIndex( CalendarContract.Events._ID ) );
        textViewId.setText(eventId);

        TextView textViewLocation = (TextView) view.findViewById(R.id.row_event_location);
        String location = cursor.getString( cursor.getColumnIndex( CalendarContract.Events.EVENT_LOCATION ) );
        textViewLocation.setText(location);

        TextView textViewStartTime = (TextView) view.findViewById(R.id.row_event_start);
        String start = cursor.getString( cursor.getColumnIndex( CalendarContract.Events.DTSTART ) );
        textViewStartTime.setText(start);

    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.row_event, parent, false);
    }
}