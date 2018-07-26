package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.SalesCallResposne;
import com.dmsegypt.dms.rest.model.Salesitem;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.SalesAdapter;
import com.dmsegypt.dms.ux.custom_view.StateView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTeleSalesActivity extends BaseActivity implements SearchView.OnQueryTextListener {




    @BindView(R.id.from_datEdit)
    EditText fromDateET;
    @BindView(R.id.to_dateEdit)
    EditText toDateET;

    @BindView(R.id.iBtnSearch)
    Button searchBtn;

    @BindView(R.id.iBtnNew)
    ImageButton newBtn;

    @BindView(R.id.teleRecyclerView)
    RecyclerView teleRecyclerView;
    @BindView(R.id.state_view)
    StateView stateView;

ArrayList<Salesitem>salesitems;
    SalesAdapter salesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        salesitems=new ArrayList<>();
        teleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        salesAdapter=new SalesAdapter(R.layout.sales_item,salesitems);
        teleRecyclerView.setAdapter(salesAdapter);
        salesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Salesitem salesitem=salesitems.get(i);
                Intent intent=new Intent(SearchTeleSalesActivity.this,AddEditTeleSalesActivity.class);
                intent.putExtra(AddEditTeleSalesActivity.EXTRA_OBJ,salesitem);
                startActivity(intent);
            }
        });
        salesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
               final Salesitem item= salesitems.get(i);

                if (view.getId()==R.id.deactive_imgv){
                    item.setLoading(true);
                    salesAdapter.notifyItemChanged(i);
                    App.getInstance().getService().cancelSalesCall(item.getId()).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            item.setLoading(false);
                            salesAdapter.notifyItemChanged(i);
                            if (response.body().getCode()==1){
                                salesitems.remove(i);
                                salesAdapter.notifyItemRemoved(i);

                                Toast.makeText(SearchTeleSalesActivity.this, R.string.label_cancel, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(SearchTeleSalesActivity.this, R.string.msg_failed, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            item.setLoading(false);
                            salesAdapter.notifyItemChanged(i);
                            Toast.makeText(SearchTeleSalesActivity.this, R.string.msg_failed, Toast.LENGTH_SHORT).show();

                        }
                    });

                }else if(view.getId()==R.id.finish_imgv){
                    item.setLoading(true);
                    salesAdapter.notifyItemChanged(i);
                    App.getInstance().getService().finishSalesCall(item.getId()).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            item.setLoading(false);
                            salesAdapter.notifyItemChanged(i);
                            if (response.body().getCode()==1){
                                salesitems.remove(i);
                                salesAdapter.notifyItemRemoved(i);
                                Toast.makeText(SearchTeleSalesActivity.this, R.string.label_finish, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(SearchTeleSalesActivity.this, R.string.msg_failed, Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            item.setLoading(false);
                            salesAdapter.notifyItemChanged(i);
                            Toast.makeText(SearchTeleSalesActivity.this, R.string.msg_failed, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.iBtnSearch)
    void setSearchEditText()
    {

        String toDate="-1";
        String fromDate="-1";
        toDate=toDateET.getText().toString();
        fromDate=fromDateET.getText().toString();


       if (!TextUtils.isEmpty(toDate) && !TextUtils.isEmpty(fromDate))
        {
            getSalesResponse("-1",fromDate,toDate);
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content),
                    "Please enter a search key or choose date pickers to filter search .", Snackbar.LENGTH_LONG).show();
        }
    }

    void getSalesResponse(final String searchKey, final String fromDate, final String toDate)
    {stateView.setVisibility(View.VISIBLE);
    stateView.showState(StateView.LOADING);
    stateView.setListener(new StateView.OnRetrListener() {
        @Override
        public void onRetry() {
            getSalesResponse(searchKey,fromDate,toDate);
        }
    });
        teleRecyclerView.setVisibility(View.GONE);
        App.getInstance().getService().searchSalesCall(searchKey,fromDate,toDate).enqueue(new Callback<SalesCallResposne>() {

            @Override
            public void onResponse(Call<SalesCallResposne> call, Response<SalesCallResposne> response) {
                DialogUtils.showDialog(SearchTeleSalesActivity.this,false);

                if(response.body() != null && response.body().getMessage() != null) {

                    Message message = response.body().getMessage();
                    if (message.getCode() != 1) {
                        stateView.showState(StateView.NO_CONNECTION);

                    } else {

                        //initialize adapter
                        if (response.body().getList().isEmpty()){
                            stateView.showState(StateView.EMPTY);
                            teleRecyclerView.setVisibility(View.GONE);

                        }else {
                            stateView.setVisibility(View.GONE);
                            teleRecyclerView.setVisibility(View.VISIBLE);
                            salesitems.clear();
                            salesitems.addAll(response.body().getList());
                            salesAdapter.notifyDataSetChanged();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<SalesCallResposne> call, Throwable throwable) {
                stateView.showState(StateView.NO_CONNECTION);
                teleRecyclerView.setVisibility(View.GONE);


            }
        });
    }
    @OnClick(R.id.iBtnNew)
    void newbtn(){

        Intent intent=new Intent(this,AddEditTeleSalesActivity.class);
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
        return R.layout.activity_tele_sales;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_search_tele;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setHint(R.string.hint_search_tele_sales);

        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                return true;
            }
        });

        searchView.setOnQueryTextListener(this);
        return true;


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getSalesResponse(query,"-1","-1");
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }



}
