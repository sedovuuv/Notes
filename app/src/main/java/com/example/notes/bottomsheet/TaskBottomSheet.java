package com.example.notes.bottomsheet;

import static android.content.Context.ALARM_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.notes.R;
import com.example.notes.activites.AlarmBroadReceiver;
import com.example.notes.activites.TaskActivity;
import com.example.notes.database.DatabaseClient;
import com.example.notes.entities.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskBottomSheet extends BottomSheetDialogFragment {
    Unbinder unbinder;
    @BindView(R.id.addTTitle)
    EditText addTaskTitle;
    @BindView(R.id.addTDescrp)
    EditText addTaskDescription;
    @BindView(R.id.taskDate)
    EditText taskDate;
    @BindView(R.id.taskTime)
    EditText taskTime;
    @BindView(R.id.addTask)
    Button addTask;

    int id;
    boolean isEdit;
    int year, month, day;
    int hour, minute;

    Task task;


    setRefreshListener setRListener;
    AlarmManager alarmMngr;
    TimePickerDialog timePickDlg;
    DatePickerDialog datePickDlg;
    TaskActivity activityTask;
    public static int c = 0;

    private BottomSheetBehavior.BottomSheetCallback sheetCallback = new BottomSheetBehavior.BottomSheetCallback() {



        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public void setTaskId(int taskId, boolean isEdit, setRefreshListener setRefreshListener, TaskActivity activity) {
        this.id = taskId;
        this.isEdit = isEdit;
        this.activityTask = activity;
        this.setRListener = setRefreshListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public void setupDialog(@NonNull Dialog dlg, int style) {
        super.setupDialog(dlg, style);
        View contentView = View.inflate(getContext(), R.layout.lout_task_editor, null);
        unbinder = ButterKnife.bind(this, contentView);
        dlg.setContentView(contentView);
        alarmMngr = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        addTask.setOnClickListener(view -> {
            if(validateFields())
                createTask();
        });
        if (isEdit) {
            showTask();
        }

        taskDate.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                datePickDlg = new DatePickerDialog(getActivity(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            taskDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickDlg.dismiss();
                        }, year, month, day);
                datePickDlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickDlg.show();
            }
            return true;
        });

        taskTime.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                timePickDlg = new TimePickerDialog(getActivity(),
                        (view12, hourOfDay, minute) -> {
                            taskTime.setText(hourOfDay + ":" + minute);
                            timePickDlg.dismiss();
                        }, hour, minute, false);
                timePickDlg.show();
            }
            return true;
        });
    }

    public boolean validateFields() {
        if(addTaskTitle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activityTask, "Введите корректный заголовок", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addTaskDescription.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activityTask, "Введите корректное описание", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(taskDate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activityTask, "Введите дату", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(taskTime.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activityTask, "Введите время", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createTask() {
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                Task createTask = new Task();
                createTask.setTaskTitle(addTaskTitle.getText().toString());
                createTask.setTaskDescrption(addTaskDescription.getText().toString());
                createTask.setDate(taskDate.getText().toString());
                createTask.setLastAlarm(taskTime.getText().toString());


                if (!isEdit)
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .insertDataIntoTaskList(createTask);
                else
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .updateAnExistingRow(id, addTaskTitle.getText().toString(),
                                    addTaskDescription.getText().toString(),
                                    taskDate.getText().toString(),
                                    taskTime.getText().toString());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    createAnAlarm();
                }
                setRListener.refresh();
                Toast.makeText(getActivity(), "Событие было добавлено", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        }
        saveTaskInBackend st = new saveTaskInBackend();
        st.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAnAlarm() {
        try {
            String[] items1 = taskDate.getText().toString().split("-");
            String dd = items1[0];
            String month = items1[1];
            String year = items1[2];

            String[] itemTime = taskTime.getText().toString().split(":");
            String hour = itemTime[0];
            String min = itemTime[1];

            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());

            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            cal.set(Calendar.MINUTE, Integer.parseInt(min));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DATE, Integer.parseInt(dd));

            Intent alarm = new Intent(activityTask, AlarmBroadReceiver.class);
            alarm.putExtra("TITLE", addTaskTitle.getText().toString());
            alarm.putExtra("DESC", addTaskDescription.getText().toString());
            alarm.putExtra("DATE", taskDate.getText().toString());
            alarm.putExtra("TIME", taskTime.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activityTask, c, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMngr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmMngr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    alarmMngr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                c++;

                PendingIntent intent = PendingIntent.getBroadcast(activityTask, c, alarm, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmMngr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmMngr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    } else {
                        alarmMngr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
                    }
                }
                c++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTask() {
        class showTaskFromId extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                task = DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction().selectDataFromAnId(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setData();
            }
        }
        showTaskFromId st = new showTaskFromId();
        st.execute();
    }

    private void setData() {
        addTaskTitle.setText(task.getTaskTitle());
        addTaskDescription.setText(task.getTaskDescrption());
        taskDate.setText(task.getDate());
        taskTime.setText(task.getLastAlarm());
    }

    public interface setRefreshListener {
        void refresh();
    }
}
