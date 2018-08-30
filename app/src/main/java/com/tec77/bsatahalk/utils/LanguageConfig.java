package com.tec77.bsatahalk.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.Preference;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.database.SharedPref;

import java.util.Locale;
import java.util.prefs.Preferences;

import static com.tec77.bsatahalk.database.SharedPref.editor;

/**
 * Created by Mohamed_Aboalarbe on 2/23/2018.
 */

public class LanguageConfig extends Application {

    private String language;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPref preferences =SharedPref.getInstance(this);
        language="ar";
        setLocale(language);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //SharedPref preferences =SharedPref.getInstance(this);
        language="ar";
        setLocale(language);
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(myLocale);
            getApplicationContext().createConfigurationContext(config);
            getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

        }else{
            config.locale=myLocale;
            getApplicationContext().getResources().updateConfiguration(config,null);
        }
    }


}