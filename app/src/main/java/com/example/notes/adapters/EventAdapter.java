package com.example.notes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notes.R;
import com.example.notes.entities.Event;
import com.example.notes.utils.CalUtils;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View conView, @NonNull ViewGroup par) {
        Event event = getItem(pos);

        if (conView == null) {conView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, par, false);}

        TextView eventCell = conView.findViewById(R.id.eventCell);

        String eTitle = event.getName() +" "+ CalUtils.formattedTime(event.getTime());
        eventCell.setText(eTitle);
        return conView;
    }
}