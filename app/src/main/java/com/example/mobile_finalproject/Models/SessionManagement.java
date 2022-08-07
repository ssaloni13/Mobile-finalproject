package com.example.mobile_finalproject.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;

/**
 * Class to maintain User sessions.
 */
public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int userLoggedIn;

    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    /**
     * Session management constructor that takes in a context and initializes sharedpreferences.
     *
     * @param context - context of the current activity
     */
    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.userLoggedIn = getUserLoggedIn();
    }

    /**
     * Method to save session of user whenever he is logged in.
     */
    public void saveSession(FirebaseUser user ) {
        String userID = user.getUid();
        this.userLoggedIn = 1;
        editor.putString(SESSION_KEY, userID).commit();
    }

    /**
     * Method to return the user id whose session is saved.
     *
     * @return - user ID who is logged in
     */
    public String getSession() {
        return sharedPreferences.getString(SESSION_KEY, "null");
    }

    /**
     * Method to get whether the user is logged in or not.
     *
     * @return - 1 if user is logged in, else -1 if user is not logged in
     */
    public int getUserLoggedIn() {
        return this.userLoggedIn;
    }

    /**
     * Method to set whether user is logged in or not.
     *
     * @param userLoggedIn - 1 for yes, -1 for no
     */
    public void setUserLoggedIn(int userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    /**
     * Method to remove the user's logged in session.
     */
    public void removeSession() {
        this.userLoggedIn = -1;
        editor.putString(SESSION_KEY, "null").commit();
    }
}
