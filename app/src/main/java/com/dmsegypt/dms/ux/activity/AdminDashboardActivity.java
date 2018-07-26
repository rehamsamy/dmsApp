package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.ux.adapter.AdminDashboardAdapter;
import com.dmsegypt.dms.rest.model.DashBoard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminDashboardActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    AdminDashboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        adapter=new AdminDashboardAdapter(DashBoard.getDashboardList());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (i){
                    case 0:
                        Intent intent=new Intent(AdminDashboardActivity.this,AdminViewAccountsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent notifIntent=new Intent(AdminDashboardActivity.this,AdminNotificationActivity.class);
                        startActivity(notifIntent);
                        break;
                    case 2:
                        Intent reviewIntent=new Intent(AdminDashboardActivity.this,GooglePlayReviewsActivity.class);
                        startActivity(reviewIntent);
                        break;
                }
            }
        });
    }


    @OnClick(R.id.dms_fab)
    void addDmsMemberAccount(){
        Intent intent=new Intent(this,AdminAddAccountsActivity.class);
        intent.putExtra(AdminAddAccountsActivity.EXTRA_USER_TYPE, Constants.USER_TYPE_DMS);
        startActivity(intent);
    }

    @OnClick(R.id.hr_fab)
    void addHrAccount(){
        Intent intent=new Intent(this,AdminAddAccountsActivity.class);
        intent.putExtra(AdminAddAccountsActivity.EXTRA_USER_TYPE, Constants.USER_TYPE_HR);
        startActivity(intent);
    }
    @OnClick(R.id.provider_fab)
    void addProviderAccount(){
        Intent intent=new Intent(this,AdminAddAccountsActivity.class);
        intent.putExtra(AdminAddAccountsActivity.EXTRA_USER_TYPE, Constants.USER_TYPE_PROVIDER);
        startActivity(intent);
    }


    @Override
    public boolean hasActionBar() {
        return true;
    }


    @Override
    public int getResLayout() {
        return R.layout.activity_admin_dashboared;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_dashboard;
    }
}
