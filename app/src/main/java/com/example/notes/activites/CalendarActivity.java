package com.example.notes.activites;
import static com.example.notes.utils.CalUtils.daysInMonthArray;
import static com.example.notes.utils.CalUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.adapters.CalendarAdapter;
import com.example.notes.utils.CalUtils;

import java.time.LocalDate;
import java.util.ArrayList;


public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView mnthYrTxt;
    private RecyclerView calRView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);
        calRView = findViewById(R.id.calRecView);
        mnthYrTxt = findViewById(R.id.mnthYear);
        Button weelkyAct = findViewById(R.id.weeklyAction);
        ImageView imgBackCal = findViewById(R.id.imgBackCalendar);
        ImageButton imgNext = findViewById(R.id.nxtMnthAction);
        ImageButton imgPrev = findViewById(R.id.prevMnthAction);

        CalUtils.selectedDate = LocalDate.now();
        //календари отвратительная штука

        imgBackCal.setOnClickListener(view -> onBackPressed());

        weelkyAct.setOnClickListener(view -> {
            Intent weekAct = new Intent(CalendarActivity.this, WeekViewActivity.class);
            startActivity(weekAct);
        });
        imgNext.setOnClickListener(view -> {
            CalUtils.selectedDate = CalUtils.selectedDate.plusMonths(1);
            setMnthView();

        });
        imgPrev.setOnClickListener(view -> {
            CalUtils.selectedDate = CalUtils.selectedDate.minusMonths(1);
            setMnthView();
        });

        setMnthView();


    }


    private void setMnthView() {
        mnthYrTxt.setText(monthYearFromDate(CalUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calRView.setLayoutManager(layoutManager);
        calRView.setAdapter(calendarAdapter);
    }


    @Override
    public void onItemClick(int pos, LocalDate date) {
        if(date != null) {
            CalUtils.selectedDate = date;
            setMnthView();
        }
    }

}





