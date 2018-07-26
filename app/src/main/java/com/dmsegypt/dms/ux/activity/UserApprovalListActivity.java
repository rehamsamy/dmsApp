package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseRequests;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.UserRequstsAdaper;
import com.dmsegypt.dms.ux.custom_view.StateView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApprovalListActivity extends BaseActivity {
    //region UI VIEW
    @BindView(R.id.listUserRequests)
    RecyclerView mRecyclerview;
    @BindView(R.id.root)
     View rootLayout;
    @BindView(R.id.fab_requests)
    FloatingActionButton FAB;

    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.state_view)
    StateView stateView;
    //endregion
    //region references
    UserRequstsAdaper userRequstsAdaper;
    String cardId;
    Call call;
    AnimUtils anim;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
         initView();

    }
    private void initView(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerview.setLayoutManager(layoutManager);
        userRequstsAdaper=new UserRequstsAdaper(UserApprovalListActivity.this,R.layout.list_item_user_request,Constants.UserListRequests);
        mRecyclerview.setAdapter(userRequstsAdaper);
        Intent intent = this.getIntent();

        if(Constants.UserListRequests == null || Constants.UserListRequests.size() == 0){
            Constants.UserListRequests=new ArrayList<>();
            getUserRquest("0");
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserRquest("0");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

@OnClick(R.id.fab_requests)
 void startRequetsActivity(View view){
    IntentManager.startRequestsActivity(this,view);
}

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_list_user_requesrs;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_requests;
    }

    public void getUserRquest(final String index) {
        cardId=App.getInstance().getPrefManager().getUser().getCardId();
        stateView.setVisibility(View.VISIBLE);
        stateView.showState(StateView.LOADING);
        mRecyclerview.setVisibility(View.VISIBLE);
        stateView.setListener(new StateView.OnRetrListener() {
            @Override
            public void onRetry() {
                getUserRquest("0");
            }
        });

        call=App.getInstance().getService().getUserRequests(getAppLanguage(),cardId,index);
        call.enqueue(new Callback<ResponseRequests>() {
            @Override
            public void onResponse(Call<ResponseRequests> call, Response<ResponseRequests> response) {
                DialogUtils.showDialog(UserApprovalListActivity.this,false);

                if(response.body() != null){
                        if (response.body().getList().isEmpty()){
                            // if first request
                            if (index.equals("0")) {
                                if(response.body().getMessage().getCode()!= 1) {
                                    if(response.body().getMessage().getDetails().equals("Can't connect to server")){
                                        stateView.showState(StateView.NO_CONNECTION);

                                    }else {
                                        stateView.showState(StateView.EMPTY);
                                        mRecyclerview.setVisibility(View.GONE);
                                    }
                                }

                            }else if ((!index.equals("0"))) {
                                if((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))){
                                    userRequstsAdaper.setEnableLoadMore(false);
                                }

                            }else if(!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))){
                                stateView.showState(StateView.NO_CONNECTION);
                            }


                    }else {
                            //if list has data

                            if (index.equals("0")) {

                                    stateView.setVisibility(View.GONE);

                                mRecyclerview.setVisibility(View.VISIBLE);
                                Constants.UserListRequests.clear();
                                Constants.UserListRequests.addAll(response.body().getList());
                                userRequstsAdaper.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        loadMoreRequest(String.valueOf(userRequstsAdaper.getItemCount() - 1));

                                    }
                                },mRecyclerview);
                                userRequstsAdaper.notifyDataSetChanged();  // data set changed
                            }else {
                                if ((response.body().getList().size() % 20) != 0)
                                    userRequstsAdaper.setEnableLoadMore(false);
                                Constants.UserListRequests.addAll(response.body().getList());
                            }
                            userRequstsAdaper.notifyDataSetChanged();
                    }

                    if (response.body().getList().size() != 0){
                        userRequstsAdaper.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRequests> call, Throwable throwable) {
                stateView.showState(StateView.NO_CONNECTION);

                mRecyclerview.setVisibility(View.GONE);



            }
        });
    }



//get user request when recycleView show load more
  void loadMoreRequest(final String index){

    cardId=App.getInstance().getPrefManager().getUser().getCardId();

    mRecyclerview.setVisibility(View.VISIBLE);

    call=App.getInstance().getService().getUserRequests(getAppLanguage(),cardId,index);
    call.enqueue(new Callback<ResponseRequests>() {
        @Override
        public void onResponse(Call<ResponseRequests> call, Response<ResponseRequests> response) {
            if(response.body() != null){
                if (response.body().getList().isEmpty()){
                    if (index.equals("0")) {
                        if(response.body().getMessage().getCode()!= 1) {
                            if(response.body().getMessage().getDetails().equals("Can't connect to server")){

                            }else {
                                mRecyclerview.setVisibility(View.GONE);
                            }
                        }

                    }else if ((!index.equals("0"))) {
                        if((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))){
                            userRequstsAdaper.setEnableLoadMore(false);
                        }

                    }else if(!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))){
                        getUserRquest(index);
                    }


                }else {
                    if (index.equals("0")) {
                        mRecyclerview.setVisibility(View.VISIBLE);
                        Constants.UserListRequests.clear();
                        Constants.UserListRequests.addAll(response.body().getList());
                        userRequstsAdaper.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {
                                loadMoreRequest(String.valueOf(userRequstsAdaper.getItemCount() - 1));

                            }
                        },mRecyclerview);
                        userRequstsAdaper.notifyDataSetChanged();  // data set changed
                    }else {
                        if ((response.body().getList().size() % 20) != 0)
                            userRequstsAdaper.setEnableLoadMore(false);
                        Constants.UserListRequests.addAll(response.body().getList());
                    }
                    userRequstsAdaper.notifyDataSetChanged();
                }
                if (index.equals("0")){

                }
                if (response.body().getList().size() != 0){
                    userRequstsAdaper.loadMoreComplete();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseRequests> call, Throwable throwable) {
           userRequstsAdaper.loadMoreComplete();
        }
    });

}





    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.UserListRequests != null || Constants.UserListRequests.size() != 0) {
            userRequstsAdaper=new UserRequstsAdaper(UserApprovalListActivity.this,R.layout.list_item_user_request,Constants.UserListRequests);
            mRecyclerview.setAdapter(userRequstsAdaper);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
