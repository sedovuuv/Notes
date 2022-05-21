package com.example.notes.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.utils.CalUtils;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener listener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener listener) {
        this.days = days;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup p, int vType) {
        LayoutInflater infl = LayoutInflater.from(p.getContext());
        View v = infl.inflate(R.layout.calendar_cell, p, false);
        ViewGroup.LayoutParams loutParams = v.getLayoutParams();
        if(days.size() > 15) loutParams.height = (int)(p.getHeight() * 0.166666666);
        else loutParams.height = (int)p.getHeight();

        return new CalendarViewHolder(v, listener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder hld, int pos) {
        final LocalDate date = days.get(pos);
        hld.MnthDayTxt.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(CalUtils.selectedDate)) hld.parView.setBackgroundColor(Color.LTGRAY);

        if(date.getMonth().equals(CalUtils.selectedDate.getMonth())) hld.MnthDayTxt.setTextColor(Color.BLACK);
        else hld.MnthDayTxt.setTextColor(Color.LTGRAY);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface  OnItemListener { void onItemClick(int pos, LocalDate date);}
}