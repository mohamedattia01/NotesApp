package com.qutiptask.notesapp.view;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.qutiptask.notesapp.R;

public class AddNewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.qutiptask.notesapp.EXTRA_TITLE";
    public static final String EXTRA_BODY = "com.qutiptask.notesapp.EXTRA_BODY";
    public static final String EXTRA_PRIORITY = "com.qutiptask.notesapp.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.qutiptask.notesapp.EXTRA_ID";
    TextInputEditText title_edit_text, body_edit_text;
    int id;
    NumberPicker priorityPicker;
    boolean hideSaveFromMenu, hideEditFromMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        title_edit_text = findViewById(R.id.title_edit_text);
        body_edit_text = findViewById(R.id.body_edit_text);
        priorityPicker = findViewById(R.id.priorityPicker);

        priorityPicker.setMinValue(1);
        priorityPicker.setMaxValue(10);

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar_color));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt("EXTRA_TYPE") == 1) {
                title_edit_text.setText(bundle.getString("TITLE"));
                body_edit_text.setText(bundle.getString("BODY"));
                priorityPicker.setValue(bundle.getInt("PRIORITY"));
                id = bundle.getInt("ID", -1);
                title_edit_text.setEnabled(false);
                body_edit_text.setEnabled(false);
                priorityPicker.setEnabled(false);

                hideSaveFromMenu = true;
                hideEditFromMenu = false;
            } else {
                hideEditFromMenu = true;
                hideSaveFromMenu = false;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);

        if (hideSaveFromMenu) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        } else {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (hideSaveFromMenu) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        } else {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveMenu:
                saveNote();
                return true;

            case R.id.editNote:
                editNote();
                return true;

            case R.id.closeMenu:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNote() {
        String title = String.valueOf(title_edit_text.getText());
        String body = String.valueOf(body_edit_text.getText());
        int priority = priorityPicker.getValue();

        if (title.trim().isEmpty() || body.trim().isEmpty()) {
            Toast.makeText(this, "please, enter a title and body", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_BODY, body);
        data.putExtra(EXTRA_PRIORITY, priority);
        if (id != 0 && id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    public void editNote(){
        hideSaveFromMenu = false;
        title_edit_text.setEnabled(true);
        body_edit_text.setEnabled(true);
        priorityPicker.setEnabled(true);
    }
}
