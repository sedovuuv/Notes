package com.example.notes.activites;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;
import android.widget.ImageView;
import android.os.AsyncTask;

import com.example.notes.R;
import com.example.notes.adapters.NotesAdapter;
import com.example.notes.database.NotesDatabase;
import com.example.notes.entities.Note;
import com.example.notes.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {

    public static final int REQ_CODE_ADD_NOTE = 1;
    public static final int REQ_CODE_UPDATE_NOTE = 2;
    public static final int REQ_CODE_SHOW_NOTES = 3;


    private RecyclerView nRecView;
    private List<Note> nList;
    private NotesAdapter nAdapter;


    private  int nClickedPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ImageView imgAddNoteMain = findViewById(R.id.mainAddBtn);
        imgAddNoteMain.setOnClickListener(view -> {
            Intent NCreate = new Intent(MainActivity.this, CreateNoteActivity.class);
            startActivityForResult(NCreate, REQ_CODE_ADD_NOTE);



        });
        ImageView imgCalendar = findViewById(R.id.imgCalendar);
        ImageView imgSettings = findViewById(R.id.imgSettings);

        imgCalendar.setOnClickListener(view -> {
            Intent calendar = new Intent(MainActivity.this, TaskActivity.class);
            startActivity(calendar);

        });

        imgSettings.setOnClickListener(view -> {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);

        });

        nRecView = findViewById(R.id.notesRView);
        nRecView.setLayoutManager( new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        nList = new ArrayList<>();
        nAdapter = new NotesAdapter(nList, this);
        nRecView.setAdapter(nAdapter);
        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(nList.size()!=0){
                    nAdapter.searchNotes(editable.toString());
                }

            }
        });



    }

    @SuppressLint("NotifyDataSetChanged")
    protected void onResume() {
        super.onResume();

        nList.clear();
        nAdapter.notifyDataSetChanged();
        getN(REQ_CODE_SHOW_NOTES, false);

    }

    @Override
    public void onClicked(Note note, int position) {
        nClickedPos = position;
        Intent crNote = new Intent(getApplicationContext(), CreateNoteActivity.class);
        crNote.putExtra("isViewOrUpdate", true);
        crNote.putExtra("note", note);
        startActivityForResult(crNote, REQ_CODE_ADD_NOTE); // прекрасно знаю, что это activityforesult устарел, но честно без малейшего понятия как заменить его :(

    }



    private void getN(final int reqCode, final boolean isNDel){
        @SuppressLint("StaticFieldLeak")
        class GetNTask extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) { return NotesDatabase.getDatabase(getApplicationContext()).noteDao().getAllNotes(); }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if(reqCode == REQ_CODE_SHOW_NOTES){
                    nList.addAll(notes);
                    nAdapter.notifyDataSetChanged();

                } else if(reqCode == REQ_CODE_ADD_NOTE){
                    nList.add(0, notes.get(0));
                    nAdapter.notifyItemInserted(0);
                    nRecView.smoothScrollToPosition(0);

                } else if(reqCode == REQ_CODE_UPDATE_NOTE){
                    nList.remove(nClickedPos);
                    if(isNDel){ nAdapter.notifyItemRemoved(nClickedPos); }
                    else {
                        nList.add(nClickedPos, notes.get(nClickedPos));
                        nAdapter.notifyItemChanged(nClickedPos);
                    }
                }
            }
        }
        new GetNTask().execute();

    }
    @Override
    protected void onActivityResult(int requCode, int resCode, @Nullable Intent data) {
        super.onActivityResult(requCode, resCode, data);
        if(requCode == REQ_CODE_ADD_NOTE && requCode == RESULT_OK){ getN(REQ_CODE_ADD_NOTE, false); }
        else if (requCode == REQ_CODE_UPDATE_NOTE && requCode == RESULT_OK){
            if(data != null){ getN(REQ_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false)); }
        }
    }

}