package com.dmsegypt.dms.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Config;
import com.dmsegypt.dms.rest.model.Response.ResponseUserToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 11/03/2017.
 **/

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_ALL);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        App.getInstance().getPrefManager().saveRegId(token);
        if (App.getInstance().getPrefManager().isLoggedIn())
            App.getInstance().getService().updateToken(App.getInstance().getPrefManager().getUser().getCardId(),
                    token).enqueue(new Callback<ResponseUserToken>() {
                @Override
                public void onResponse(Call<ResponseUserToken> call, Response<ResponseUserToken> response) {

                }

                @Override
                public void onFailure(Call<ResponseUserToken> call, Throwable t) {

                }
            });
    }
}