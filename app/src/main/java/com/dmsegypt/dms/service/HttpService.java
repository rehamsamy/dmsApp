package com.dmsegypt.dms.service;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.Response.ResponseOtp;
import com.dmsegypt.dms.ux.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 19/02/2017.
 **/

public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            String cardId = intent.getStringExtra("card");
            verifyOtp(cardId , otp);
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String cardId , final String otp) {
        App.getInstance().getService().postOtp(cardId, otp).enqueue(new Callback<ResponseOtp>() {
            @Override
            public void onResponse(Call<ResponseOtp> call, Response<ResponseOtp> response) {
                if (response.body() != null) {
                    if (response.body().getCode() != 1){
                        new MaterialDialog.Builder(HttpService.this)
                                .content(response.body().getDetails())
                                .positiveText("Ok")
                                .onPositive(null)
                                .negativeText(null)
                                .onNegative(null)
                                .cancelable(true)
                                .show();
                    }else {
                        Intent intent = new Intent(HttpService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                // TODO: 19/02/2017 createLogin and move to mainActivity.
            }

            @Override
            public void onFailure(Call<ResponseOtp> call, Throwable t) {
                //// TODO: 19/02/2017
            }
        });
    }
}