package com.dmsegypt.dms.ux.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.ux.Fragment.MainMassengerFragment;
import com.dmsegypt.dms.ux.Fragment.MassengerApprovalFragment;
import com.dmsegypt.dms.ux.Fragment.MassengerDoneFragment;
import com.dmsegypt.dms.ux.Fragment.MassengerHoldFragment;
import com.dmsegypt.dms.ux.Fragment.MassengerPendFragment;
import com.dmsegypt.dms.ux.dialogs.FilterDetailDialog;
import com.dmsegypt.dms.ux.dialogs.MoveMassengerDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import it.sephiroth.android.library.bottomnavigation.BadgeProvider;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import it.sephiroth.android.library.bottomnavigation.FloatingActionButtonBehavior;
import it.sephiroth.android.library.bottonnavigation.BuildConfig;

@TargetApi (Build.VERSION_CODES.KITKAT_WATCH)
public class MainMassengerActivity extends MassengerBaseActivity implements BottomNavigation.OnMenuItemSelectionListener {
    private BottomNavigation mBottomNavigation;
    private ViewPager mViewPager;
    private SystemBarTintManager mSystemBarTint;
    private boolean mTranslucentStatus;
    private boolean mTranslucentStatusSet;
    private boolean mTranslucentNavigation;
    private boolean mTranslucentNavigationSet;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    static boolean isDMSUser;


    @Override
    public void finishActivity(int requestCode) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutResId());
        initView();
        initializeBottomNavigation(savedInstanceState);
        initializeUI(savedInstanceState);
    }

    protected int getActivityLayoutResId() {return R.layout.activity_main_massenger;}


    private void initializeUI(Bundle savedInstanceState) {
        isDMSUser= App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        if (null != floatingActionButton) {

            if(isDMSUser){
                floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));


            }else {
                floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

            }
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isDMSUser){
                        IntentManager.startAddMassengerActivity(MainMassengerActivity.this);
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));


                    }else {

                    /*
                        final View root = findViewById(R.id.CoordinatorLayout01);
                        Snackbar snackbar = Snackbar.make(root, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction(
                                        "Action",
                                        null
                                );
                        snackbar.show();*/
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                        IntentManager.startAddOrderActivity(MainMassengerActivity.this);

                    }


                }
            });

            if (hasTranslucentNavigation()) {
                final ViewGroup.LayoutParams params = floatingActionButton.getLayoutParams();
                if (CoordinatorLayout.LayoutParams.class.isInstance(params)) {
                    CoordinatorLayout.LayoutParams params1 = (CoordinatorLayout.LayoutParams) params;
                    if (FloatingActionButtonBehavior.class.isInstance(params1.getBehavior())) {
                        ((FloatingActionButtonBehavior) params1.getBehavior()).setNavigationBarHeight(getNavigationBarHeight());
                    }
                }
            }
        }

        final ViewPager viewPager = getViewPager();
        if (null != viewPager) {

            getBottomNavigation().setOnMenuChangedListener(new BottomNavigation.OnMenuChangedListener() {
                @Override
                public void onMenuChanged(final BottomNavigation parent) {

                    viewPager.setAdapter(new ViewPagerAdapter(MainMassengerActivity.this, parent.getMenuItemCount()));
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(
                                final int position, final float positionOffset, final int positionOffsetPixels) { }

                        @Override
                        public void onPageSelected(final int position) {
                            if(position == 4){
                                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.logotitleblue:R.drawable.logotitle_ar);
                                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.massenger_arrow_back_blue:R.mipmap.ic_toolbar_arrow_ar);
                                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            }else if(position == 0){
                                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.logotitle:R.drawable.logotitle_ar);
                                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.ic_toolbar_back_light:R.mipmap.ic_toolbar_arrow_ar);
                                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colormain)));
                            }else if(position == 2){
                                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.logotitleyellow:R.drawable.logotitle_ar);
                                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.massenger_arrow_back_yellow:R.mipmap.ic_toolbar_arrow_ar);
                                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentyellow)));

                            }else if(position == 3){
                                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.logotitlered:R.drawable.logotitle_ar);
                                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.massenger_arrow_back_red:R.mipmap.ic_toolbar_arrow_ar);
                                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentRed)));


                            }else if(position == 1){
                                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.logotitlegreen:R.drawable.logotitle_ar);
                                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                                        R.drawable.massenger_arrow_back_green:R.mipmap.ic_toolbar_arrow_ar);
                                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentGreen)));


                            }
                            if (getBottomNavigation().getSelectedIndex() != position) {
                                getBottomNavigation().setSelectedIndex(position, false);
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(final int state) {

                        }
                    });
                }
            });

        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void initializeBottomNavigation(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            getBottomNavigation().setDefaultSelectedIndex(0);
            final BadgeProvider provider = getBottomNavigation().getBadgeProvider();
            provider.show(R.id.bbn_item3);
            provider.show(R.id.bbn_item4);
        }
    }

    public BottomNavigation getBottomNavigation() {
        if (null == mBottomNavigation) {
            mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
            if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_HR))
            mBottomNavigation.inflateMenu(R.menu.hr_massenger_menu);
            else
                mBottomNavigation.inflateMenu(R.menu.bottombar_menu_5items);

        }
        return mBottomNavigation;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mViewPager = (ViewPager) findViewById(R.id.ViewPager01);
        mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        if (null != mBottomNavigation) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "arabic.ttf");
            mBottomNavigation.setOnMenuItemClickListener(this);
            mBottomNavigation.setDefaultTypeface(typeface);
        }
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_Massenger;
    }

    private void initView() {
        final int statusbarHeight = getStatusBarHeight();
        final boolean translucentStatus = hasTranslucentStatusBar();
        final boolean translucentNavigation = hasTranslucentNavigation();
        BottomNavigation.DEBUG = BuildConfig.DEBUG;
        final ViewGroup root = (ViewGroup) findViewById(R.id.CoordinatorLayout01);
        mViewPager = (ViewPager) findViewById(R.id.ViewPager01);
        mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_HR))
            mBottomNavigation.inflateMenu(R.menu.hr_massenger_menu);
        else
            mBottomNavigation.inflateMenu(R.menu.bottombar_menu_5items);

        final CoordinatorLayout coordinatorLayout;
        if (root instanceof CoordinatorLayout) {
            coordinatorLayout = (CoordinatorLayout) root;
        } else {
            coordinatorLayout = null;
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        TextView titleTv=(TextView)findViewById(R.id.toolbar_title);
        titleTv.setText(getToolbarTitle());
        toolbar.setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                R.drawable.logotitle:R.drawable.logotitle_ar);
        toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                R.drawable.ic_toolbar_back_light:R.mipmap.ic_toolbar_arrow_ar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (translucentStatus) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) root.getLayoutParams();
            params.topMargin = -statusbarHeight;

            params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            params.topMargin = statusbarHeight;
        }

        if (translucentNavigation) {
            final ViewPager viewPager = getViewPager();
            if (null != viewPager) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
                params.bottomMargin = -getNavigationBarHeight();
            }
        }

    }

    @TargetApi (19)
    public boolean hasTranslucentStatusBar() {
        if (!mTranslucentStatusSet) {
            if (Build.VERSION.SDK_INT >= 19) {
                mTranslucentStatus =
                        ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                                == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                mTranslucentStatus = false;
            }
            mTranslucentStatusSet = true;
        }
        return mTranslucentStatus;
    }

    public SystemBarTintManager getSystemBarTint() {
        if (null == mSystemBarTint) {
            mSystemBarTint = new SystemBarTintManager(this);
        }
        return mSystemBarTint;
    }

    @TargetApi(19)
    public boolean hasTranslucentNavigation() {
        if (!mTranslucentNavigationSet) {
            final SystemBarTintManager.SystemBarConfig config = getSystemBarTint().getConfig();
            if (Build.VERSION.SDK_INT >= 19) {
                boolean themeConfig =
                        ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                                == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                mTranslucentNavigation = themeConfig && config.hasNavigtionBar() && config.isNavigationAtBottom()
                        && config.getNavigationBarHeight() > 0;
            }
            mTranslucentNavigationSet = true;
        }
        return mTranslucentNavigation;
    }

    public int getNavigationBarHeight() {
        return getSystemBarTint().getConfig().getNavigationBarHeight();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMenuItemSelect(final int itemId, final int position, final boolean fromUser) {
        if (fromUser) {
            if(position == 4){
                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.logotitleblue:R.drawable.logotitle_ar);
                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.massenger_arrow_back_blue:R.mipmap.ic_toolbar_arrow_ar);
            }else if(position == 0){
                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.logotitle:R.drawable.logotitle_ar);
                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.ic_toolbar_back_light:R.mipmap.ic_toolbar_arrow_ar);
            }else if(position == 2){
                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.logotitleyellow:R.drawable.logotitle_ar);
                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.massenger_arrow_back_yellow:R.mipmap.ic_toolbar_arrow_ar);

            }else if(position == 3){
                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.logotitlered:R.drawable.logotitle_ar);
                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.massenger_arrow_back_red:R.mipmap.ic_toolbar_arrow_ar);

            }else if(position == 1){
                getToolbar().setBackgroundResource(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.logotitlegreen:R.drawable.logotitle_ar);
                toolbar.setNavigationIcon(getResources().getInteger(R.integer.direction)==0?
                        R.drawable.massenger_arrow_back_green:R.mipmap.ic_toolbar_arrow_ar);

            }
            getBottomNavigation().getBadgeProvider().remove(itemId);
            if (null != getViewPager()) {
                getViewPager().setCurrentItem(position);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMenuItemReselect(@IdRes final int itemId, final int position, final boolean fromUser) {
        if (fromUser) {
            final FragmentManager manager = getSupportFragmentManager();
            MainMassengerFragment fragment = (MainMassengerFragment) manager.findFragmentById(R.id.fragment);

            if (null != fragment) {
                fragment.scrollToTop();
            }
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final int mCount;

        public ViewPagerAdapter(final AppCompatActivity activity, int count) {
            super(activity.getSupportFragmentManager());
            this.mCount = count;
        }

        @Override
        public Fragment getItem(final int position) {
            Fragment fragment=null;
            switch (position){
                case 0:fragment=new MainMassengerFragment();break;
                case 1:fragment=new MassengerDoneFragment();break;
                case 2:fragment=new MassengerPendFragment();break;
                case 3:fragment=new MassengerHoldFragment();break;
                case 4:fragment=new MassengerApprovalFragment();break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mCount;
        }
    }
    public boolean hasManagedToolbarScroll() {
        return true && findViewById(R.id.CoordinatorLayout01) instanceof CoordinatorLayout;
    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }
    public int getStatusBarHeight() {
        return getSystemBarTint().getConfig().getStatusBarHeight();
    }
}
