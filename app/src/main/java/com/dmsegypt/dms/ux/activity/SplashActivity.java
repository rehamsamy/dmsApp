package com.dmsegypt.dms.ux.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.BuildConfig;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Config;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.DataStore;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseDataStore;
import com.dmsegypt.dms.rest.model.Response.ResponseLogin;
import com.dmsegypt.dms.rest.model.Response.ResponseLookups;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.DataUtils.retrieveNotifications;

/**
 * Created by Mohamed Abdallah on 15/04/2017.
 **/

@SuppressWarnings("FieldCanBeLocal")
public class   SplashActivity extends AppCompatActivity {
    //region Constants
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String EXTRA_MESSSAGE ="message" ;
  //endregion
    //region references
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @BindString(R.string.label_push_message)
    String notifictionlabel;
    private String language="En";
    //endregion
    //region UI VIEWS
    @BindView(R.id.logo)
    ImageView logoImgv;
    @BindView(R.id.progress)
    ImageView progress;
//endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        language= LocaleHelper.getLanguage(this).equals("ar")?Constants.Language.AR:Constants.Language.EN;
        animateLogo();
        checkForceUpdate();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_ALL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new_image push notification is received
                    String message = intent.getStringExtra(EXTRA_MESSSAGE);
                    DialogUtils.showNormalDialog(SplashActivity.this,notifictionlabel+message,
                            null,null,getString(R.string.ok),null,true);
                }
            }
        };
        displayFirebaseRegId();
    }

    private void animateLogo() {
        logoImgv.setTranslationY(-500f);
        logoImgv.animate().translationY(0).setDuration(500).setInterpolator(new FastOutSlowInInterpolator()).start();
        progress.setTranslationY(500f);
        progress.animate().translationY(0).setDuration(500).setInterpolator(new FastOutSlowInInterpolator()).start();
        Drawable drawable=progress.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable){
            ((AnimatedVectorDrawable)drawable).start();
        } else  if(drawable instanceof AnimatedVectorDrawableCompat){
            ((AnimatedVectorDrawableCompat)drawable).start();


        }
    }

    private void displayFirebaseRegId() {
        Log.e(TAG, "Firebase RegId : " + App.getInstance().getPrefManager().getRegId());
        if (App.getInstance().getPrefManager().isLoggedIn())
            Log.e(TAG, "User CareId : " + App.getInstance().getPrefManager().getUser().getCardId());
    }

    private void validateLogin() {
        if (App.getInstance().getPrefManager().isLoggedIn()) {
            App.getInstance().getService().login(
                    App.getInstance().getPrefManager().getUsername(),
                    App.getInstance().getPrefManager().getPassword(), language,
                    App.getInstance().getPrefManager().getRegId())
                    .enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            if (response.body() != null) {
                                Message message = response.body().getMessage();
                                if (message.getCode() == 1&&response.body().getUser()!=null) {
                                    App.getInstance().getPrefManager().createLogin(response.body().getUser());
                                    retrieveData();

                                } else {
                                    App.getInstance().getPrefManager().makeLogout();
                                    IntentManager.startActivityAndFinishMe(SplashActivity.this, IntentManager.ACTIVITY_AUTH);

                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            Log.e(TAG, "API login Error " + t.toString());
                            IntentManager.startActivityAndFinishMe(SplashActivity.this, IntentManager.ACTIVITY_AUTH);
                        }
                    });
        } else {
            retrieveData();
        }
    }

    private void retrieveData() {
        App.getInstance().getService().getLookups(language).enqueue(new Callback<ResponseLookups>() {
            @Override
            public void onResponse(Call<ResponseLookups> call, Response<ResponseLookups> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().getCode() == 1) {
                        Constants.providerTypesList = response.body().getProviderTypes();
                        Constants.providerCities = response.body().getCities();
                    }
                }
                IntentManager.startActivityAndFinishMe(SplashActivity.this, IntentManager.ACTIVITY_AUTH);
            }

            @Override
            public void onFailure(Call<ResponseLookups> call, Throwable t) {
                Log.e(TAG, "API lookups Error " + t.toString());
                IntentManager.startActivityAndFinishMe(SplashActivity.this, IntentManager.ACTIVITY_AUTH);
            }
        });
    }

    private void checkForceUpdate() {
        String packagename=BuildConfig.APPLICATION_ID;
       App.getInstance().getService().getStoreData(packagename).enqueue(new Callback<ResponseDataStore>() {
           @Override
           public void onResponse(Call<ResponseDataStore> call, Response<ResponseDataStore> response) {
               DataStore dataStore = new DataStore();
               if (response != null && response.body() != null) {
                   if (response.body().getList().size() != 0) {
                       dataStore = response.body().getList().get(0);
                       if (!dataStore.getVersion().equals(BuildConfig.VERSION_NAME)) {
                           progress.setVisibility(View.GONE);
                           DialogUtils.forceUpdateDialog(SplashActivity.this);

                       } else {
                          //retrieveNotifications();
                           validateLogin();

                       }
                   }
               }
           }


           @Override
           public void onFailure(Call<ResponseDataStore> call, Throwable t) {
            Log.e(TAG, "API getStoreData Error " + t.toString());
            validateLogin();

           }
       });


    }
}
