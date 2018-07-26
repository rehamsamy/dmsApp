package com.dmsegypt.dms.ux.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by amr on 02/11/2017.
 */

public class UpdateViewerActivity extends AppIntro2 {


    //region variables
    boolean isHR;
    boolean isDMS;
    boolean isUser;
    boolean isProvider;

//endregion

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        isHR= App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_HR);
        isDMS=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);
        isUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_NORMAL);
        isProvider=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER);

        if(isHR){
            addSlide(AppIntroFragment.newInstance(getString(R.string.intro_last_update),"", R.drawable.logo,Color.parseColor("#518092")));
            addSlide(AppIntroFragment.newInstance(getString(R.string.action_notifications),getString(R.string.intro_notification), R.drawable.notifyhelper,Color.parseColor("#518092")));

            setBarColor(Color.parseColor("#05d2ec"));
            setSlideOverAnimation();

        }else if(isDMS){
            addSlide(AppIntroFragment.newInstance(getString(R.string.intro_last_update),"", R.drawable.logo,Color.parseColor("#518092")));
            addSlide(AppIntroFragment.newInstance(getString(R.string.action_notifications),getString(R.string.intro_notification), R.drawable.notifyhelper,Color.parseColor("#518092")));

            setBarColor(Color.parseColor("#05d2ec"));
            setSlideOverAnimation();

        }else if (isUser){
            addSlide(AppIntroFragment.newInstance(getString(R.string.intro_last_update),"", R.drawable.logo,Color.parseColor("#518092")));
            addSlide(AppIntroFragment.newInstance(getString(R.string.action_notifications),getString(R.string.intro_notification), R.drawable.notifyhelper,Color.parseColor("#518092")));

            setBarColor(Color.parseColor("#05d2ec"));
            setSlideOverAnimation();

        }else if(isProvider){
            addSlide(AppIntroFragment.newInstance(getString(R.string.intro_last_update),"", R.drawable.logo,Color.parseColor("#518092")));
            addSlide(AppIntroFragment.newInstance(getString(R.string.action_notifications),getString(R.string.intro_notification), R.drawable.notifyhelper,Color.parseColor("#518092")));

        }

    }



    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        IntentManager.startActivity(this, IntentManager.ACTIVITY_MAIN);
        finish();
    }
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        IntentManager.startActivity(this, IntentManager.ACTIVITY_MAIN);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
