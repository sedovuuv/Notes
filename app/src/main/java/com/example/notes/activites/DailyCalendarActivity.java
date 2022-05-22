package com.example.notes.activites;

import static com.example.notes.utils.CalUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.adapters.HourAdapter;
import com.example.notes.entities.Event;
import com.example.notes.entities.HourEvent;
import com.example.notes.utils.CalUtils;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DailyCalendarActivity extends AppCompatActivity {

    private TextView mnthDTxt;
    private TextView dayOfWeekTxt;
    private ListView hourLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar);

        mnthDTxt = findViewById(R.id.mnthDTxt);
        dayOfWeekTxt = findViewById(R.id.weekDay);
        hourLView = findViewById(R.id.hrLView);
        ImageButton prevBtn = findViewById(R.id.prevDayAction);
        ImageButton nxtBtn = findViewById(R.id.nxtDatAction);
        Button nEvent = findViewById(R.id.nEventAction);

        prevBtn.setOnClickListener(view -> {
            selectedDate = selectedDate.minusDays(1);
            setDView();

        });
        nxtBtn.setOnClickListener(view -> {
            selectedDate = selectedDate.plusDays(1);
            setDView();

        });
        nEvent.setOnClickListener(view -> {
            Intent nEvent1 = new Intent(DailyCalendarActivity.this, EventEditActivity.class);
            startActivity(nEvent1);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDView();
    }

    private void setDView() {
        mnthDTxt.setText(CalUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTxt.setText(dayOfWeek);
        setHAdapter();
    }

    private void setHAdapter() {
        HourAdapter hAdapter = new HourAdapter(getApplicationContext(), hrEventList());
        hourLView.setAdapter(hAdapter);
    }

    private ArrayList<HourEvent> hrEventList() {
        ArrayList<HourEvent> hrList = new ArrayList<>();

        for(int i = 0; i < 24; i++) {
            LocalTime time = LocalTime.of(i, 0);
            ArrayList<Event> eventList = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hrEvent = new HourEvent(time, eventList);
            hrList.add(hrEvent);
        }

        return hrList;
    }

}