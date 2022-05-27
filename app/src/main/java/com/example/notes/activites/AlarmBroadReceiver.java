package com.example.notes.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadReceiver extends BroadcastReceiver {
    String title, discrp, date, time;
    @Override
    public void onReceive(Context context, Intent intent) {

        title = intent.getStringExtra("TITLE");
        discrp = intent.getStringExtra("DESC");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");
        Intent alarm = new Intent(context, AlarmActivity.class);
        alarm.putExtra("TITLE", title);
        alarm.putExtra("DESC", discrp);
        alarm.putExtra("DATE", date);
        alarm.putExtra("TIME", time);
        alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarm);
    }
}
