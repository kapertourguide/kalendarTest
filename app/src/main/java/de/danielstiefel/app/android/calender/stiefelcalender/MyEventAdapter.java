package de.danielstiefel.app.android.calender.stiefelcalender;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;


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


        TextView textViewStartTime  = (TextView) view.findViewById(R.id.row_event_start);
        Long timeInMillisStart  = Long.valueOf( cursor.getString( cursor.getColumnIndex( CalendarContract.Events.DTSTART ) ));
        String formatedDate     = formatToGermanDate( timeInMillisStart);
        textViewStartTime   .setText( formatedDate);

        TextView textViewEndTime    = (TextView) view.findViewById(R.id.row_event_end);
        String dtend            = CalendarContract.Events.DTEND;
        int columnIndex         = cursor.getColumnIndex(dtend);
        String string           = cursor.getString(columnIndex);
        if(!(string == null)){
            Long timeInMillisEnd    = Long.valueOf( string);
            String text             = formatToGermanDate( timeInMillisEnd);
            textViewEndTime     .setText( text);
        }
    }

    @NonNull
    private String formatToGermanDate(Long timeInMillis) {
        Date inDate = new Date();
        inDate.setTime(timeInMillis);
        String date = DateFormat.format("dd.MM.yyyy", inDate).toString();
        String time = DateFormat.format("kk:mm", inDate).toString();
        String result = date +" "+ time + "Uhr";
//        if( !time.equals("00:00")){
//            result +=  " " + time + "Uhr";
//        }
        return result;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.row_event, parent, false);
    }
}