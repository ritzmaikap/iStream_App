package com.example.istream_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * This helper class stores the logged-in user's session.
 * I am using SharedPreferences because it is simple for basic login state.
 */
public class AuthKeeper {

    private static final String PREF_NAME = "istream_auth_pref";
    private static final String KEY_LOGGED_IN_USER_ID = "logged_in_user_id";

    public static void saveLoggedInUser(Context context, int userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_LOGGED_IN_USER_ID, userId).apply();
    }

    public static int getLoggedInUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_LOGGED_IN_USER_ID, -1);
    }

    public static boolean isLoggedIn(Context context) {
        return getLoggedInUserId(context) != -1;
    }

    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}