package com.example.notes.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ArrayList<LocalDate> days;
    public final View parView;
    public final TextView MnthDayTxt;
    private final CalendarAdapter.OnItemListener listener;

    public CalendarViewHolder(@NonNull View iView, CalendarAdapter.OnItemListener listener, ArrayList<LocalDate> days) {
        super(iView);
        parView = iView.findViewById(R.id.parView);
        MnthDayTxt = iView.findViewById(R.id.cellDTxt);
        this.listener = listener;
        iView.setOnClickListener(this);
        this.days = days;
    }

    @Override
    public void onClick(View view) { listener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition())); }
}