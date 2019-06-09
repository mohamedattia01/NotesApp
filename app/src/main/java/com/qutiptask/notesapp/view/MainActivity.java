package com.qutiptask.notesapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qutiptask.notesapp.UiUtilities.Utilities;
import com.qutiptask.notesapp.Notes;
import com.qutiptask.notesapp.viewModel.NotesViewModel;
import com.qutiptask.notesapp.R;
import com.qutiptask.notesapp.UiUtilities.SwipeToDeleteCallback;
import com.qutiptask.notesapp.adapters.NotesAdapter;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView notesRecyclerView;
    FloatingActionButton addNewNoteBtn;
    NotesViewModel notesViewModel;

    int lang;
    String displayLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar_color));

        if (Utilities.getLocale(this).equals(Locale.ENGLISH.getDisplayLanguage())) {
            lang = 1;
            displayLang = "en";
        } else {
            lang = 2;
            displayLang = "ar";
        }

        addNewNoteBtn = findViewById(R.id.addNewNoteBtn);
        addNewNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
                intent.putExtra("EXTRA_TYPE", 0);
                startActivityForResult(intent, 1);
            }
        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setHasFixedSize(true);

        final TextView noNotes_txt = findViewById(R.id.noNotes_txt);

        final NotesAdapter notesAdapter = new NotesAdapter(this);
        notesRecyclerView.setAdapter(notesAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(this, notesAdapter));
        itemTouchHelper.attachToRecyclerView(notesRecyclerView);

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        notesViewModel.getListLiveData().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {

                notesAdapter.setNotesList(notes);
                if (notes != null && notes.size() != 0) {
                    noNotes_txt.setVisibility(View.GONE);
                } else
                    noNotes_txt.setVisibility(View.VISIBLE);
            }
        });
    }

    public void changeLang(String lang_refer) {
        if (lang_refer.equals("en")) {
            Utilities.init(this).saveLang("en");
        } else {
            Utilities.init(this).saveLang("ar");
        }

        if (Utilities.getLocale(this).equals(Locale.ENGLISH.getDisplayLanguage())) {
            lang = 1;
            displayLang = "en";
        } else {
            lang = 2;
            displayLang = "ar";
        }

        try {
            Intent intent = new Intent();
            intent.setAction("LANG");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
        startActivity(getIntent());

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (displayLang.equals(Utilities.init(this).getLang())) {
            return;
        } else {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.deleteAllItem) {
            notesViewModel.deleteAllNotes();
            Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
            return true;

        } else if (item.getItemId() == R.id.langChang) {
            if (Utilities.init(MainActivity.this).getLang().equals("en"))
                changeLang("ar");
            else
                changeLang("en");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (data != null) {
                String title = data.getStringExtra(AddNewNoteActivity.EXTRA_TITLE);
                String body = data.getStringExtra(AddNewNoteActivity.EXTRA_BODY);
                int priority = data.getIntExtra(AddNewNoteActivity.EXTRA_PRIORITY, 1);

                Notes notes = new Notes(title, body, priority);

                notesViewModel.insert(notes);

                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            if (data != null) {
                String title = data.getStringExtra(AddNewNoteActivity.EXTRA_TITLE);
                String body = data.getStringExtra(AddNewNoteActivity.EXTRA_BODY);
                int priority = data.getIntExtra(AddNewNoteActivity.EXTRA_PRIORITY, 1);
                int noteId = data.getIntExtra(AddNewNoteActivity.EXTRA_ID, -1);

                if (noteId == -1)
                    Toast.makeText(this, "note can't be updated", Toast.LENGTH_SHORT).show();

                Notes notes = new Notes(title, body, priority);
                notes.setId(noteId);
                notesViewModel.update(notes);

                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
