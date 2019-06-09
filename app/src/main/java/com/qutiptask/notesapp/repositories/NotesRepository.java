package com.qutiptask.notesapp.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.qutiptask.notesapp.Notes;
import com.qutiptask.notesapp.database.NotesDataBase;

import java.util.List;

public class NotesRepository {

    private NotesDataAccessObject noteDataAccessObject;
    private LiveData<List<Notes>> listLiveData;

    public NotesRepository(Application application){
        NotesDataBase notesDataBase = NotesDataBase.getInstance(application);
        noteDataAccessObject = notesDataBase.noteDataAccessObject();
        listLiveData = noteDataAccessObject.getNotesByPriority();
    }

    public void insert(Notes notes){

        new InsertNotesAsync(noteDataAccessObject).execute(notes);
    }

    public void update(Notes notes){

        new UpdateNotesAsync(noteDataAccessObject).execute(notes);
    }

    public void delete(Notes notes){

        new DeleteNotesAsync(noteDataAccessObject).execute(notes);
    }

    public void deleteAllNotes(){

        new DeleteAllNotesAsync(noteDataAccessObject).execute();
    }

    public LiveData<List<Notes>> getListLiveData() {
        return listLiveData;
    }

    private static class InsertNotesAsync extends AsyncTask<Notes, Void, Void> {

        private NotesDataAccessObject noteDataAccessObject;

        InsertNotesAsync(NotesDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Notes... notes) {

            noteDataAccessObject.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteNotesAsync extends AsyncTask<Notes, Void, Void>{

        private NotesDataAccessObject noteDataAccessObject;

        DeleteNotesAsync(NotesDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Notes... notes) {

            noteDataAccessObject.delete(notes[0]);
            return null;
        }
    }

    private static class UpdateNotesAsync extends AsyncTask<Notes, Void, Void>{

        private NotesDataAccessObject noteDataAccessObject;

        UpdateNotesAsync(NotesDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Notes... notes) {

            noteDataAccessObject.update(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsync extends AsyncTask<Void, Void, Void>{

        private NotesDataAccessObject noteDataAccessObject;

        DeleteAllNotesAsync(NotesDataAccessObject noteDataAccessObject) {
            this.noteDataAccessObject = noteDataAccessObject;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDataAccessObject.deleteNotesFromTable();
            return null;
        }
    }
}
