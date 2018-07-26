package com.dmsegypt.dms.ux.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.adapter.MembersAdapter;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class FamilyMemberActivity extends BaseActivity
        implements
        SwipeBackCoordinatorLayout.OnSwipeListener {

    private final String TAG = this.getClass().getSimpleName();
    //region UI VIEW
    @BindView(R.id.activity_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;
    @BindView(R.id.activity_container)
    CoordinatorLayout container;
    @BindView(R.id.emtpy_tv)
    TextView emptyTv;
    @BindView(R.id.recyclerView)
    RecyclerView rvMembers;
    //endregion
    //region references
    private MembersAdapter membersAdapter;
    private Unbinder unbinder;
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
        return R.layout.activity_family_member;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_family;
    }



    @OnClick(R.id.fab_add)
    public void addFamilyMember(){
        IntentManager.startAddFamilyMembers(FamilyMemberActivity.this);
    }

    private void initView() {
        swipeBackView.setOnSwipeListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMembers.setLayoutManager(layoutManager);
        if(membersAdapter != null) {
            membersAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                    if (view.getId() == R.id.ib_delete) {
                        //show confirm dialog to delete member
                        DialogUtils.ConfirmDialog(FamilyMemberActivity.this,
                                getString(R.string.delete_member_confirmation), new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //Yes
                                        deleteMember(App.getInstance().getPrefManager().getUser().getCardId(),
                                                membersAdapter.getItem(position).getCardId(), "en");

                                    }
                                }, new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //No
                                    }
                                });

                    }

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        getMembers();
    }
   //send request to get family member
    private void getMembers() {


        DialogUtils.showDialog(FamilyMemberActivity.this,true);

       call= App.getInstance().getService().getMembers( App.getInstance().getPrefManager().getUser().getCardId(), getAppLanguage());
                call.enqueue(new Callback<ResponseMembers>() {
                    @Override
                    public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {
                        if (response.body() != null) {

                            if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {
                                emptyTv.setVisibility(View.GONE);
                                rvMembers.setVisibility(View.VISIBLE);
                                membersAdapter = new MembersAdapter(R.layout.list_item_member, response.body().getList());
                                rvMembers.setAdapter(membersAdapter);
                                membersAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                    @Override
                                    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                                        if (view.getId() == R.id.ib_delete) {
                                            //show confirm dialog to delete member
                                            DialogUtils.ConfirmDialog(FamilyMemberActivity.this,
                                                    getString(R.string.delete_member_confirmation), new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            //Yes
                                                            deleteMember(App.getInstance().getPrefManager().getUser().getCardId(),
                                                                    membersAdapter.getItem(position).getCardId(), "en");

                                                        }
                                                    }, new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            //No
                                                        }
                                                    });

                                        }

                                    }
                                });



                            }else {
                                emptyTv.setVisibility(View.VISIBLE);
                                rvMembers.setVisibility(View.GONE);

                            }

                        }

                        DialogUtils.showDialog(FamilyMemberActivity.this,false);
                    }

                    @Override
                    public void onFailure(Call<ResponseMembers> call, Throwable t) {
                        Log.e(TAG, "API getMembers Error " + t.toString());
                        DialogUtils.showDialog(FamilyMemberActivity.this,false);
                        Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getMembers();
                                    }
                                }).setActionTextColor(Color.GREEN).show();

                    }
                });
    }

    public void deleteMember(String cardId, String memberCardId, String language){

        DialogUtils.showDialog(FamilyMemberActivity.this,true);

        call=App.getInstance().getService().deleteMembers(cardId, memberCardId, language);
                call.enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                        if(response != null){

                        //    Toast.makeText(FamilyMemberActivity.this, response.body().getDetails(), Toast.LENGTH_SHORT).show();
                            SuperToast.create(FamilyMemberActivity.this, response.body().getDetails(), 3000).show();

                            if( response.body().getCode() != null && response.body().getCode() == 1){
                                DialogUtils.showDialog(FamilyMemberActivity.this,false);
                                getMembers();
                            }

                            DialogUtils.showDialog(FamilyMemberActivity.this,false);
                        }else {
                            DialogUtils.showDialog(FamilyMemberActivity.this,false);
                        }

                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Log.e(TAG, "API deleteMembers Error " + t.toString());
                        DialogUtils.showDialog(FamilyMemberActivity.this,false);
                    }
                });

    }



/*
    private void showLoading(boolean isShow) {

        if (loadingDialog != null) {

            if (isShow) ++loadingCount;
            else --loadingCount;

            if (loadingCount <= 0) {

                if (isShow) {
                    loadingDialog.show();
                    loadingCount = 0;
                } else {
                    loadingDialog.dismiss();
                    loadingCount = -1;
                }

            }

            Log.i("Complains", isShow + " - " + loadingCount);
        }

    }
*/
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
