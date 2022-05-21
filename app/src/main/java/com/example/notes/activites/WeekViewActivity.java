package com.example.notes.activites;
import static com.example.notes.utils.CalUtils.daysInWeekArray;
import static com.example.notes.utils.CalUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.adapters.CalendarAdapter;
import com.example.notes.adapters.EventAdapter;
import com.example.notes.entities.Event;
import com.example.notes.utils.CalUtils;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView mnthYTxt;
    private RecyclerView calRecView;
    private ListView eventLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        calRecView = findViewById(R.id.calRecView);
        mnthYTxt = findViewById(R.id.monthYearTV2);
        eventLView = findViewById(R.id.eventListView);
        ImageButton nxtBtn = findViewById(R.id.nxtWAction);
        ImageButton prevBtn = findViewById(R.id.prevWAction);
        Button dlyAct = findViewById(R.id.dlyAction);
        Button nEventAct = findViewById(R.id.nEventAction);
        ImageView imgBack = findViewById(R.id.imgBackWCalendar);
        nxtBtn.setOnClickListener(view -> {
            CalUtils.selectedDate = CalUtils.selectedDate.plusWeeks(1);
            setWView();

        });
        prevBtn.setOnClickListener(view -> {
            CalUtils.selectedDate = CalUtils.selectedDate.minusWeeks(1);
            setWView();

        });
        dlyAct.setOnClickListener(view -> {
            Intent dailyCal = new Intent(WeekViewActivity.this, DailyCalendarActivity.class);
            startActivity(dailyCal);

        });
        nEventAct.setOnClickListener(view -> {
            Intent eventEdit = new Intent(WeekViewActivity.this, EventEditActivity.class);
            startActivity(eventEdit);


        });


        imgBack.setOnClickListener(view -> onBackPressed());
        setWView();
    }

    private void setWView() {
        mnthYTxt.setText(monthYearFromDate(CalUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalUtils.selectedDate);

        CalendarAdapter calAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager loutManager = new GridLayoutManager(getApplicationContext(), 7);
        calRecView.setLayoutManager(loutManager);
        calRecView.setAdapter(calAdapter);
        setEventAdpater();
    }


    @Override
    public void onItemClick(int pos, LocalDate date) {
        CalUtils.selectedDate = date;
        setWView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater() {
        ArrayList<Event> dEvents = Event.eventsForDate(CalUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dEvents);
        eventLView.setAdapter(eventAdapter);
    }

}