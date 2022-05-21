package com.example.notes.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.entities.Note;
import com.example.notes.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{

    private List<Note> notes;
    private final NotesListener nListener;
    private Timer timer;
    private final List<Note> nSource;



    public NotesAdapter(List<Note> notes, NotesListener nListener) {
        this.notes = notes;
        this.nListener = nListener;
        nSource = notes;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.setNote(notes.get(position));
        holder.loutNote.setOnClickListener(view -> nListener.onClicked(notes.get(position), position));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int pos) {
        return pos;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtSubtitle, txtDTime;
        LinearLayout loutNote;
        RoundedImageView imgNote;

        NoteViewHolder(@NonNull View itemView){
            super(itemView);
            txtTitle = itemView.findViewById(R.id.textTitle);
            txtSubtitle = itemView.findViewById(R.id.textSubtitle);
            txtDTime = itemView.findViewById(R.id.textDateTime);
            loutNote = itemView.findViewById(R.id.loutNote);
            imgNote = itemView.findViewById(R.id.imgNote);


        }

        void setNote(Note note){
            txtTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){ txtSubtitle.setVisibility(View.GONE); } else { txtSubtitle.setText(note.getSubtitle()); }
            txtDTime.setText(note.getDatetime());

            GradientDrawable gradientDrawable = (GradientDrawable) loutNote.getBackground();
            if(note.getColor() != null){ gradientDrawable.setColor(Color.parseColor(note.getColor())); }
            else { gradientDrawable.setColor(Color.parseColor("#333333")); }
            if(note.getImagePath() != null){
                imgNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imgNote.setVisibility(View.VISIBLE);
            } else { imgNote.setVisibility(View.GONE); }
        }


    }
    public void searchNotes(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()){
                    notes = nSource;
                } else {
                    ArrayList<Note> tmp = new ArrayList<>();
                    for (Note note : nSource){
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            tmp.add(note);
                        }
                    }
                    notes = tmp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());

            }
        }, 500);
    }
    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }
}
