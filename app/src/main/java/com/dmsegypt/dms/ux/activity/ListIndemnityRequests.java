package com.dmsegypt.dms.ux.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.IndemnityRequest;
import com.dmsegypt.dms.rest.model.Response.ResponseIndemityRequests;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.IndemnityListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListIndemnityRequests extends BaseActivity {



    @BindView(R.id.empty_view)
    FrameLayout empty_view;

    @BindView(R.id.fab_requests)
    FloatingActionButton fab_requests;

    @BindView(R.id.list_indemnty_approval_requests)
    RecyclerView mRecyclerview;

    IndemnityListAdapter indemnityListAdapter;

    @BindView(R.id.activity_indemnity_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;



    Call call;
    boolean isDMSUser;
    String CardId="";


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
        return R.layout.activity_list_indemnity_requests;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.indemnity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.IndemnityList != null) {
            indemnityListAdapter = new IndemnityListAdapter(ListIndemnityRequests.this, R.layout.item_list_indemnty_request, Constants.IndemnityList);
            mRecyclerview.setAdapter(indemnityListAdapter);
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(layoutManager);
        indemnityListAdapter = new IndemnityListAdapter(this, R.layout.item_list_indemnty_request, Constants.IndemnityList);
        mRecyclerview.setAdapter(indemnityListAdapter);
        isDMSUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);
        if(isDMSUser){
            CardId=App.getInstance().getPrefManager().getUser().getCardId();

        }else {
            CardId="1";
        }


        if (Constants.IndemnityList == null || Constants.IndemnityList.size() == 0) {
            Constants.IndemnityList = new ArrayList<>();
            getIndemnityRquest("0",CardId);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getIndemnityRquest("0", CardId);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void getIndemnityRquest(final String index, final String cardId) {
        DialogUtils.showDialog(this, true);
        if(isDMSUser){
            App.getInstance().getService().getUserIndvidualRequests(getAppLanguage(), CardId, index).enqueue(new Callback<ResponseIndemityRequests>() {
                @Override
                public void onResponse(Call<ResponseIndemityRequests> call, Response<ResponseIndemityRequests> response) {
                    DialogUtils.showDialog(ListIndemnityRequests.this, false);

                    if (response.body() != null) {
                        if (response.body().getList().isEmpty()) {
                            // if first request
                            if (index.equals("0")) {
                                if (response.body().getMessage().getCode() != 1) {
                                    if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                        getIndemnityRquest("0", cardId);
                                    } else {
                                        empty_view.setVisibility(View.VISIBLE);
                                        mRecyclerview.setVisibility(View.GONE);

                                    }
                                }else {
                                    empty_view.setVisibility(View.VISIBLE);
                                    mRecyclerview.setVisibility(View.GONE);
                                }

                            } else if ((!index.equals("0"))) {
                                if ((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))) {
                                    indemnityListAdapter.setEnableLoadMore(false);
                                }

                            } else if (!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))) {
                                getIndemnityRquest(index,cardId);
                            }


                        } else {
                            //if list has data

                            if (index.equals("0")) {

                                empty_view.setVisibility(View.GONE);
                                mRecyclerview.setVisibility(View.VISIBLE);
                                Constants.IndemnityList.clear();
                                Constants.IndemnityList.addAll(response.body().getList());



                                indemnityListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        loadMoreRequest(String.valueOf(indemnityListAdapter.getItemCount() - 1),cardId);

                                    }
                                }, mRecyclerview);
                                indemnityListAdapter.notifyDataSetChanged();  // data set changed
                            } else {
                                if ((response.body().getList().size() % 20) != 0)
                                    indemnityListAdapter.setEnableLoadMore(false);
                                Constants.IndemnityList.addAll(response.body().getList());
                            }
                            indemnityListAdapter.notifyDataSetChanged();
                        }
                        if (index.equals("0")) {

                            DialogUtils.showDialog(ListIndemnityRequests.this, false);
                        }
                        if (response.body().getList().size() != 0) {
                            indemnityListAdapter.loadMoreComplete();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseIndemityRequests> call, Throwable throwable) {
                    DialogUtils.showDialog(ListIndemnityRequests.this,false);
                    Snackbar.make(fab_requests,R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getIndemnityRquest("0",cardId);
                                }
                            }).setActionTextColor(Color.GREEN).show();

                }
            });

        }else {
            App.getInstance().getService().getAllIndvidualRequests(getAppLanguage(), CardId, index).enqueue(new Callback<ResponseIndemityRequests>() {
                @Override
                public void onResponse(Call<ResponseIndemityRequests> call, Response<ResponseIndemityRequests> response) {
                    DialogUtils.showDialog(ListIndemnityRequests.this, false);

                    if (response.body() != null) {
                        if (response.body().getList().isEmpty()) {
                            // if first request
                            if (index.equals("0")) {
                                if (response.body().getMessage().getCode() != 1) {
                                    if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                        getIndemnityRquest("0", cardId);
                                    } else {
                                        empty_view.setVisibility(View.VISIBLE);
                                        mRecyclerview.setVisibility(View.GONE);

                                    }
                                }

                            } else if ((!index.equals("0"))) {
                                if ((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))) {
                                    indemnityListAdapter.setEnableLoadMore(false);
                                }

                            } else if (!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))) {
                                getIndemnityRquest(index,cardId);
                            }


                        } else {
                            //if list has data

                            if (index.equals("0")) {

                                empty_view.setVisibility(View.GONE);
                                mRecyclerview.setVisibility(View.VISIBLE);
                                Constants.IndemnityList.clear();
                                Constants.IndemnityList.addAll(response.body().getList());



                                indemnityListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        loadMoreRequest(String.valueOf(indemnityListAdapter.getItemCount() - 1),cardId);

                                    }
                                }, mRecyclerview);
                                indemnityListAdapter.notifyDataSetChanged();  // data set changed
                            } else {
                                if ((response.body().getList().size() % 20) != 0)
                                    indemnityListAdapter.setEnableLoadMore(false);
                                Constants.IndemnityList.addAll(response.body().getList());
                            }
                            indemnityListAdapter.notifyDataSetChanged();
                        }
                        if (index.equals("0")) {

                            DialogUtils.showDialog(ListIndemnityRequests.this, false);
                        }
                        if (response.body().getList().size() != 0) {
                            indemnityListAdapter.loadMoreComplete();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseIndemityRequests> call, Throwable throwable) {
                    DialogUtils.showDialog(ListIndemnityRequests.this,false);
                    Snackbar.make(fab_requests,R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getIndemnityRquest("0",cardId);
                                }
                            }).setActionTextColor(Color.GREEN).show();

                }
            });

        }
    }

    private void loadMoreRequest(final String index,final String cardId) {
        if(isDMSUser){
            App.getInstance().getService().getUserIndvidualRequests(getAppLanguage(), CardId, index).enqueue(new Callback<ResponseIndemityRequests>() {
                @Override
                public void onResponse(Call<ResponseIndemityRequests> call, Response<ResponseIndemityRequests> response) {
                    DialogUtils.showDialog(ListIndemnityRequests.this, false);

                    if (response.body() != null) {
                        if (response.body().getList().isEmpty()) {
                            // if first request
                            if (index.equals("0")) {
                                if (response.body().getMessage().getCode() != 1) {
                                    if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                        getIndemnityRquest("0", cardId);
                                    } else {
                                        empty_view.setVisibility(View.VISIBLE);
                                        mRecyclerview.setVisibility(View.GONE);

                                    }
                                }

                            } else if ((!index.equals("0"))) {
                                if ((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))) {
                                    indemnityListAdapter.setEnableLoadMore(false);
                                }

                            } else if (!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))) {
                                getIndemnityRquest(index,cardId);
                            }


                        } else {
                            //if list has data

                            if (index.equals("0")) {

                                empty_view.setVisibility(View.GONE);
                                mRecyclerview.setVisibility(View.VISIBLE);
                                Constants.IndemnityList.clear();
                                Constants.IndemnityList.addAll(response.body().getList());



                                indemnityListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        loadMoreRequest(String.valueOf(indemnityListAdapter.getItemCount() - 1),cardId);

                                    }
                                }, mRecyclerview);
                                indemnityListAdapter.notifyDataSetChanged();  // data set changed
                            } else {
                                if ((response.body().getList().size() % 20) != 0)
                                    indemnityListAdapter.setEnableLoadMore(false);
                                Constants.IndemnityList.addAll(response.body().getList());
                            }
                            indemnityListAdapter.notifyDataSetChanged();
                        }
                        if (index.equals("0")) {

                            DialogUtils.showDialog(ListIndemnityRequests.this, false);
                        }
                        if (response.body().getList().size() != 0) {
                            indemnityListAdapter.loadMoreComplete();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseIndemityRequests> call, Throwable throwable) {
                    DialogUtils.showDialog(ListIndemnityRequests.this,false);
                    Snackbar.make(fab_requests,R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getIndemnityRquest("0",cardId);
                                }
                            }).setActionTextColor(Color.GREEN).show();

                }
            });

        }else {
            App.getInstance().getService().getAllIndvidualRequests(getAppLanguage(), CardId, index).enqueue(new Callback<ResponseIndemityRequests>() {
                @Override
                public void onResponse(Call<ResponseIndemityRequests> call, Response<ResponseIndemityRequests> response) {
                    DialogUtils.showDialog(ListIndemnityRequests.this, false);

                    if (response.body() != null) {
                        if (response.body().getList().isEmpty()) {
                            // if first request
                            if (index.equals("0")) {
                                if (response.body().getMessage().getCode() != 1) {
                                    if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                        getIndemnityRquest("0", cardId);
                                    } else {
                                        empty_view.setVisibility(View.VISIBLE);
                                        mRecyclerview.setVisibility(View.GONE);

                                    }
                                }

                            } else if ((!index.equals("0"))) {
                                if ((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))) {
                                    indemnityListAdapter.setEnableLoadMore(false);
                                }

                            } else if (!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))) {
                                getIndemnityRquest(index,cardId);
                            }


                        } else {
                            //if list has data

                            if (index.equals("0")) {

                                empty_view.setVisibility(View.GONE);
                                mRecyclerview.setVisibility(View.VISIBLE);
                                Constants.IndemnityList.clear();
                                Constants.IndemnityList.addAll(response.body().getList());



                                indemnityListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        loadMoreRequest(String.valueOf(indemnityListAdapter.getItemCount() - 1),cardId);

                                    }
                                }, mRecyclerview);
                                indemnityListAdapter.notifyDataSetChanged();  // data set changed
                            } else {
                                if ((response.body().getList().size() % 20) != 0)
                                    indemnityListAdapter.setEnableLoadMore(false);
                                Constants.IndemnityList.addAll(response.body().getList());
                            }
                            indemnityListAdapter.notifyDataSetChanged();
                        }
                        if (index.equals("0")) {

                            DialogUtils.showDialog(ListIndemnityRequests.this, false);
                        }
                        if (response.body().getList().size() != 0) {
                            indemnityListAdapter.loadMoreComplete();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseIndemityRequests> call, Throwable throwable) {
                    DialogUtils.showDialog(ListIndemnityRequests.this,false);
                    Snackbar.make(fab_requests,R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getIndemnityRquest("0",cardId);
                                }
                            }).setActionTextColor(Color.GREEN).show();

                }
            });

        }
    }

    @Override
        protected void onDestroy () {
            super.onDestroy();
            if (call != null) {
                call.cancel();
            }
        }
        @OnClick(R.id.fab_requests)
          void startSingleApproval(){ DateFormat df = new SimpleDateFormat("dd-MM-yy HH-mm-ss", Locale.US);
            Date dateobj = new Date();
            String subfolder=df.format(dateobj);

            Intent newintent = new Intent(this, SingleApproval.class);
            newintent.putExtra(SingleApproval.EXTRA_SUB_FOLDER,subfolder);
            ActivityOptions options=ActivityOptions.makeCustomAnimation(this,R.anim.translate_left_to_right,R.anim.slide_down);
            this.startActivity(newintent,options.toBundle());
           }
    }

