package com.example.notes.activites;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.adapters.TaskAdapter;
import com.example.notes.bottomsheet.CalBottomSheet;
import com.example.notes.bottomsheet.TaskBottomSheet;
import com.example.notes.database.DatabaseClient;
import com.example.notes.entities.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class TaskActivity extends AppCompatExtendsClass implements TaskBottomSheet.setRefreshListener{

    RecyclerView tRecycler;
    TaskAdapter tAdptr;
    List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);



        TextView add = findViewById(R.id.addT);
        ImageView cal = findViewById(R.id.imgCal);
        ImageView back = findViewById(R.id.imgBackTask);

        back.setOnClickListener(view -> onBackPressed());


        ButterKnife.bind(this);
        setUpAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName rec = new ComponentName(this, AlarmBroadReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(rec, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        add.setOnClickListener(view -> {
            TaskBottomSheet taskBS = new TaskBottomSheet();
            taskBS.setTaskId(0, false, this, TaskActivity.this);
            taskBS.show(getSupportFragmentManager(), taskBS.getTag());
        });

        getSavedTasks();

        cal.setOnClickListener(view -> {
            CalBottomSheet calViewBS = new CalBottomSheet();
            calViewBS.show(getSupportFragmentManager(), calViewBS.getTag());
        });
    }

    public void setUpAdapter() {
        tRecycler = findViewById(R.id.taskRecycler);
        tAdptr = new TaskAdapter(this, tasks, this);
        tRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tRecycler.setAdapter(tAdptr);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                            .getAllTasksList();
                return tasks;
            }
            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                setUpAdapter();
            }

        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }
}
