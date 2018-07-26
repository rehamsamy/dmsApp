package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.AfterSales;
import com.dmsegypt.dms.rest.model.AfterSalesItem;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseAfterSales;
import com.dmsegypt.dms.rest.model.Response.SalesCallResposne;
import com.dmsegypt.dms.rest.model.Salesitem;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.AfteSalesSearchAdapter;
import com.dmsegypt.dms.ux.adapter.SalesAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAfterSalesActivity extends BaseActivity {

    @BindView(R.id.etSearchText)
    EditText searchEditText;


    @BindView(R.id.from_datEdit)
    EditText fromDateET;

    @BindView(R.id.to_dateEdit)
    EditText toDateET;

    @BindView(R.id.iBtnSearch)
    ImageButton searchBtn;

    @BindView(R.id.iBtnNew)
    ImageButton newBtn;

    @BindView(R.id.teleRecyclerView)
    RecyclerView afterTeleRecyclerView;

    @BindView(R.id.empty_view)
    View emptyTv;

    ArrayList<AfterSalesItem>salesitems;

    AfteSalesSearchAdapter salesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        salesitems=new ArrayList<>();
        afterTeleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        salesAdapter=new AfteSalesSearchAdapter(R.layout.item_list_aftersales,salesitems);

        afterTeleRecyclerView.setAdapter(salesAdapter);
        salesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AfterSalesItem salesitem=salesitems.get(i);
                Intent intent=new Intent(SearchAfterSalesActivity.this,AfterSalesActivity.class);
                intent.putExtra(AfterSalesActivity.EXTRA_OBJ,salesitem);
                startActivity(intent);
            }
        });
        salesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter baseQuickAdapter, final View view, final int i) {
               final AfterSalesItem item= salesitems.get(i);

               if (view.getId()==R.id.delete_after_sales_item)
               {
                    item.setLoading(true);
                    salesAdapter.notifyItemChanged(i);
                   item.setDeletedFlag("Y");
                   item.setUpdatedBy("android");
                   item.setUpdatedDate("01-01-2018");

                   AfterSales afterSales=new AfterSales();
                   afterSales.setAfterSalesItem(item);

                   App.getInstance().getService().updateAfterSales(afterSales).enqueue(new Callback<Message>() {
                       @Override
                       public void onResponse(Call<Message> call, Response<Message> response) {
                           item.setLoading(false);
                           salesAdapter.notifyItemChanged(i);
                           if(response.body() != null && response.body().getDetails()!= null) {
                               if (response.body().getCode()==1) {
                                   view.setVisibility(View.GONE);
                                   baseQuickAdapter.getViewByPosition(afterTeleRecyclerView, i, R.id.deleted_label).setVisibility(View.VISIBLE);
                               }
                           }
                       }

                       @Override
                       public void onFailure(Call<Message> call, Throwable throwable) {
                           Snackbar.make(findViewById(android.R.id.content),
                                   R.string.err_data_load_failed, Snackbar.LENGTH_LONG).show();
                       }
                   });

               }



            }
        });
        getSalesResponse("-1","-1","-1");
    }

    @OnClick(R.id.iBtnSearch)
    void setSearchEditText()
    {

        String searchKey="-1";
        String toDate="-1";
        String fromDate="-1";
        searchKey=searchEditText.getText().toString();
        toDate=toDateET.getText().toString();
        fromDate=fromDateET.getText().toString();


        if (!TextUtils.isEmpty(searchKey))
        {
            //search with search key
            getSalesResponse(searchKey,"-1","-1");

        }
        else if (!TextUtils.isEmpty(toDate) && !TextUtils.isEmpty(fromDate))
        {
            getSalesResponse("-1",fromDate,toDate);
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content),
                    "Please enter a search key or choose date pickers to filter search .", Snackbar.LENGTH_LONG).show();
        }
    }

    void getSalesResponse(String searchKey,String fromDate,String toDate)
    {
        DialogUtils.showDialog(SearchAfterSalesActivity.this,true);
        emptyTv.setVisibility(View.GONE);
        afterTeleRecyclerView.setVisibility(View.GONE);
        App.getInstance().getService().SearchAfterSales(searchKey,fromDate,toDate).enqueue(new Callback<ResponseAfterSales>() {

            @Override
            public void onResponse(Call<ResponseAfterSales> call, Response<ResponseAfterSales> response) {
                DialogUtils.showDialog(SearchAfterSalesActivity.this,false);

                if(response.body() != null && response.body().getMessage() != null) {

                    Message message = response.body().getMessage();
                    if (message.getCode() != 1) {
                        Snackbar.make(findViewById(android.R.id.content), response.body().getMessage().getDetails().toString(), Snackbar.LENGTH_LONG).show();
                    } else {

                        //initialize adapter
                        if (response.body().getList().isEmpty()){
                            emptyTv.setVisibility(View.VISIBLE);
                            afterTeleRecyclerView.setVisibility(View.GONE);

                        }else {
                            emptyTv.setVisibility(View.GONE);
                            afterTeleRecyclerView.setVisibility(View.VISIBLE);
                            salesitems.clear();
                            salesitems.addAll(response.body().getList());
                            salesAdapter.notifyDataSetChanged();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseAfterSales> call, Throwable throwable) {
                Snackbar.make(findViewById(android.R.id.content),
                        R.string.err_data_load_failed, Snackbar.LENGTH_LONG).show();
                DialogUtils.showDialog(SearchAfterSalesActivity.this,false);
                emptyTv.setVisibility(View.GONE);
                afterTeleRecyclerView.setVisibility(View.GONE);


            }
        });
    }
    @OnClick(R.id.iBtnNew)
    void newbtn(){

        Intent intent=new Intent(this,AfterSalesActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.fromDateBtn)
    void fromDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String newDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;                                fromDateET.setText(newDate);
                        fromDateET.setText(newDate);

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "datePickerDialog");

    }

    //   etBirthDate.getText().toString().replaceAll("/", "-"));
    @OnClick(R.id.todateBtn)
    void toDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String newDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        toDateET.setText(newDate);

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "datePickerDialog");
    }


    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_search_after_sales;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_search_after_tele;
    }


}
