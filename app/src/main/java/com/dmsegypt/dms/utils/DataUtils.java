package com.dmsegypt.dms.utils;

import android.os.Build;
import android.util.Log;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.NotificationItem;
import com.dmsegypt.dms.rest.model.Response.ResponseGetNotifications;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 15/04/2017.
 **/

public class DataUtils {
    public static String getTypeNameById(String id) {
        String name = "";
        for (int i = 0; i < Constants.providerTypesList.size(); i++) {
            if (Constants.providerTypesList.get(i).getId().equals(id)) {
                name = Constants.providerTypesList.get(i).getName();
                break;
            }
        }
        return name;
    }
    public static boolean hasCardID(String cardid){
        return cardid.split("-").length==4;
    }


    public static int getTypeIconById(String id) {
        switch (id){
            //hospital
            case "1":
                return R.drawable.hospital;
            //pharmacy
            case "2":
                return R.drawable.pharmacy;
            //Laboratory
            case "3":
                return R.drawable.test_tube;
            //Radiology
            case "4":
                return R.drawable.radiology;
            //Doctor
            case "5":
                return R.drawable.doctor;
            //physiotherapy
            case "6":
                return R.drawable.physical_therapy_filled;
            //Dental Center
            case "7":
                return R.drawable.tooth_filled;
            //Optical Center
            case "8":
                return R.drawable.invisible;

            case "9":
                return R.drawable.unlock;
            case "10":
                return R.drawable.padlock;


            default:
                return R.drawable.no_image;
        }

    }

    //region To get the device model
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    //endregion

    public static void retrieveNotifications() {
        if (!App.getInstance().getPrefManager().isLoggedIn()) return;
        App.getInstance().getService().getNotifications(App.getInstance().getPrefManager().getCurrentUser().getCardId(), "en")
                .enqueue(new Callback<ResponseGetNotifications>() {
                    @Override
                    public void onResponse(Call<ResponseGetNotifications> call, Response<ResponseGetNotifications> response) {
                        if (response.body() != null){
                            Message message = response.body().getMessage();
                            if (message.getCode() == 1) {
                                if (response.body().getList() != null)
                                    Constants.Notifications = response.body().getList();
                                else Constants.Notifications = new ArrayList<NotificationItem>();
                            } else {
                                //// TODO: 20/04/2017 on other codes
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGetNotifications> call, Throwable t) {
                        Log.e("API GetNotify error", t.toString());
                    }
                });
    }
}
