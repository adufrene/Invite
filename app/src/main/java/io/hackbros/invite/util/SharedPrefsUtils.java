package io.hackbros.invite.util;

import android.content.SharedPreferences;
import android.content.Context;

public class SharedPrefsUtils {
    private static final String SHARED_PREFS_FILE = "Shared Prefs file name";
    public static final String LOGGED_IN_KEY = "logged_in_key";

    public static SharedPreferences getSharedPrefs(Context ctx) {
        return ctx.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static void putString(Context ctx, String key, String value) {
        getSharedPrefs(ctx).edit().putString(key, value).apply(); 
    }

    public static void putBool(Context ctx, String key, boolean value) {
        getSharedPrefs(ctx).edit().putBoolean(key, value).apply();
    }

    public static String getString(Context ctx, String key, String defaultVal) {
        return getSharedPrefs(ctx).getString(key, defaultVal);
    }

    public static boolean getBool(Context ctx, String key, boolean defaultVal) {
        return getSharedPrefs(ctx).getBoolean(key, defaultVal);
    }

    public static boolean contains(Context ctx, String key) {
        return getSharedPrefs(ctx).contains(key);
    }
}
