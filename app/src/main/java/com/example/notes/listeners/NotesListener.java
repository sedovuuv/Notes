package com.example.notes.listeners;

import com.example.notes.entities.Note;

public interface NotesListener {
    void onClicked(Note note, int position);
}
