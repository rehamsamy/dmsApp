package com.dmsegypt.dms.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Mohamed Abdallah on 17/02/2017.
 **/

public class utils {

    public static boolean isEmailValid(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * @param str
     * @return 0 for less than 6 characters <P>
     * 1 for more than 20 characters <P>
     * 2 for meet
     */
    public static short meetLengthValidation(String str) {
        final int length = str.length();
        if (length < 6) return 0;
        else if (length > 20) return 1;
        return 2;
    }

    public static boolean isMobileNumberValid(String mobileNumber) {
        if (mobileNumber.length() < 11) return false;
        Pattern MOBILE_NUMBER_PATTERN = Pattern
                .compile("^(?:01|\\\\+201)[0-2][0-9]{8}$");
        return MOBILE_NUMBER_PATTERN.matcher(mobileNumber).matches();
    }

    /**
     * play animation on a specific view.
     *
     * @param context
     * @param view        to play animation on
     * @param animationID reference to the wanted animation
     */
    public static void playAnimation(Context context, final View view, @AnimRes int animationID) {
        Animation animation = AnimationUtils.loadAnimation(context, animationID);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void promptToClose(final Activity activity) {
        new MaterialDialog.Builder(activity)
                .content(R.string.close_app)
                .positiveText(activity.getString(R.string.yes))
                .negativeText(activity.getString(R.string.no)).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                activity.finish();
            }
        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            }
        })
                .show();
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }




   /* public static String handleMessageDate(String date){
        SimpleDateFormat sdf=new SimpleDateFormat();
        try {
            Date date1=sdf.parse(date);
            if (DateUtils.isToday(date1.getTime())){
                 return new SimpleDateFormat("hh:mm a", Locale.US).format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    return date;
    }*/


}
