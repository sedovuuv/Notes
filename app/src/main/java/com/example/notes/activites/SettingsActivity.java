package com.example.notes.activites;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;

public class SettingsActivity extends AppCompatActivity {

    static private boolean themeChangingClicked = false;
    public boolean themeChanging = false;
    AlertDialog changeThemeDlg;
    Switch ThemeSwitching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView imgBack = findViewById(R.id.imgBackSettings);
        LinearLayout themeChange = findViewById(R.id.SettingTheme);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        themeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeChangingClicked = true;
                showThemeChangingDialog();


            }
        });

    }
    private void showThemeChangingDialog(){
        if(themeChangingClicked){
            ThemeSwitching = findViewById(R.id.themeChanger);
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.lout_changing_themes, (ViewGroup) findViewById(R.id.loutChangingThemesContainer));
            builder.setView(view);
            changeThemeDlg = builder.create();
            if(changeThemeDlg.getWindow() != null){
                changeThemeDlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }



        }
        changeThemeDlg.show();
    }
}