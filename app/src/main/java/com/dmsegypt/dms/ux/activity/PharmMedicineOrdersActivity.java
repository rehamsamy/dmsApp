package com.dmsegypt.dms.ux.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.MedicineOrder;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.MedicineOrderResultResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.ProviderMedicineOrdersAdapter;
import com.dmsegypt.dms.ux.dialogs.ImageViewerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PharmMedicineOrdersActivity extends BaseActivity {

    @BindView(R.id.rosheta_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar loadingView;
    @BindView(R.id.empty_view)
    View emptyView;


    List<MedicineOrder> medicineOrderList;
    ProviderMedicineOrdersAdapter providerMedicineOrdersAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        medicineOrderList =new ArrayList<>();



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        providerMedicineOrdersAdapter =new ProviderMedicineOrdersAdapter(medicineOrderList);
        recyclerView.setAdapter(providerMedicineOrdersAdapter);
        getProviderOrders("0");


        if (providerMedicineOrdersAdapter!=null)
        {
            providerMedicineOrdersAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    EditText editText= (EditText) baseQuickAdapter.getViewByPosition(recyclerView,i,R.id.reply_edittext);
                    String reply="None";
                    MedicineOrder medicineOrder= medicineOrderList.get(i);
                    medicineOrder.setLoading(true);
                    providerMedicineOrdersAdapter.notifyItemChanged(i);

                    if (view.getId()==R.id.acceptOrderBtn)
                    {
                        if (!TextUtils.isEmpty(editText.getText().toString()))
                        {
                            reply=editText.getText().toString();

                        }
                        changeOrderState(i,medicineOrderList.get(i).getOrderName(),"1",reply);
                    }
                    else  if (view.getId()==R.id.refuseOrderBtn)
                    {
                        if (!TextUtils.isEmpty(editText.getText().toString()))
                        {
                            reply=editText.getText().toString();

                        }
                        changeOrderState(i,medicineOrderList.get(i).getOrderName(),"0",reply);


                    }

                    else if (view.getId()==R.id.image_id)
                    {
                        if (medicineOrderList.get(i).getPresImgUrl() != null) {


                            ImageViewerDialog dialog= ImageViewerDialog.newInstance(medicineOrderList.get(i).getPresImgUrl().toString(),false);
                            dialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(),"image_dialog");
                        }}



                }
            });
        }

    }


    private void getProviderOrders(final String index)
    {     if (index.equals("0"))
        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        App.getInstance().getService().getPharmMedicineOrders(App.getInstance().getPrefManager().getCurrentUser().getPrvId(),"-1",index).enqueue(new Callback<MedicineOrderResultResponse>() {
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
                                medicineOrderList.add(order);
                            }

                            providerMedicineOrdersAdapter.notifyDataSetChanged();
                            providerMedicineOrdersAdapter.loadMoreComplete();
                            providerMedicineOrdersAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getProviderOrders(medicineOrderList.size()+1+"");
                                }
                            },recyclerView);

                        }else {
                            providerMedicineOrdersAdapter.loadMoreEnd();
                            if (index.equals("0"))
                                emptyView.setVisibility(View.VISIBLE);
                        }



                    }else {
                        if (index.equals("0"))

                            Snackbar.make(findViewById(android.R.id.content),"Error in response pleasetry again !", Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getProviderOrders("0");
                                        }
                                    }).setActionTextColor(Color.GREEN).show();
                    }


                }else {
                    if (index.equals("0"))

                        Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getProviderOrders("0");
                                    }
                                }).setActionTextColor(Color.GREEN).show();
                }
            }

            @Override
            public void onFailure(Call<MedicineOrderResultResponse> call, Throwable throwable) {
                DialogUtils.showDialog(PharmMedicineOrdersActivity.this,false);
                if (index.equals("0"))
                    Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getProviderOrders("0");
                                }
                            }).setActionTextColor(Color.GREEN).show();
            }
        });
    }


    private void changeOrderState(final int pos, String reqId, final String state, String reply)
    {



        App.getInstance().getService().changeMedicineOrders(reqId,state,reply).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                medicineOrderList.get(pos).setLoading(false);
                providerMedicineOrdersAdapter.notifyItemChanged(pos);
                if (response.body()!=null)
                {
                    if (response.body().getCode()==1)
                    {

                        Snackbar.make(findViewById(android.R.id.content),"Order state has been updated successfully !", Snackbar.LENGTH_SHORT)
                                .setActionTextColor(Color.GREEN).show();
                        medicineOrderList.clear();
                        providerMedicineOrdersAdapter.notifyDataSetChanged();
                        getProviderOrders("0");

                    }
                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getProviderOrders("0");
                                }
                            }).setActionTextColor(Color.GREEN).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable throwable) {
                medicineOrderList.get(pos).setLoading(false);
                providerMedicineOrdersAdapter.notifyItemChanged(pos);
                Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getProviderOrders("0");
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
        return R.layout.activity_provider_medicine_orders;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_pharmcy_order;
    }

}

