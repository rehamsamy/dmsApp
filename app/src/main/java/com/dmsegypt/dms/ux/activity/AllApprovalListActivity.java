package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Response.ResponseRequests;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.RequestAdapter;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllApprovalListActivity extends BaseActivity {


    //region declare View and Variables
    @BindView(R.id.recycler_view_requests)
    RecyclerView mRecyclerViewRequests;


    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private RequestAdapter requestsAdapter;
    Call call;

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
       initview();

    }

    //region initviews
    public void  initview(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerViewRequests.setLayoutManager(layoutManager);

        requestsAdapter=new RequestAdapter(AllApprovalListActivity.this, R.layout.list_item_requests,Constants.Requests);
        mRecyclerViewRequests.setAdapter(requestsAdapter);
        if(Constants.Requests == null || Constants.Requests.size() == 0) {
            Constants.Requests=new ArrayList<>();
            DialogUtils.showDialog(this,true);
            getData("0");
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getData("0");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    //endregion


    //region Toolbar Declare
    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_request_list;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_requests;
    }

//endregion




//region Check if requests list at Constant
    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.Requests != null || Constants.Requests.size() != 0) {
            requestsAdapter = new RequestAdapter(AllApprovalListActivity.this, R.layout.list_item_requests, Constants.Requests);
            mRecyclerViewRequests.setAdapter(requestsAdapter);
        }

    }

    //endregion


    //region get All Request

    /**
     *
     * @param index
     */
    public void getData(final String index) {
        call= App.getInstance().getService().getAllRequests(getAppLanguage(),1,index);
        call.enqueue(new Callback<ResponseRequests>() {
            @Override
            public void onResponse(Call<ResponseRequests> call, Response<ResponseRequests> response) {
              DialogUtils.showDialog(AllApprovalListActivity.this,false);

                if(response.body() != null){
                    if (response.body().getList().isEmpty()) {
                        if (index.equals("0")) {
                            if (response.body().getMessage().getCode() != 1) {
                                if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                    getData("0");
                                }
                            }
                        }else if(!index.equals("0")){
                            if((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))){
                                requestsAdapter.setEnableLoadMore(false);
                            }
                        }else if(!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))){
                            getData(index);
                        }
                        }else {
                        if(index.equals("0")){
                            Constants.Requests.clear();
                            DialogUtils.showDialog(AllApprovalListActivity.this,false);
                            mRecyclerViewRequests.setVisibility(View.VISIBLE);
                            Constants.Requests.addAll(response.body().getList());
                            requestsAdapter.notifyDataSetChanged();  // data set changed
                            requestsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getData(String.valueOf(requestsAdapter.getItemCount() - 1));

                                }
                            },mRecyclerViewRequests);
                            requestsAdapter.notifyDataSetChanged();  // data set changed

                        }else {
                            if ((response.body().getList().size() % 20) != 0)
                                requestsAdapter.setEnableLoadMore(false);
                            Constants.Requests.addAll(response.body().getList());

                        }
                        requestsAdapter.notifyDataSetChanged();
                        }
                    if (index.equals("0")){

                        DialogUtils.showDialog(AllApprovalListActivity.this,false);
                    }
                    if (response.body().getList().size() != 0){
                        requestsAdapter.loadMoreComplete();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseRequests> call, Throwable throwable) {
                //  Toast.makeText(getActivity(),throwable.toString(),Toast.LENGTH_LONG).show();
                SuperToast.create(AllApprovalListActivity.this,getString(R.string.label_no_conenction), 3000).show();

                DialogUtils.showDialog(AllApprovalListActivity.this,false);
            }
        });
    }

    //endregion

}

