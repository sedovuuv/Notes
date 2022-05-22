package com.example.notes.activites;

import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;

    import android.Manifest;
    import android.annotation.SuppressLint;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.database.Cursor;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.graphics.drawable.GradientDrawable;
    import android.net.Uri;
    import android.os.AsyncTask;
import android.os.Bundle;
    import android.provider.MediaStore;
    import android.util.Patterns;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.database.NotesDatabase;
import com.example.notes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

    import java.io.InputStream;
    import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class    CreateNoteActivity extends AppCompatActivity {

    private EditText inputNTitle, inputNSubtitle, inputNText;
    private TextView txtDateTime;
    private View viewSubtitleIndicator;
    private ImageView imgNote;
    private TextView txtURL;
    private LinearLayout loutURL;

    private String selectedNColor;
    private String selectedImgPath;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMG = 2;

    private AlertDialog dialogAddURL;
    private AlertDialog dialogDeleteNote;

    private Note alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> onBackPressed());

        inputNTitle = findViewById(R.id.inputNTitle);
        inputNSubtitle = findViewById(R.id.inputNSubtitle);
        inputNText = findViewById(R.id.inputNote);
        txtDateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleInd);
        imgNote = findViewById(R.id.imgNote);
        txtURL = findViewById(R.id.textURL);
        loutURL = findViewById(R.id.loutURL);



        txtDateTime.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        ImageView imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(view -> saveN());

        selectedNColor = "#DBDBDB";
        selectedImgPath = "";

        if(getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewUpdateN();

        }

        findViewById(R.id.imgRemoveURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtURL.setText(null);
                loutURL.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.imgRemoveImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNote.setImageBitmap(null);
                imgNote.setVisibility(View.GONE);
                findViewById(R.id.imgRemoveImg).setVisibility(View.GONE);
                selectedImgPath="";
            }
        });


        initNChanging();
        setSubtitleIndicClr();


    }

    private void setViewUpdateN(){

        inputNTitle.setText(alreadyAvailableNote.getTitle());
        inputNSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNText.setText(alreadyAvailableNote.getNoteText());
        txtDateTime.setText(alreadyAvailableNote.getDatetime());

        if(alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()){

            imgNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            imgNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imgRemoveImg).setVisibility(View.VISIBLE);
            selectedImgPath = alreadyAvailableNote.getImagePath();

        }

        if(alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()){

            txtURL.setText(alreadyAvailableNote.getWebLink());
            loutURL.setVisibility(View.VISIBLE);
        }

    }

    private void saveN() {

        if(inputNTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Заголовок заметки не может быть пустым.", Toast.LENGTH_SHORT).show();
            return;

        } else if (inputNSubtitle .getText().toString().trim().isEmpty() && inputNText.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Заметка не может быть пустой.", Toast.LENGTH_SHORT).show();
            return;

        }

        final Note note = new Note();

        note.setTitle(inputNTitle.getText().toString());
        note.setSubtitle(inputNSubtitle.getText().toString());
        note.setNoteText(inputNText.getText().toString());
        note.setDatetime(txtDateTime.getText().toString());
        note.setColor(selectedNColor);
        note.setImagePath(selectedImgPath);

        if(loutURL.getVisibility() == View.VISIBLE){
            note.setWebLink(txtURL.getText().toString());
        }

        if(alreadyAvailableNote!=null){
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        }

        new SaveNTask().execute();

    }


    private void initNChanging(){

        final LinearLayout loutChanging = findViewById(R.id.loutChanger);
        final BottomSheetBehavior<LinearLayout> btmSheetBehavior = BottomSheetBehavior.from(loutChanging);

        loutChanging.findViewById(R.id.txtMiscellaneous).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                if(btmSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    btmSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    btmSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }
        });

        final ImageView imgColor1 = loutChanging.findViewById(R.id.imgColor1);
        final ImageView imgColor2 = loutChanging.findViewById(R.id.imgColor2);
        final ImageView imgColor3 = loutChanging.findViewById(R.id.imgColor3);
        final ImageView imgColor4 = loutChanging.findViewById(R.id.imgColor4);
        final ImageView imgColor5 = loutChanging.findViewById(R.id.imgColor5);

        loutChanging.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNColor = "#DBDBDB";
                imgColor1.setImageResource(R.drawable.ic_done);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicClr();
            }
        });

        loutChanging.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNColor = "#FDBE3B";
                imgColor2.setImageResource(R.drawable.ic_done);
                imgColor1.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicClr();
            }
        });
        loutChanging.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNColor = "#F44336";
                imgColor3.setImageResource(R.drawable.ic_done);
                imgColor2.setImageResource(0);
                imgColor1.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicClr();
            }
        });

        loutChanging.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNColor = "#4CAF50";
                imgColor4.setImageResource(R.drawable.ic_done);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor1.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicClr();
            }
        });

        loutChanging.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNColor = "#3F51B5";
                imgColor5.setImageResource(R.drawable.ic_done);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor1.setImageResource(0);
                setSubtitleIndicClr();
            }
        });

        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()){
            switch (alreadyAvailableNote.getColor()){
                case "#FDBE3B":
                    loutChanging.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#F44336":
                    loutChanging.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#4CAF50":
                    loutChanging.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#3F51B5":
                    loutChanging.findViewById(R.id.viewColor5).performClick();
                    break;



            }
        }



        loutChanging.findViewById(R.id.loutAddImg).setOnClickListener(view -> {
            btmSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            } else { selectImg(); }

        });



        loutChanging.findViewById(R.id.loutAddUrl).setOnClickListener(view -> {
            btmSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showURLDialog();
        });
        loutChanging.findViewById(R.id.loutCheckBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btmSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


            }
        });

        if(alreadyAvailableNote != null){
            loutChanging.findViewById(R.id.loutNDelete).setVisibility(View.VISIBLE);
            loutChanging.findViewById(R.id.loutNDelete).setOnClickListener(view -> {
                btmSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDeleteNoteDialog();

            });
        }

    }

    private void showDeleteNoteDialog(){
        if(dialogDeleteNote == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View v = LayoutInflater.from(this).inflate(R.layout.lout_delete_note, (ViewGroup) findViewById(R.id.loutDeleteNoteContainer));
            builder.setView(v);
            dialogDeleteNote = builder.create();
            if(dialogDeleteNote.getWindow() != null){ dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }
            v.findViewById(R.id.TextDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    class DeleteNTask extends AsyncTask<Void, Void, Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getDatabase(getApplicationContext()).noteDao().deleteNote(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            Intent del = new Intent();
                            del.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, del);
                            finish();
                        }
                    }
                    new DeleteNTask().execute();
                }
            });
            v.findViewById(R.id.textCancel).setOnClickListener(view -> dialogDeleteNote.dismiss());

        }
        dialogDeleteNote.show();
    }

    private void setSubtitleIndicClr(){
        GradientDrawable grad = (GradientDrawable) viewSubtitleIndicator.getBackground();
        grad.setColor(Color.parseColor((selectedNColor)));

    }

    private void selectImg() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMG); // как заменить я все еще не знаю но пока работает значит не трогать

        }

    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permis, @NonNull int[] grantRes) {
        super.onRequestPermissionsResult(reqCode, permis, grantRes);
        if(reqCode == REQUEST_CODE_STORAGE_PERMISSION && grantRes.length > 0){
            if(grantRes[0]==PackageManager.PERMISSION_GRANTED){ selectImg();} else { Toast.makeText(this, "Permisson denied", Toast.LENGTH_SHORT).show(); }
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, @Nullable Intent data) {

        super.onActivityResult(reqCode, resCode, data);
        if (reqCode == REQUEST_CODE_SELECT_IMG && resCode == RESULT_OK){
            if (data !=  null){
                Uri selectImgUri = data.getData();
                if(selectImgUri != null){
                    try{
                        InputStream inputS = getContentResolver().openInputStream(selectImgUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputS);
                        imgNote.setImageBitmap(bitmap);
                        imgNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imgRemoveImg).setVisibility(View.VISIBLE);
                        selectedImgPath = getUriPath(selectImgUri);

                    } catch (Exception er){
                        Toast.makeText(this, er.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    private String getUriPath(Uri contentUri){
        String fPath;
        Cursor csr = getContentResolver().query(contentUri, null, null, null, null);
        if(csr == null){
            fPath = contentUri.getPath();
        } else {
            csr.moveToFirst();
            int index = csr.getColumnIndex("_data");
            fPath = csr.getString(index);
            csr.close();
        }
        return fPath;

    }
    private void showURLDialog(){
        if(dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View v = LayoutInflater.from(this).inflate(R.layout.lout_add_url, (ViewGroup) findViewById(R.id.loutAddUrlContainer));
            builder.setView(v);

            dialogAddURL = builder.create();
            if(dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inURL = v.findViewById(R.id.inputURL);
            inURL.requestFocus();

            v.findViewById(R.id.textAdd).setOnClickListener(view -> {
                if(inURL.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Введите URL", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(inURL.getText().toString()).matches()){
                    Toast.makeText(CreateNoteActivity.this, "Введите корректный URL", Toast.LENGTH_SHORT).show();

                } else {
                    txtURL.setText(inURL.getText().toString());
                    loutURL.setVisibility(View.VISIBLE);
                    dialogAddURL.dismiss();
                }
            });

            v.findViewById(R.id.textCancel).setOnClickListener(view -> dialogAddURL.dismiss());


        }
        dialogAddURL.show();

    }


}