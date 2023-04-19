package com.example.recipesapp;

import android.app.Application;
import android.util.Log;

import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Firebase.FireStorage;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FireStorage.initHelper(this);
        DataManager.initHelper();
        Log.d("ptt","create manger App");
    }
}
