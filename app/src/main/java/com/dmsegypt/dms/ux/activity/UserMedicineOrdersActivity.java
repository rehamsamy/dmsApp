package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.MedicineOrder;
import com.dmsegypt.dms.rest.model.Response.MedicineOrderResultResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.UserMedicineOrdersAdapter;
import com.dmsegypt.dms.ux.dialogs.ImageViewerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMedicineOrdersActivity extends BaseActivity {

    @BindView(R.id.rosheta_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.progress)
    ProgressBar loadingView;
    @BindView(R.id.empty_view)
    View emptyView;

    List<MedicineOrder> userMedicineOrders;
    UserMedicineOrdersAdapter userMedicineOrdersAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        userMedicineOrders=new ArrayList<>();



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userMedicineOrdersAdapter =new UserMedicineOrdersAdapter(userMedicineOrders);
        recyclerView.setAdapter(userMedicineOrdersAdapter);
        getUserOrders("0");

        userMedicineOrdersAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (view.getId()==R.id.image_id)
                { if (userMedicineOrders.get(i).getPresImgUrl()!=null){
                    ImageViewerDialog dialog= ImageViewerDialog.newInstance(userMedicineOrders.get(i).getPresImgUrl().toString(),false);
                    dialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(),"image_dialog");
                }}



            }
        });

    }

    @OnClick(R.id.fab)
    void  setFloatingActionButton()
    {
        Intent intent=new Intent(UserMedicineOrdersActivity.this,SendMedicineOrderActivty.class);
        startActivity(intent);
    }


    private void getUserOrders(final String index)
    {
        if (index.equals("0"))
            loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        App.getInstance().getService().getUserMedicineOrders(App.getInstance().getPrefManager().getCurrentUser().getCardId(),"-1",index).enqueue(new Callback<MedicineOrderResultResponse>() {
            @Override
            public void onResponse(Call<MedicineOrderResultResponse> call, Response<MedicineOrderResultResponse> response) {
                loadingView.setVisibility(View.GONE);

                if (response.body()!=null)
                {

                    if (response.body().getMessage().getCode()==1)
                    {

                        if (response.body().getList().size()>0)
                        {

                            for (MedicineOrder order:response.body().getList())
                            {
                                userMedicineOrders.add(order);
                            }

                            userMedicineOrdersAdapter.notifyDataSetChanged();
                            userMedicineOrdersAdapter.loadMoreComplete();
                            userMedicineOrdersAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getUserOrders(userMedicineOrders.size()+1+"");
                                }
                            },recyclerView);

                        }else {
                            userMedicineOrdersAdapter.loadMoreEnd();
                            if (index.equals("0"))
                                emptyView.setVisibility(View.VISIBLE);
                        }


                    }else {
                        if (index.equals("0"))

                            Snackbar.make(findViewById(android.R.id.content),"Error in response please try again !", Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getUserOrders("0");
                                        }
                                    }).setActionTextColor(Color.GREEN).show();
                    }


                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUserOrders("0");
                                }
                            }).setActionTextColor(Color.GREEN).show();
                }
            }

            @Override
            public void onFailure(Call<MedicineOrderResultResponse> call, Throwable throwable) {

                loadingView.setVisibility(View.GONE);
                if (index.equals("0"))
                    Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUserOrders("0");
                                }
                            }).setActionTextColor(Color.GREEN).show();
            }
        });
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_pharmacy_orders;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_medicine_order;
    }
}
