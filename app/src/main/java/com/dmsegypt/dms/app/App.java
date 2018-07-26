package com.dmsegypt.dms.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.ApiService;
import com.dmsegypt.dms.rest.GooglePlayRestClient;
import com.dmsegypt.dms.rest.RestClient;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mohamed Abdallah on 19/02/2017.
 **/

public class App extends Application {

    private static App app;
    private static ApiService service;
    private PrefManager prefManager;
    private static ApiService goglePlayService;



    public static synchronized App getInstance() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("arabic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        service = (new RestClient()).getApiService();
        goglePlayService=(new GooglePlayRestClient()).getApiService();
        prefManager = PrefManager.getInstance(getApplicationContext());
        app = this;
        enableLeakCanary();
        enableCrashlytics();
    }

    public ApiService getService() {
        return service;
    }
    public ApiService getGooglePlayService() {
        return goglePlayService;
    }
    public PrefManager getPrefManager() {
        return prefManager;
    }

    private void enableLeakCanary() {
        if (com.dmsegypt.dms.BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
            // Normal app init code...
        }
    }

    private void enableCrashlytics() {
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(com.dmsegypt.dms.BuildConfig.DEBUG).build())
                .build();
        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
        MultiDex.install(this);
    }

}
