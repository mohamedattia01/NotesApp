package com.qutiptask.notesapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Notes.class}, version = 1)
abstract class NotesDataBase extends RoomDatabase {

    private static NotesDataBase instance;

    abstract Note_DataAccessObject noteDataAccessObject();

    static synchronized NotesDataBase getInstance(Context context){

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
//            new populateDataBaseAsync(instance).execute();
            super.onCreate(db);
        }
    };

//    private static class populateDataBaseAsync extends AsyncTask<Void, Void, Void> {
//
//        private Note_DataAccessObject noteDataAccessObject;
//
//        private populateDataBaseAsync(NotesDataBase notesDataBase) {
//            this.noteDataAccessObject = notesDataBase.noteDataAccessObject();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            noteDataAccessObject.insert(new Notes("title1", "body1", 1));
////            noteDataAccessObject.insert(new Notes("title2", "body2", 2));
////            noteDataAccessObject.insert(new Notes("title3", "body3", 3));
//            return null;
//        }
//    }
}