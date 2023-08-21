package com.gsbussiness.sarkariexamupdates.AppInit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;


public class AppCheck extends Application {
    private static AppOpenManager appOpenManager;

    private static final String TAG = AppCheck.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(getApplicationContext());
        FirebaseApp.initializeApp(this);
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        appOpenManager = new AppOpenManager(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}