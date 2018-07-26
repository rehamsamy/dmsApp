package com.dmsegypt.dms.ux.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Response.ResponseStatus;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.adapter.StatusAdapter;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class CheckStatusActivity extends BaseActivity
        implements
        SwipeBackCoordinatorLayout.OnSwipeListener {


    private final String TAG = this.getClass().getSimpleName();
    //region UI VIEW
    @BindView(R.id.activity_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;
    @BindView(R.id.activity_container)
    CoordinatorLayout container;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llNoResult)
    LinearLayout llNoResult;
    //endregion
//region refrences
    private StatusAdapter statusAdapter;
    private MaterialDialog loadingDialog;
    private int loadingCount = -1;
    Call call;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_check_status;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_check_status;
    }

    private void initView() {
        swipeBackView.setOnSwipeListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getStatus();

    }




    private void getStatus() {

        DialogUtils.showDialog(this,true);

        call=App.getInstance().getService().checkStatus(App.getInstance().getPrefManager().getUser().getCardId(), getAppLanguage());
               call.enqueue(new Callback<ResponseStatus>() {
                    @Override
                    public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                        if (response.body() != null) {

                            if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {

                                statusAdapter = new StatusAdapter(R.layout.list_item_status, response.body().getList());
                                recyclerView.setAdapter(statusAdapter);

                            } else {
                                llNoResult.setVisibility(View.VISIBLE);
                            }

                        }

                        DialogUtils.showDialog(CheckStatusActivity.this,false);
                    }

                    @Override
                    public void onFailure(Call<ResponseStatus> call, Throwable t) {
                        Log.e(TAG, "API getStatus Error " + t.toString());
                        DialogUtils.showDialog(CheckStatusActivity.this,false);
                    }
                });
    }



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


    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
