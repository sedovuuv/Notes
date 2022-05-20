package com.example.notes.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

import com.example.notes.entities.Note;

import java.util.List;


@Dao

public interface NoteDao  {
    @Query("SELECT * FROM notes ORDER BY id")
    List<Note> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);



}
