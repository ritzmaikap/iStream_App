package com.example.istream_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * I am creating a helper class to manage user login session.
 * I am using SharedPreferences to store simple login data like userId.
 */
public class AuthKeeper {

    // I am defining the name of my SharedPreferences file
    private static final String PREF_NAME = "istream_auth_pref";

    // I am defining a key to store the logged-in user's ID
    private static final String KEY_LOGGED_IN_USER_ID = "logged_in_user_id";

    public static void saveLoggedInUser(Context context, int userId) {
        // I am accessing SharedPreferences in private mode
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // I am saving the userId when the user logs in
        preferences.edit().putInt(KEY_LOGGED_IN_USER_ID, userId).apply();
    }

    public static int getLoggedInUserId(Context context) {
        // I am retrieving SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // I am getting the stored userId, default is -1 if no user is logged in
        return preferences.getInt(KEY_LOGGED_IN_USER_ID, -1);
    }

    public static boolean isLoggedIn(Context context) {
        // I am checking if a valid userId exists (not -1)
        return getLoggedInUserId(context) != -1;
    }

    public static void logout(Context context) {
        // I am clearing all stored data to log the user out
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}