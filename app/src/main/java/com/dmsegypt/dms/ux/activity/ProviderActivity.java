package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DisplayUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.custom_view.ProviderDetailsView;
import com.dmsegypt.dms.ux.custom_view.SafeHandler;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class ProviderActivity extends BaseActivity implements
        SwipeBackCoordinatorLayout.OnSwipeListener, SafeHandler.HandlerContainer {
    //region Constants
    public static final String KEY_PROVIDER_ACTIVITY_PROVIDER = "provider_activity_provider";
    private final String TAG = this.getClass().getSimpleName();
    //endregion
    //region references
    Provider provider;
    //endregion
    //region UI VIEW
    @BindView(R.id.activity_provider_title)
    TextView title;
    @BindView(R.id.activity_provider_titleBar)
    RelativeLayout titleBar;
    @BindView(R.id.activity_provider_scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.activity_provider_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;
    @BindView(R.id.activity_provider_container)
    RelativeLayout container;
    @BindView(R.id.activity_provider_detailsView)
    ProviderDetailsView detailsView;
    //endregion



    // on menu Item click listener.
    private Toolbar.OnMenuItemClickListener scrollToolbarMenuListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_share:
                    IntentManager.makeShare(ProviderActivity.this, provider.detailsToShare());
                    break;

            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int index = (int) getIntent().getExtras().getSerializable(KEY_PROVIDER_ACTIVITY_PROVIDER);
        provider = Constants.providers.get(index);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_provider;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    private void init() {

        swipeBackView.setOnSwipeListener(this);

        scrollView.setPadding(0, DisplayUtils.getStatusBarHeight(getResources()), 0, 0);

        title.setText(provider.getName());

        detailsView.initData(provider, getSnackbarContainer());
        AnimUtils.animInitShow(titleBar, 300);
        AnimUtils.animInitShow(detailsView, 400);
    }

    @Override
    public boolean canSwipeBack(int dir) {
        return SwipeBackCoordinatorLayout.canSwipeBackForThisView(scrollView, dir);
    }

    @Override
    public void onSwipeProcess(float percent) {
        container.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
    }

    @Override
    public void onSwipeFinish(int dir) {
        finishActivity(dir);
    }

    @Override
    public void handleMessage(Message message) {
    }


    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        onBackPressed();
    }




    //    @Override
    public View getSnackbarContainer() {
        return container;
    }

    @OnClick({R.id.btnCall, R.id.btnNavigation, R.id.btnBookmark})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCall:
                IntentManager.makeCall(this, provider.getTel());
                break;
            case R.id.btnNavigation:
                IntentManager.makeNavigate(this, provider.getAddress());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentManager.KEY_CALL_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(ProviderActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                IntentManager.makeCall(ProviderActivity.this, provider.getTel());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else finish();
    }
}
