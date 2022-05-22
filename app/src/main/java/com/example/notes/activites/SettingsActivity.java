package com.example.notes.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.notes.R;
import com.example.notes.database.NotesDatabase;
import com.example.notes.entities.Note;


public class SettingsActivity extends AppCompatActivity {

    static private boolean themeChangingClicked = false;

    AlertDialog changeThemeDlg;
    CheckBox ThemeSwitching;
    TextView cnl;
    TextView change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageView imgBack = findViewById(R.id.imgBackSettings);
        LinearLayout themeChange = findViewById(R.id.SettingTheme);
        imgBack.setOnClickListener(view -> onBackPressed());
        themeChange.setOnClickListener(view -> {
            themeChangingClicked = true;
            showThemeChangingDialog();


        });

    }
    private void showThemeChangingDialog(){

        if(themeChangingClicked){

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.lout_changing_themes, (ViewGroup) findViewById(R.id.loutChangingThemesContainer));
            builder.setView(view);
            changeThemeDlg = builder.create();
            cnl=view.findViewById(R.id.cnl);
            change=view.findViewById(R.id.change);
            cnl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeThemeDlg.cancel();
                }
            });
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    switch (currentNightMode) {
                        case Configuration.UI_MODE_NIGHT_NO:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case Configuration.UI_MODE_NIGHT_YES:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                    }
                }
            });
            }


        changeThemeDlg.show();

        }
}

