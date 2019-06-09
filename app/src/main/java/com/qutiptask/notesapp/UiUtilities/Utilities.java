package com.qutiptask.notesapp.UiUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class Utilities {

    private static Utilities inistance;

    private static final String PREF_NAME = "OrientalWeavers";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Utilities(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static Utilities init(Context context) {
        if (inistance == null) {
            inistance = new Utilities(context);
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
