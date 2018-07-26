package com.dmsegypt.dms.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dmsegypt.dms.rest.model.User;
import com.google.gson.Gson;

/**
 * Created by Mohamed Abdallah on 19/02/2017.
 **/

public class PrefManager {

    private static final String PREF_NAME = "DMS";
    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER = "user";
    private static final String KEY_CURRENT_USER = "current_user";
    private static final String KEY_CARD_ID = "cardId";
    private static final String KEY_REG_ID = "regId";
    private static final String KEY_NOTIFICATION_PREF = "notification pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SHOW_UPDATE="show_update";
    private static final String KEY_NOTFI_COUNT ="key_notify_count" ;
    private static final String KEY_FIRST_PROVIDER_LOGIN ="key_first_provider_login" ;


    private static PrefManager prefManager;
    private SharedPreferences pref;
    private Editor editor;

    @SuppressLint("CommitPrefEdits")
    private PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0/*PRIVATE_MODE*/);
        editor = pref.edit();
    }

    public static PrefManager getInstance(Context context) {
        if (prefManager == null) prefManager = new PrefManager(context);
        return prefManager;
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public String getCardId() {
        return pref.getString(KEY_CARD_ID, "");
    }

    public void setCardId(String cardId) {
        editor.putString(KEY_CARD_ID, cardId);
        editor.commit();
    }

    public boolean getNotificationPref() {
        return pref.getBoolean(KEY_NOTIFICATION_PREF, true);
    }

    public void setNotificationPref(boolean value) {
        editor.putBoolean(KEY_NOTIFICATION_PREF, value).apply();
    }

    public void createLogin(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putString(KEY_CURRENT_USER, userJson);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public void setCurrentUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_CURRENT_USER, userJson);
        editor.commit();
    }

    public void makeLogout() {
        editor.remove(KEY_USER).remove(KEY_CURRENT_USER).remove(KEY_IS_LOGGED_IN).commit();
    }

    public void createRegister(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putString(KEY_CURRENT_USER,userJson);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.commit();
        setIsWaitingForSms(true);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn).commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public User getUser() {
        Gson gson = new Gson();
        String userJson = pref.getString(KEY_USER, null);
        return gson.fromJson(userJson, User.class);
    }

    public User getCurrentUser() {
        Gson gson = new Gson();
        String userJson = pref.getString(KEY_CURRENT_USER, null);
        return gson.fromJson(userJson, User.class);
    }

    public void saveString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public void saveRegId(String token) {
        editor.putString(KEY_REG_ID, token).apply();
    }

    public String getRegId() {
        return pref.getString(KEY_REG_ID, "-1");
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    public void setUsername(String username) {
        saveString(KEY_USERNAME, username);
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, "");
    }

    public void setPassword(String password) {
        saveString(KEY_PASSWORD, password);
    }
    public void setProviderFirstLogin() {
        editor.putBoolean(KEY_FIRST_PROVIDER_LOGIN, true).commit();
    }

    public boolean getProviderFirstLogin() {
        return pref.getBoolean(KEY_FIRST_PROVIDER_LOGIN, false);

    }
    public int isUpdateViewed() {

        return pref.getInt(KEY_SHOW_UPDATE, 0);
    }
    public void setViewUpdate() {
        editor.putInt(KEY_SHOW_UPDATE, 1)
                .apply();}

    public int getNotificationCount() {
        return pref.getInt(KEY_NOTFI_COUNT, 0);
    }
    public void setNotificationCount(int count){
        editor.putInt(KEY_NOTFI_COUNT,count).apply();
    }
}
