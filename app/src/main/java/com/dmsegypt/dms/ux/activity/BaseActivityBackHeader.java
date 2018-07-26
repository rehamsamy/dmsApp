package com.dmsegypt.dms.ux.activity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.Fragment.AboutFragment;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.dmsegypt.dms.ux.custom_view.nestedScrollView.NestedScrollAppBarLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class  BaseActivityBackHeader extends BaseActivity
        implements SwipeBackCoordinatorLayout.OnSwipeListener {
    //region Ui View

    @BindView(R.id.swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.appBar)
    NestedScrollAppBarLayout appBar;
    //endregion

    private AnimUtils a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        swipeBackView.setOnSwipeListener(this);
        init();
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_base_back_header;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_about;
    }

    private void init() {
        Intent intent=getIntent();

        Fragment fragment = null;
        switch (getIntent().getIntExtra(IntentManager.KEY_FRAGMENT, -1)) {
            case IntentManager.FRAGMENT_ABOUT:
                fragment = new AboutFragment();
                setTitle(getString(R.string.action_about));
              //  startActivity(new Intent(this,SingleApproval.class));
                break;
            case IntentManager.ACTIVITY_EDIT_PROFILE:
             // startRevealAnimation(intent);

           //     appBar.setVisibility(View.GONE);
             //   fragment = new EditProfileFragment();
             //   setTitle(" ");
                break;
            default:
                onBackPressed();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMainContent, fragment, fragment.getClass().getName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    private void startRevealAnimation(Intent intent) {
        a= new AnimUtils(container, intent, this, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onBackPressed() {
       if( getIntent().getIntExtra(IntentManager.KEY_FRAGMENT, -1)==IntentManager.ACTIVITY_EDIT_PROFILE)
        a.unRevealActivity();
        else super.onBackPressed();

    }

    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
            switch (dir) {
                case SwipeBackCoordinatorLayout.UP_DIR:
                    overridePendingTransition(0, R.anim.activity_slide_out_top);
                    break;

                case SwipeBackCoordinatorLayout.DOWN_DIR:
                    overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                    break;
            }
        }
    }

    //region Swipe handle
    @Override
    public boolean canSwipeBack(int dir) {
        return true;
    }

    @Override
    public void onSwipeProcess(float percent) {
        container.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
    }

    @Override
    public void onSwipeFinish(int dir) {
        finishActivity(dir);
    }
    //endregion
}
