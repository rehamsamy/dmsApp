package com.dmsegypt.dms.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.content.DialogInterface;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;

import static com.dmsegypt.dms.app.IntentManager.openAppOnStore;

/**
 * Created by Mohamed Abdallah on 26/04/2017.
 **/

public class DialogUtils {

    private static MaterialDialog dialog;
    public static void forceUpdateDialog(final Activity activity){
        new MaterialDialog.Builder(activity)
                .content(R.string.update_msg)
                .positiveText(activity.getString(R.string.ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        openAppOnStore(activity.getApplicationContext());
                    }
                })
                .negativeText(activity.getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        activity.finish();
                    }
                })
                .cancelable(false)
                .show();
    }




    public static void showDialog(Activity activity,boolean Isshow){

        if (Isshow){
            dialog=new MaterialDialog.Builder(activity)
                    .content(R.string.msg_loading)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        }else {
            if (dialog!=null){
                dialog.dismiss();
                dialog=null;
            }
        }
    }




    /****
     * choose complaint type (Provider or Service)
     * @param activity
     * @param providerListener
     * @param serviceListener
     */
    public static void chooseComplainsDialog(final Activity activity, MaterialDialog.SingleButtonCallback providerListener,
                                             MaterialDialog.SingleButtonCallback serviceListener){
        new MaterialDialog.Builder(activity)
                .content(activity.getString(R.string.choose_complains_type))
                .positiveText(activity.getString(R.string.provider))
                .onPositive(providerListener)
                .negativeText(activity.getString(R.string.service))
                .onNegative(serviceListener)
                .cancelable(true)
                .show();
    }
public  static MaterialDialog progressDialog(Activity activity, @StringRes int contentres){

   return new MaterialDialog.Builder(activity)
            .content(contentres)
            .positiveText(null)
            .onPositive(null)
            .onNegative(null)
            .negativeText(null)
           .progress(true, 0)
           .cancelable(false)
            .show();
}

    public  static void showNormalDialog(Activity activity, String progressContent, MaterialDialog.SingleButtonCallback providerListener,
                                         MaterialDialog.SingleButtonCallback serviceListener,
                                         String postid,String negid,boolean iscanacel){

        new MaterialDialog.Builder(activity)
                .content(progressContent)
                .positiveText(postid)
                .onPositive(providerListener)
                .onNegative(serviceListener)
                .negativeText(negid)
                .cancelable(iscanacel)
                .show();
    }








    public static void waitingMessage (final Activity activity , final Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(
                activity).create();
        // Setting Dialog Title
        alertDialog.setTitle("Comming Soon");
        // Setting Dialog Message
        alertDialog.setMessage("Waiting for new_image Update...");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_build);
        // Setting OK Button
     /*  alertDialog.setOnCancelListener(new_image DialogInterface.OnCancelListener() {
           @Override
           public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(context,"Fine",Toast.LENGTH_LONG).show();
           }
       });*/



        // Showing Alert Message
        alertDialog.show();
    }
    /***
     * confirm dialog with Yes, No options
     * @param activity
     * @param contentMsg
     * @param yesListener
     * @param noListener
     */
    public static void ConfirmDialog(final Activity activity, String contentMsg, MaterialDialog.SingleButtonCallback yesListener,
                                             MaterialDialog.SingleButtonCallback noListener){
        new MaterialDialog.Builder(activity)
                .content(contentMsg)
                .positiveText(activity.getString(R.string.yes))
                .onPositive(yesListener)
                .negativeText(activity.getString(R.string.no))
                .onNegative(noListener)
                .cancelable(true)
                .show();
    }

}
