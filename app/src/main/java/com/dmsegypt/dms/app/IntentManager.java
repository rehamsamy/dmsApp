package com.dmsegypt.dms.app;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.BuildConfig;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.ux.activity.AddFamilyMember;
import com.dmsegypt.dms.ux.activity.AddHrRequestActivity;
import com.dmsegypt.dms.ux.activity.AddMassengerActivity;
import com.dmsegypt.dms.ux.activity.AddMemberActivity;
import com.dmsegypt.dms.ux.activity.AddOrderActivity;
import com.dmsegypt.dms.ux.activity.AddProviderActivity;
import com.dmsegypt.dms.ux.activity.AdminLoginActivity;
import com.dmsegypt.dms.ux.activity.AdminViewAccountsActivity;
import com.dmsegypt.dms.ux.activity.AllApprovalListActivity;
import com.dmsegypt.dms.ux.activity.ApprovalActivity;
import com.dmsegypt.dms.ux.activity.AuthActivity;
import com.dmsegypt.dms.ux.activity.BaseActivityBackHeader;
import com.dmsegypt.dms.ux.activity.BatchActivity;
import com.dmsegypt.dms.ux.activity.CheckStatusActivity;
import com.dmsegypt.dms.ux.activity.ChronicActivity;
import com.dmsegypt.dms.ux.activity.ComplainsActivity;
import com.dmsegypt.dms.ux.activity.EditCardActivity;
import com.dmsegypt.dms.ux.activity.EditProfileActivity;
import com.dmsegypt.dms.ux.activity.EmployeesActivity;
import com.dmsegypt.dms.ux.activity.FamilyMemberActivity;
import com.dmsegypt.dms.ux.activity.ListIndemnityRequests;
import com.dmsegypt.dms.ux.activity.UserApprovalListActivity;
import com.dmsegypt.dms.ux.activity.MainActivity;
import com.dmsegypt.dms.ux.activity.MainMassengerActivity;
import com.dmsegypt.dms.ux.activity.NetworkActivity;
import com.dmsegypt.dms.ux.activity.NotificationActivity;
import com.dmsegypt.dms.ux.activity.OtpActivity;
import com.dmsegypt.dms.ux.activity.PharmMedicineOrdersActivity;
import com.dmsegypt.dms.ux.activity.SendApprovalActivity;
import com.dmsegypt.dms.ux.activity.UserMedicineOrdersActivity;
import com.dmsegypt.dms.ux.activity.PhoneVerficationActivity;
import com.dmsegypt.dms.ux.activity.ProviderActivity;
import com.dmsegypt.dms.ux.activity.SettingsActivity;
import com.dmsegypt.dms.ux.activity.UpdateViewerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Handler;


/**
 * Created by Mohamed Abdallah on 12/03/2017.
 **/

@SuppressWarnings("WeakerAccess")
public class IntentManager {









    public static final int ACTIVITY_AUTH = 0;
    public static final int ACTIVITY_MAIN = 1;
    public static final int ACTIVITY_AGENT = 2;
    public static final int ACTIVITY_NOTIFICATION = 3;
    public static final int ACTIVITY_SETTINGS = 4;
    public static final int ACTIVITY_OTP = 5;

    /**
     * system Approval
     */
    public static final int ACTIVITY_APPROVAL=6;

    public static final String KEY_FRAGMENT = "fragment_index";
    public static final int FRAGMENT_ABOUT = 0;
    public static final int ACTIVITY_EDIT_PROFILE = 1;

    public static final int KEY_CALL_PERMISSION = 100;
    public static final int KEY_STORAGE_PERMISSION = 101;

    public static final int KEY_PICK_IMAGE = 200;
    public static final int ACTIVITY_VIEW_UPDATE =7 ;

    public static final Handler handler=new Handler();
    public static final int ACTIVITY_ACCOUNTS =8 ;
    public static final int ACTIVITY_ADMIN =9 ;


    public static void startSettingsActivity(Activity a) {
        Intent intent = new Intent(a, SettingsActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startAddProviderActivity(Activity a){
        Intent intent = new Intent(a, AddProviderActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startAddMassengerActivity(Activity a){
        Intent intent = new Intent(a, AddMassengerActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startPharmacyOrdersActivity(Activity a){
        Intent intent = new Intent(a, UserMedicineOrdersActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startAddOrderActivity(Activity a){
        Intent intent = new Intent(a, AddOrderActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }


    public static void startApprovalActivity(Activity a) {
        Intent intent = new Intent(a, ApprovalActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }


    public static void startRequestsActivity(final Activity a, View v) {
        Intent intent = new Intent(a, SendApprovalActivity.class);
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);
        intent.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        ActivityCompat.startActivity(a, intent, null);
        a.overridePendingTransition(0, 0);


    }

    public static void startChronicActivity(Activity a){
        Intent intent = new Intent(a, ChronicActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }
    public static void startHREditCard(Activity a){
        Intent intent = new Intent(a, AddHrRequestActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();

    }

    public static void startComplaintsActivity(Activity a, int complaintType) {


        Intent intent = new Intent(a, ComplainsActivity.class);
        intent.putExtra(Constants.KEY_COMPLAINTS_TYPE, complaintType);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startFamilyMembers(Activity a) {
        Intent intent = new Intent(a, FamilyMemberActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }



    public static void startCheckStatus(Activity a) {
        Intent intent = new Intent(a, CheckStatusActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startNetwork(Activity a) {
        Intent intent = new Intent(a, NetworkActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }




    public static void startCompanyEmployees(Activity a,boolean isApprovalActivity) {

        Intent intent = new Intent(a, EmployeesActivity.class);
        intent.putExtra(EmployeesActivity.EXTRA_ACTIVITY_TYPE,isApprovalActivity);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
        //  a.overridePendingTransition(R.anim.activity_in, 0);
    }

    public static void startListUserRequests(Activity a, String CardId){
        Intent intent = new Intent(a, UserApprovalListActivity.class);
        intent.putExtra("CARD_ID",CardId);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();




    }

    public static void startRequestsActivity(Activity a){
        Intent intent = new Intent(a, SendApprovalActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();




    }

    public static void startRequestslistActivity(Activity a){
        Intent intent = new Intent(a, AllApprovalListActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }


    public static void startBatchActivity(MainActivity a) {
        Intent intent = new Intent(a, BatchActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
        a.finish();
    }

    public static void startAddFamilyMembers(FamilyMemberActivity a) {
        Intent intent = new Intent(a, AddFamilyMember.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(intent,options.toBundle());
    }

    public static void startAddMembers(Activity a, int type) {

        Intent intent = new Intent(a, AddMemberActivity.class);
        intent.putExtra(Constants.KEY_ADD_TYPE, type);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.slide_in_right,R.anim.slide_out_left);
        a.startActivity(intent,options.toBundle());
        a.finish();
        //  a.overridePendingTransition(R.anim.activity_in, 0);
    }

    public static void startEditCardActivity(Activity a ,String cardId){
        Intent intent = new Intent(a, EditCardActivity.class);
        intent.putExtra("CARD_ID",cardId);
        a.startActivity(intent);

        a.overridePendingTransition(R.anim.activity_in, 0);

    }



    public static void startActivity(Activity a, int target) {
        Intent intent = null;
        ActivityOptions options=null;
        switch (target) {
            case ACTIVITY_AUTH:
                intent = new Intent(a, AuthActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;
            case ACTIVITY_MAIN:
                intent = new Intent(a, MainActivity.class);
                options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
                a.startActivity(intent,options.toBundle());
                a.finish();
                break;
            case ACTIVITY_AGENT:
                intent = new Intent(a, ProviderActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;
            case ACTIVITY_ADMIN:
                intent = new Intent(a, AdminLoginActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;

            case ACTIVITY_NOTIFICATION:
                intent = new Intent(a, NotificationActivity.class);
                options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
                a.startActivity(intent,options.toBundle());
                a.finish();
                break;
            case ACTIVITY_SETTINGS:
                intent = new Intent(a, SettingsActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;
            case ACTIVITY_OTP:
                intent = new Intent(a, PhoneVerficationActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;

            case ACTIVITY_VIEW_UPDATE:
                intent = new Intent(a, UpdateViewerActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;

            /**
             * system Approval case
             */
            case ACTIVITY_APPROVAL:
                intent=new Intent(a,ApprovalActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;
            case ACTIVITY_ACCOUNTS:
                intent=new Intent(a,AdminViewAccountsActivity.class);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;




        }

    }
/*
    public static void startActivity(Activity a, int target, int animResId) {
        Intent intent = null;
        ActivityOptions options=null;
        switch (target) {
            case ACTIVITY_AUTH:
                intent = new_image Intent(a, AuthActivity.class);
                break;
            case ACTIVITY_MAIN:
                intent = new_image Intent(a, MainActivity.class);
                break;
            case ACTIVITY_AGENT:
                intent = new_image Intent(a, ProviderActivity.class);
                break;
            case ACTIVITY_NOTIFICATION:
                intent = new_image Intent(a, NotificationActivity.class);
                options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
                a.startActivity(intent,options.toBundle());
                a.finish();
                break;
//            case ACTIVITY_PROFILE:
//                intent = new_image Intent(a, ProfileActivity.class);
//                break;
            case ACTIVITY_SETTINGS:
                intent = new_image Intent(a, SettingsActivity.class);
                break;
            case ACTIVITY_OTP:
                intent = new_image Intent(a, OtpActivity.class);
                break;


        }
        a.startActivity(intent);
        a.overridePendingTransition(animResId, 0);
    }
*/
    public static void startActivityWithExtra(Activity a, int target, @NonNull String key, @NonNull Object extra) {
        Intent intent = null;
        ActivityOptions options=null;
        switch (target) {
            case ACTIVITY_AUTH:
                intent = new Intent(a, AuthActivity.class);
                break;
            case ACTIVITY_MAIN:
                intent = new Intent(a, MainActivity.class);
                break;
            case ACTIVITY_AGENT:
                intent = new Intent(a, ProviderActivity.class);
                break;
            case ACTIVITY_NOTIFICATION:
              /*  intent = new_image Intent(a, NotificationActivity.class);
                options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
                a.startActivity(intent,options.toBundle());
                a.finish();*/
                break;
            case ACTIVITY_SETTINGS:
                intent = new Intent(a, SettingsActivity.class);
                break;
            case ACTIVITY_OTP:
                intent = new Intent(a, OtpActivity.class);
                break;
        }
        if (intent != null) {
            if (extra instanceof String) {
                intent.putExtra(key, ((String) extra));
            } else if (extra instanceof Integer) {
                intent.putExtra(key, ((Integer) extra));
            } else if (extra instanceof Boolean) {
                intent.putExtra(key, ((Boolean) extra));
            } else if (extra instanceof Float) {
                intent.putExtra(key, ((Float) extra));
            } else if (extra instanceof Long) {
                intent.putExtra(key, ((Long) extra));
            } else {
                intent.putExtra(key, extra.toString());
            }

            a.startActivity(intent);
            a.overridePendingTransition(R.anim.activity_in, 0);
        }
    }

    public static void startActivityAndFinishMe(Activity a, int target) {
        startActivity(a, target);
        a.finish();
    }

//    public static void startProfileActivityWithTransitionAnim(Activity a, View avatar) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Intent intent = new_image Intent(a, ProfileActivity.class);
////                    intent.putExtra(MainActivity.KEY_ME_ACTIVITY_PAGE_POSITION, page);
//            ActivityOptionsCompat options = ActivityOptionsCompat
//                    .makeSceneTransitionAnimation(
//                            a,
//                            Pair.create(
//                                    avatar/*.navigationView.getHeaderView(0).findViewById(R.id.navigation_drawer_list_header_avatar)*/,
//                                    a.getString(R.string.transition_user_avatar)));
//            ActivityCompat.startActivity(a, intent, options.toBundle());
//        } else {
//            Intent intent = new_image Intent(a, ProfileActivity.class);
////                    intent.putExtra(ProfileActivity.KEY_ME_ACTIVITY_PAGE_POSITION, page);
//            a.startActivity(intent);
//            a.overridePendingTransition(R.anim.activity_in, 0);
//        }
//    }

    public static void startBaseBackActivity(final Activity a, int fragment, View v) {

        switch (fragment) {
            case FRAGMENT_ABOUT:
                Intent intent = new Intent(a, BaseActivityBackHeader.class);
                intent.putExtra(KEY_FRAGMENT, FRAGMENT_ABOUT);
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_in, 0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        a.finish();
                    }
                }, 1000) ;

                break;
            case ACTIVITY_EDIT_PROFILE:
                Intent intent1=new Intent(a, EditProfileActivity.class);
                int revealX = (int) (v.getX() + v.getWidth() / 2);
                int revealY = (int) (v.getY() + v.getHeight() / 2);
                intent1.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_X, revealX);
                intent1.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_Y, revealY);
                intent1.putExtra(KEY_FRAGMENT, ACTIVITY_EDIT_PROFILE);
                a.startActivity(intent1);
                break;
        }

    }

    //region Make call handle
    public static void makeCall(final Activity activity, String phoneNumber) {
        if (phoneNumber.contains("-")) {
            CharSequence[] numbers = phoneNumber.split("-");
            new MaterialDialog.Builder(activity)
                    .title("Select number to call")
                    .items(numbers)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            call(activity, text);
                        }
                    })
                    .show();
        } else if (!TextUtils.isEmpty(phoneNumber)) call(activity, phoneNumber);
        else

            new MaterialDialog.Builder(activity)
                    .content("No phone number available")
                    .positiveText("Ok")
                    .onPositive(null)
                    .negativeText(null)
                    .onNegative(null)
                    .cancelable(true)
                    .show();

    }

    private static void call(Activity activity, CharSequence phoneNumber) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, KEY_CALL_PERMISSION);
            return;
        }
        activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }
    //endregion

    public static void makeShare(Activity activity, String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void makeNavigate(Activity activity, String address) {
        if (TextUtils.isEmpty(address))
            new MaterialDialog.Builder(activity)
                    .content(activity.getString(R.string.error_not_available_address))
                    .positiveText("Ok")
                    .onPositive(null)
                    .negativeText(null)
                    .onNegative(null)
                    .cancelable(true)
                    .show();
        else
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address)));
    }
/*
    public static void pickImg(EditProfileFragment fragment) {
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fragment.getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    KEY_STORAGE_PERMISSION);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //  Intent intent=new_image Intent(MediaStore.EXTRA_OUTPUT);
        //  intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        fragment.startActivityForResult(intent, KEY_PICK_IMAGE);
    }*/

    //region Make force update handle
    public static void openAppOnStore(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = BuildConfig.APPLICATION_ID;
        Intent storeIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our storeIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(storeIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                storeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                storeIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                storeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                storeIntent.setComponent(componentName);
                context.startActivity(storeIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            context.startActivity(webIntent);
        }
    }


    public static void startReplyActivity(Activity a,Class b,String reuestId,View v) {
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);
        Intent intent = new Intent(a, b);
        intent.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("REQUEST_ID",reuestId);
        ActivityCompat.startActivity(a, intent, null);

        a.overridePendingTransition(0, 0);

    }

public static void startRevealActivity(Activity a,Class b,View v) {
    int revealX = (int) (v.getX() + v.getWidth() / 2);
    int revealY = (int) (v.getY() + v.getHeight() / 2);
    Intent intent = new Intent(a, b);
    intent.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_X, revealX);
    intent.putExtra(AnimUtils.EXTRA_CIRCULAR_REVEAL_Y, revealY);
    ActivityCompat.startActivity(a, intent, null);

    a.overridePendingTransition(0, 0);

}

    public static void startIndemnityActivity(MainActivity a) {
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH-mm-ss", Locale.US);
        Date dateobj = new Date();
        String subfolder=df.format(dateobj);

        Intent newintent = new Intent(a, ListIndemnityRequests.class);
        newintent.putExtra("SUBFOLDERTIME",subfolder);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(newintent,options.toBundle());

    }

    public static void StartMassengerActivity(Activity a){

        Intent newintent = new Intent(a, MainMassengerActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(newintent,options.toBundle());
        a.finish();

    }

    public static void startAdminActivity(Activity a) {
        Intent newintent = new Intent(a, AdminLoginActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(newintent,options.toBundle());
        a.finish();
    }
    public static void startOrderActivity(Activity a) {
        Intent newintent = new Intent(a, PharmMedicineOrdersActivity.class);
        ActivityOptions options=ActivityOptions.makeCustomAnimation(a,R.anim.translate_left_to_right,R.anim.slide_down);
        a.startActivity(newintent,options.toBundle());
        a.finish();
    }




    //endregion
}
