package com.project.sabeen.textme;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Application Class for saving Client Id and Client Secret of Git Hub App
 * Created by sabeen on 6/3/16.
 */
public class TextMeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences("AUTH",MODE_PRIVATE);
//      Runs only once when Installed
        if(!prefs.getBoolean("firstTime", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("client_id", "09ea761811924142d7ee");
            editor.putString("client_secret", "215365e5382f583c2f94f838d0d231bfce377e2f");
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


    }
}
