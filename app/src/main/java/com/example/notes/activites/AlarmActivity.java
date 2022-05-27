package com.example.notes.activites;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import butterknife.ButterKnife;

public class AlarmActivity extends AppCompatActivity {
    private static AlarmActivity inst;

    TextView title;
    TextView dscrpt;
    TextView tnD;
    Button clBtn;
    MediaPlayer mPlayer;

    public static AlarmActivity inst() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        title = findViewById(R.id.Atitle);
        dscrpt = findViewById(R.id.descrpt);
        tnD =findViewById(R.id.timenDate);
        clBtn = findViewById(R.id.clBtn);


        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.notific);
        mPlayer.start();

        if(getIntent().getExtras() != null) {
            title.setText(getIntent().getStringExtra("TITLE"));
            dscrpt.setText(getIntent().getStringExtra("DESC"));
            tnD.setText(getIntent().getStringExtra("DATE") + ", " + getIntent().getStringExtra("TIME"));
        }

        clBtn.setOnClickListener(view -> finish());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}
