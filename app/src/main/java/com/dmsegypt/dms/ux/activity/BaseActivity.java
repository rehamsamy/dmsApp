package com.dmsegypt.dms.ux.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.utils.LocaleHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by amr on 23/10/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResLayout());

        //region static toolbar declare at all Activity
        if (hasActionBar()){
            Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
            TextView titleTv=(TextView)findViewById(R.id.toolbar_title);
            titleTv.setText(getToolbarTitle());
            toolbar.setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                    R.drawable.logotitle:R.drawable.logotitle_ar);
           toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                    R.drawable.ic_toolbar_back_light:R.mipmap.ic_toolbar_arrow_ar);
           /* toolbar.setNavigationOnClickListener(new_image View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });*/
               setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


        }

        //endregion

    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    //region Funtion toolbar declare
    public  abstract boolean hasActionBar();
    public abstract  @LayoutRes int getResLayout();
    public abstract @StringRes int getToolbarTitle();
//endregion

    //region getlanguage
    public String getAppLanguage(){
      return LocaleHelper.getLanguage(this).equals("ar")? Constants.Language.AR:Constants.Language.EN;
    }

    //endregion


    //region BackPressed with handler 1sec. delayed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        ActivityOptions options= ActivityOptions.makeCustomAnimation(this,R.anim.translate_right_to_left,R.anim.slide_down);
        this.startActivity(intent,options.toBundle());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);

    }

    //endregion
}
