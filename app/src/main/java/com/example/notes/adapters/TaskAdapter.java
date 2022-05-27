package com.example.notes.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.activites.TaskActivity;
import com.example.notes.bottomsheet.TaskBottomSheet;
import com.example.notes.database.DatabaseClient;
import com.example.notes.entities.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private TaskActivity ctx;
    private LayoutInflater infl;
    private List<Task> tList;
    public SimpleDateFormat dFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.getDefault());
    public SimpleDateFormat inDFormat = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());

    Date date = null;
    String outDString = null;
    TaskBottomSheet.setRefreshListener setRListener;

    public TaskAdapter(TaskActivity ctx, List<Task> tList, TaskBottomSheet.setRefreshListener setRListener) {
        this.ctx = ctx;
        this.tList = tList;
        this.setRListener = setRListener;
        this.infl = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = infl.inflate(R.layout.task_items, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tList.get(position);
        holder.title.setText(task.getTaskTitle());
        holder.descr.setText(task.getTaskDescrption());
        holder.time.setText(task.getLastAlarm());
        holder.sts.setText(task.isComplete() ? "ЗАВЕРШЕНО" : "В ПРОЦЕССЕ");
        holder.opts.setOnClickListener(view -> showMenu(view, position));

        try {
            date = inDFormat.parse(task.getDate());
            outDString = dFormat.format(date);

            String[] items1 = outDString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.day.setText(day);
            holder.date.setText(dd);
            holder.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMenu(View view, int position) {
        final Task task = tList.get(position);
        PopupMenu menu = new PopupMenu(ctx, view);
        menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    AlertDialog.Builder delDlg = new AlertDialog.Builder(ctx, R.style.AppTheme_Dialog);
                    delDlg.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                            setPositiveButton(R.string.yes, (dialog, which) -> { deleteTaskFromId(task.getTaskId(), position); }).setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
                case R.id.menuUpdate:
                    TaskBottomSheet taskBS = new TaskBottomSheet();
                    taskBS.setTaskId(task.getTaskId(), true, ctx, ctx);
                    taskBS.show(ctx.getSupportFragmentManager(), taskBS.getTag());
                    break;
                case R.id.menuComplete:
                    AlertDialog.Builder complDlg = new AlertDialog.Builder(ctx, R.style.AppTheme_Dialog);
                    complDlg.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(task.getTaskId(), position)).setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
            }
            return false;
        });
        menu.show();
    }

    public void showCompleteDialog(int taskId, int position) {
        Dialog dlg = new Dialog(ctx, R.style.AppTheme);
        dlg.setContentView(R.layout.lout_complited);
        TextView cls = dlg.findViewById(R.id.closeButton);
        cls.setOnClickListener(view -> {
            deleteTaskFromId(taskId, position);
            dlg.dismiss();
        });
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.show();
    }


    private void deleteTaskFromId(int taskId, int position) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                DatabaseClient.getInstance(ctx)
                        .getAppDatabase()
                        .dataBaseAction()
                        .deleteTaskFromId(taskId);

                return tList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                removeAtPosition(position);
                setRListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    private void removeAtPosition(int position) {
        tList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tList.size());
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.descrp)
        TextView descr;
        @BindView(R.id.sts)
        TextView sts;
        @BindView(R.id.opts)
        ImageView opts;
        @BindView(R.id.time)
        TextView time;

        TaskViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
