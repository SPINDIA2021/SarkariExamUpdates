package com.gsbussiness.sarkariexamupdates.SarkariHelper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferClass {

    private static final String PREFERENCE_NAME = "sarkarinaukri";
    public static final String defaultinterstital1 = "";
    public static String TOKEN = "firebase_token";
    public static String NativeAds = "NativeAds";
    public static String Splash = "Splash";
    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(key)) {
            return preferences.getString(key, "");
        }
        return "";
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static String getDefault(Context context, String key,String dflt) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(key)) {
            return preferences.getString(key, dflt);
        }
        return dflt;
    }
}