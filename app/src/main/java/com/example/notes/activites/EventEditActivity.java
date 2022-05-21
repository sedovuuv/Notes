package com.example.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.entities.Event;
import com.example.notes.utils.CalUtils;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity {
    private EditText nameOfEvent;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        nameOfEvent = findViewById(R.id.eventName);
        TextView eventDate = findViewById(R.id.eventDate);
        TextView eventTime = findViewById(R.id.eventTime);
        time = LocalTime.now();

        eventDate.setText("Date: " + CalUtils.formattedDate(CalUtils.selectedDate));
        eventTime.setText("Time: " + CalUtils.formattedTime(time));

    }


    public void saveAction(View view) {
        String eventName = nameOfEvent.getText().toString();
        Event nEvent = new Event(eventName, CalUtils.selectedDate, time);
        Event.eventsList.add(nEvent);
        finish();
    }
}