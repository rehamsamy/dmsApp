package com.dmsegypt.dms.ux.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class ProfileActivity extends BaseActivity
        implements
        SwipeBackCoordinatorLayout.OnSwipeListener,
        Toolbar.OnMenuItemClickListener {


    //region declare View and Variables
    @BindView(R.id.activity_user_avatar)
    CircleImageView activityUserAvatar;
    @BindView(R.id.activity_user_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;
    @BindView(R.id.activity_user_container)
    CoordinatorLayout container;
    @BindView(R.id.activity_profile_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_profile_title)
    TextView title;
    @BindInt(R.integer.direction)
    int direction;

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
    }


    //region declare Toolbar
    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    //endregion


    //init View
    private void initView() {
        swipeBackView.setOnSwipeListener(this);
        toolbar.inflateMenu(R.menu.activity_profile_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setBackgroundResource(direction==0?R.drawable.logotitle:R.drawable.logotitle_ar);
        toolbar.setNavigationIcon(direction==0?R.drawable.ic_toolbar_back_light:R.mipmap.ic_toolbar_arrow_ar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity(SwipeBackCoordinatorLayout.DOWN_DIR);
            }
        });
    }



    //region SwipeBack
    @Override
    public boolean canSwipeBack(int dir) {
        return true;
    }

    @Override
    public void onSwipeProcess(float percent) {
//        statusBar.setAlpha(1 - percent);
        container.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
    }

    @Override
    public void onSwipeFinish(int dir) {
//        statusBar.setAlpha(1 - percent);
        finishActivity(dir);
    }


    //endregion


    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        onBackPressed();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
             //   Toast.makeText(getApplicationContext(), "edit clicked", Toast.LENGTH_SHORT).show();
           //     SuperToast.create(ProfileActivity.this, "edit Clicked", 3000).show();

                break;
        }
        return true;
    }
}
