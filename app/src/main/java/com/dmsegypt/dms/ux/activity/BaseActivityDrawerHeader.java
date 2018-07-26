package com.dmsegypt.dms.ux.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;

import com.dmsegypt.dms.ux.custom_view.nestedScrollView.NestedScrollAppBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class BaseActivityDrawerHeader extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,EditProfileActivity.OnImageUpdateListener {



//region declare variables and views
    @BindView(R.id.llHeaderContainer)
    LinearLayout llHeaderContainer;
    @BindView(R.id.fragment_home_appBar)
    NestedScrollAppBarLayout fragmentHomeAppBar;
    @BindView(R.id.flMainContent)
    FrameLayout flMainContent;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private boolean isProfiLeUpdated;


    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    //region Toolbar declaration
    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return 0;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    //endregion






    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base_drawer_header);
//        f = (RelativeLayout) findViewById(R.id.content_frame);
        flMainContent.addView(LayoutInflater.from(this).inflate(layoutResID, null));
        ButterKnife.bind(this);

    }


    /**
     * Called when an Item in the navigation menu is selected.
     *
     * @param item The selected Item
     * @return true to display the Item as the selected Item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view Item clicks here.
        setChildView(item.getItemId(), item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

    }


    @SuppressWarnings("UnusedParameters")
    private void setChildView(@IdRes int id, CharSequence title) {
        setHeader(id);
        setTitle(title.toString());
        Fragment fragment = null;
        Intent intent;
        switch (id) {
            case R.id.action_about:
                IntentManager.startBaseBackActivity(this, IntentManager.FRAGMENT_ABOUT,null);
                break;
        }


        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMainContent, fragment, fragment.getClass().getName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .addToBackStack(fragment.getClass().getName())
                    .commit();

        }
    }




    private void setHeader(@IdRes int id) {

        switch (id) {
            case R.id.action_network:
                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.layout_home_header, null);
                LinearLayout llHomeHeader = ButterKnife.findById(view, R.id.llHomeHeader);
                llHeaderContainer.removeAllViews();
                llHeaderContainer.addView(llHomeHeader);
                setTitle(R.string.search);
                break;

            case R.id.action_complaints:
                chooseComplainsType();
                break;
            default:
                llHeaderContainer.removeAllViews();
                break;
        }
    }


    //region Choose Complains Type
    private void chooseComplainsType() {
        DialogUtils.chooseComplainsDialog(this, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.layout_complains_header, null);
                LinearLayout llHomeHeader = ButterKnife.findById(view, R.id.llHomeHeader);
                llHeaderContainer.removeAllViews();
                llHeaderContainer.addView(llHomeHeader);
                setTitle(getString(R.string.action_complaints));

            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                setTitle(getString(R.string.action_complaints));
            }
        }
        );

    }

    //endregion



    @Override
    public void onImageUpdate() {
        isProfiLeUpdated=true;
    }
}
