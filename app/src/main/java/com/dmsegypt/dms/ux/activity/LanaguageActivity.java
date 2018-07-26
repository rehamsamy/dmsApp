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
import android.widget.Button;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.DataUtils.retrieveNotifications;

/**
 * Created by Mohamed Abdallah on 15/04/2017.
 **/

@SuppressWarnings("FieldCanBeLocal")
public class   LanaguageActivity extends AppCompatActivity {
    @BindView(R.id.english_button)
    Button englishButton;
    @BindView(R.id.arabic_button)
    Button arabicButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_language);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.english_button)
    public void chooseEnglish(){
        LocaleHelper.setLocale(this,"en");
        startActivity(new Intent(this,SplashActivity.class));
        finish();


    }
    @OnClick(R.id.arabic_button)
    public void chooseArabic(){
        LocaleHelper.setLocale(this,"ar");
        startActivity(new Intent(this,SplashActivity.class));
        finish();


    }


}
