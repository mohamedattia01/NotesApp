package com.qutiptask.notesapp.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.qutiptask.notesapp.Notes;
import com.qutiptask.notesapp.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private NotesRepository notesRepository;
    private LiveData<List<Notes>> listLiveData;

    public NotesViewModel(@NonNull Application application) {
        super(application);

        notesRepository = new NotesRepository(application);
        listLiveData = notesRepository.getListLiveData();
    }

    public void insert(Notes notes){
        notesRepository.insert(notes);
    }

    public void delete(Notes notes){
        notesRepository.delete(notes);
    }

    public void update(Notes notes){
        notesRepository.update(notes);
    }

    public void deleteAllNotes(){
        notesRepository.deleteAllNotes();
    }

    public LiveData<List<Notes>> getListLiveData() {
        return listLiveData;
    }
}
