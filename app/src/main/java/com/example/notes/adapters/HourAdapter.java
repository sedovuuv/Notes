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
import com.example.notes.entities.HourEvent;
import com.example.notes.utils.CalUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent> {
    public HourAdapter(@NonNull Context cxt, List<HourEvent> hEvents) {
        super(cxt, 0, hEvents);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View conView, @NonNull ViewGroup par) {
        HourEvent event = getItem(pos);
        if (conView == null) { conView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, par, false); }
        setHour(conView, event.time);
        setEvents(conView, event.events);

        return conView;
    }

    private void setHour(View conView, LocalTime time) {
        TextView timeTV = conView.findViewById(R.id.time);
        timeTV.setText(CalUtils.formattedShortTime(time));
    }

    private void setEvents(View conView, ArrayList<Event> events) {
        TextView e1 = conView.findViewById(R.id.event1);


        if(events.size() == 0) {
            hideEvent(e1);

        }
        else if(events.size() == 1) {
            setEvent(e1, events.get(0));

        }

    }

    private void setEvent(TextView txtView, Event event) {
        txtView.setText(event.getName());
        txtView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView txt) {
        txt.setVisibility(View.INVISIBLE);
    }

}




