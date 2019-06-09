package com.qutiptask.notesapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class Settings {

    static Settings inistance;
    String language;

    // Sharedpref file name
    private static final String PREF_NAME = "OrientalWeavers";
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    public Settings(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static Settings init(Context context) {
        if (inistance == null) {
            inistance = new Settings(context);
        }

        return inistance;
    }

    public String getLang() {
        return pref.getString("LANG", "en");
    }

    public void saveLang(String language) {
        editor.putString("LANG", language);
        editor.commit();
    }

    public static String getLocale(Activity activity) {
        Locale locale = new Locale(init(activity).getLang());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
        locale = activity.getResources().getConfiguration().locale;
        Log.e("currentLoc", locale.getDisplayLanguage());

        return locale.getDisplayLanguage();
    }
}
