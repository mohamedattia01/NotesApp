package com.qutiptask.notesapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.qutiptask.notesapp.Notes;
import com.qutiptask.notesapp.repositories.NotesDataAccessObject;

@Database(entities = {Notes.class}, version = 1)
public abstract class NotesDataBase extends RoomDatabase {

    private static NotesDataBase instance;

    public abstract NotesDataAccessObject noteDataAccessObject();

    public static synchronized NotesDataBase getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDataBase.class, "Notes_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}