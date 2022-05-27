package com.example.notes.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.notes.dao.NoteDao;
import com.example.notes.dao.TaskDao;
import com.example.notes.entities.Note;
import com.example.notes.entities.Task;

@Database(entities = {Note.class, Task.class}, version = 2, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase notesDatabase;



    public static synchronized NotesDatabase getDatabase(Context context){
        if(notesDatabase == null){
            notesDatabase = Room.databaseBuilder(context, NotesDatabase.class, "notes_db").build();

        }
        return notesDatabase;
    }

    public abstract TaskDao dataBaseAction();
    public abstract NoteDao noteDao();
    private static volatile NotesDatabase appDatabase;

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

}
