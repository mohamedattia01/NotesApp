package com.qutiptask.notesapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface Note_DataAccessObject {

    @Insert
    void insert(Notes note);

    @Update
    void update(Notes note);

    @Delete
    void delete(Notes note);

    @Query("DELETE FROM notes_table")
    void deleteNotesFromTable();

    @Query("SELECT * FROM notes_table ORDER BY priority DESC")
    LiveData<List<Notes>> getNotesByPriority();
}
