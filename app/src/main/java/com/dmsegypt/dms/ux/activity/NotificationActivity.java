package com.dmsegypt.dms.ux.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.NotificationItem;
import com.dmsegypt.dms.rest.model.Response.ResponseGetNotifications;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.adapter.NotificationAdapter;
import com.dmsegypt.dms.ux.custom_view.StateView;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class NotificationActivity extends BaseActivity {


    //region declare view and variables
    @BindView(R.id.activity_notification_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.activity_notification_container)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
   /* @BindView(R.id.tv_empty_notfi)
    TextView emptyNotificationTv;
    @BindView(R.id.tv_no_connection)
    TextView noConnectionTv;*/
    @BindView(R.id.state_view)
    StateView stateView;


    Call call;
    private NotificationAdapter adapter;
    //endregion




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


       init();
    }

    //region init View
    public void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        prepareNotifications();
        retrieveNotifications();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveNotifications();
            }
        });
    }

    //endregion




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //region declare toolbar
    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_notification;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.notification;
    }


    //endregion




    @Override
    public void finishActivity(int dir) {
        finish();
    }


    //prepare Notification variables
    private void prepareNotifications() {
        Constants.Notifications=new ArrayList<>();
        adapter = new NotificationAdapter(R.layout.layout_notification_card, Constants.Notifications);
        adapter.setEnableLoadMore(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    //retrive notification by Card Id
    public void retrieveNotifications() {
        swipeRefreshLayout.setRefreshing(true);/*
        emptyNotificationTv.setVisibility(View.GONE);
        noConnectionTv.setVisibility(View.GONE);*/
        stateView.setVisibility(View.VISIBLE);
        stateView.showState(StateView.LOADING);
        recyclerView.setVisibility(View.GONE);
        call=App.getInstance().getService().getNotifications(App.getInstance().getPrefManager().getUser().getCardId(), getAppLanguage());
                call.enqueue(new GetNotifictionCallback(this));
    }
    private static  class  GetNotifictionCallback implements Callback<ResponseGetNotifications>{
        WeakReference<NotificationActivity>activityWeakReference;

        public GetNotifictionCallback(NotificationActivity activity) {
            this.activityWeakReference = new WeakReference<NotificationActivity>(activity);
        }

        @Override
        public void onResponse(Call<ResponseGetNotifications> call, Response<ResponseGetNotifications> response) {
          NotificationActivity activity=activityWeakReference.get();
            if (activity==null)
                return;
            activity.swipeRefreshLayout.setRefreshing(false);

            if (response.body() != null) {
                Message message = response.body().getMessage();
                if (message.getCode() == 1) {
                       // activity.emptyNotificationTv.setVisibility(View.GONE);
                        activity.recyclerView.setVisibility(View.VISIBLE);
                       // activity.noConnectionTv.setVisibility(View.GONE);
                    activity.stateView.setVisibility(View.GONE);
                    Constants.Notifications.addAll(response.body().getList());
                        activity.adapter.notifyDataSetChanged();
                    App.getInstance().getPrefManager().setNotificationCount(0);


                }else {
                    activity.stateView.showState(StateView.EMPTY);

                    //activity.emptyNotificationTv.setVisibility(View.VISIBLE);
                    activity.recyclerView.setVisibility(View.GONE);
                   // activity.noConnectionTv.setVisibility(View.GONE);

                }
            }else {
                activity.stateView.showState(StateView.NO_CONNECTION);

               // activity.emptyNotificationTv.setVisibility(View.GONE);
                activity.recyclerView.setVisibility(View.GONE);
               // activity.noConnectionTv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<ResponseGetNotifications> call, Throwable t) {
            NotificationActivity activity=activityWeakReference.get();
            if (activity==null)return;
            activity.swipeRefreshLayout.setRefreshing(false);
            //activity.emptyNotificationTv.setVisibility(View.GONE);
            //activity.noConnectionTv.setVisibility(View.VISIBLE);
            activity.stateView.showState(StateView.NO_CONNECTION);
            activity.recyclerView.setVisibility(View.GONE);
        }
    }


    //destroy Callback to avoid leak memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){call.cancel();  }
    }
}
