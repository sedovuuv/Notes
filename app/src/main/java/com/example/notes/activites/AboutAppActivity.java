package com.example.notes.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;

public class AboutAppActivity extends AppCompatActivity {
    ImageView imgBack;
    ImageView imgInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        imgBack = findViewById(R.id.imgBackAbout);
        imgInfo = findViewById(R.id.info);

        imgBack.setOnClickListener(view -> onBackPressed());
        imgInfo.setOnClickListener(view -> {
            Intent sysSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            sysSettings.setData(uri);
            startActivity(sysSettings);
        });

    }
}
