package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Account;
import com.dmsegypt.dms.rest.model.CardIdsList;
import com.dmsegypt.dms.rest.model.Response.ResponseUserAccount;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.UserNotificationAccountAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNotificationActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    public static final int EMPTY_STATE=0;
    public static final int SUCESS_STATE=1;
    public static final int LOADING_STATE=2;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.typeSpinner)
    MaterialSpinner typeSpinner;


    @BindView(R.id.send_form_linearlayout)
    LinearLayout sendForm;

    @BindView(R.id.msg_edittext)
    EditText msgEdittext;

    @BindView(R.id.sendBtn)
    Button sendBtn;

    @BindView(R.id.send_notification_fab)
    FloatingActionButton sendNotificationFab;

    @BindView(R.id.cancel_notification_fab)
    FloatingActionButton cancelNotificationGFab;



    ArrayList<String> cardIdsArrayList;
    ArrayList<Account> accountList;
    UserNotificationAccountAdapter adapter;
    String page;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindArray(R.array.user_type_list)
    String[]userTypeList;
    private boolean loaded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        iniView();
    }

    @OnClick(R.id.send_notification_fab)
    void showSendForm(){

        sendForm.setVisibility(View.VISIBLE);
        sendNotificationFab.setVisibility(View.GONE);
        cancelNotificationGFab.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.cancel_notification_fab)
    void hideSendForm()
    {
        sendForm.setVisibility(View.GONE);
        sendNotificationFab.setVisibility(View.VISIBLE);
        cancelNotificationGFab.setVisibility(View.GONE);

    }
    @OnClick(R.id.sendBtn)
    void setSendBtn()
    {


        if (checkEditText(msgEdittext))
        {
            if (cardIdsArrayList.isEmpty())
            {
                Toast.makeText(getApplicationContext(),R.string.msg_empty_user_list,Toast.LENGTH_LONG).show();

            }else {
                String msg=msgEdittext.getText().toString().trim();
                CardIdsList cardIdsList=new CardIdsList();
                cardIdsList.setCardIdsList(cardIdsArrayList);
                DialogUtils.showDialog(this,true);
                App.getInstance().getService().sendUserNotification(cardIdsList,msg).enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        DialogUtils.showDialog(AdminNotificationActivity.this,false);

                        if (response.body()!=null) {
                            if( response.body().getCode()==1)
                            {
                                Snackbar.make(findViewById(android.R.id.content),R.string.msg_notifiction_sent,Snackbar.LENGTH_LONG).show();
                            }
                            else {
                                Snackbar.make(findViewById(android.R.id.content),R.string.msg_failed_notifiction,Snackbar.LENGTH_LONG).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        DialogUtils.showDialog(AdminNotificationActivity.this,false);
                        Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();
                    }
                });
            }


        }



    }

    private   boolean checkEditText(EditText editText) {
        String temp = editText.getText().toString().trim();
        boolean result = false;
        if (!TextUtils.isEmpty(temp)) {
            result = true;
        } else {
            editText.setError(getString(R.string.error_field_required));
            editText.requestFocus();
            result = false;
        }

        return result;
    }
    private void iniView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        accountList=new ArrayList<>();
        cardIdsArrayList=new ArrayList<>();
        adapter=new UserNotificationAccountAdapter(R.layout.account_notification_user_item,accountList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {

                if (view.getId()==R.id.checkbox){

                    final  Account account=accountList.get(i);
                    if (account.isChecked())
                    {
                        account.setChecked(false);
                        adapter.notifyDataSetChanged();
                        //is alredy in the finallist , so we will remove it from CardIdsList
                        if (cardIdsArrayList.contains(account.getCardId()))
                        {
                            cardIdsArrayList.remove(account.getCardId());
                        }

                    }
                    else if (!account.isChecked()){
                        account.setChecked(true);
                        adapter.notifyDataSetChanged();
                        cardIdsArrayList.add(account.getCardId());

                    }
                }


            }
        });
        typeSpinner.setItems(userTypeList);
        typeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
                switch (i){
                    case 0:showAccounts(Constants.NONE,Constants.FIRST_PAGE,false);break;
                    case 1:showAccounts(Constants.USER_TYPE_NORMAL,Constants.FIRST_PAGE,false);break;
                    case 2:showAccounts(Constants.USER_TYPE_HR,Constants.FIRST_PAGE,false);break;
                    case 3:showAccounts(Constants.USER_TYPE_PROVIDER,Constants.FIRST_PAGE,false);break;
                    case 4:showAccounts(Constants.USER_TYPE_DMS,Constants.FIRST_PAGE,false);break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loaded) {
            loaded=true;
            showAccounts(Constants.NONE, Constants.FIRST_PAGE, false);
        }
    }

    private void showAccounts(final String query, String page, final boolean isloadmore){
        if (!isloadmore)showProgress(LOADING_STATE);
        App.getInstance().getService().getUserAccounts(query,page, Constants.PAGE_SIZE,getAppLanguage()).enqueue(new Callback<ResponseUserAccount>() {
            @Override
            public void onResponse(Call<ResponseUserAccount> call, Response<ResponseUserAccount> response) {
                if (response.body()!=null) {
                    if (response.body().getList().isEmpty()) {
                        if (!isloadmore) {
                            showProgress(EMPTY_STATE);
                            adapter.loadMoreEnd();
                        }
                    } else {
                        if (!isloadmore){
                            showProgress(SUCESS_STATE);
                            accountList.clear();
                            cardIdsArrayList.clear();
                        }
                        accountList.addAll(response.body().getList());
                        adapter.notifyDataSetChanged();
                        adapter.loadMoreComplete();
                        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {
                                showAccounts(query,accountList.size()+1+"",true);
                            }
                        },recyclerView);
                    }
                }else {
                    if (isloadmore){
                        Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_SHORT).show();
                    }else {
                        sendNotificationFab.setVisibility(View.GONE);
                        showProgress(EMPTY_STATE);
                        Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.action_reload, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAccounts(query,Constants.FIRST_PAGE,false);

                                    }
                                }).show();

                    }
                }

            }




            @Override
            public void onFailure(Call<ResponseUserAccount> call, Throwable t) {

                if (isloadmore){
                    Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_SHORT).show();
                }else {
                    sendNotificationFab.setVisibility(View.GONE);

                    showProgress(EMPTY_STATE);
                    Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.action_reload, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showAccounts(query,Constants.FIRST_PAGE,false);

                                }
                            }).show();

                }







            }
        });


    }

    private void showProgress(int state){
        if (state==EMPTY_STATE) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }else if (state==SUCESS_STATE){
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else if (state==LOADING_STATE){
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_custom_notification;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_accounts;
    }

    private MenuItem checkAll;
    private MenuItem unCheckAll;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_notification_accounts_menu, menu);
        checkAll = menu.findItem(R.id.check_all);
        unCheckAll = menu.findItem(R.id.un_check_all);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setHint(R.string.hint_search_account);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.check_all:


                if (accountList.isEmpty())
                {

                    Toast.makeText(getApplicationContext(),getString(R.string.msg_empty_list),Toast.LENGTH_LONG).show();
                }else {
                    checkAll.setVisible(false);
                    unCheckAll.setVisible(true);
                    cardIdsArrayList.clear();

                    for (int i=0;i<accountList.size();i++)
                    {
                        accountList.get(i).setChecked(true);
                        adapter.notifyDataSetChanged();
                        cardIdsArrayList.add(accountList.get(i).getCardId());

                    }


                }
                return true;

            case R.id.un_check_all:


                if (accountList.isEmpty())
                {

                    Toast.makeText(getApplicationContext(),getString(R.string.msg_empty_list),Toast.LENGTH_LONG).show();


                }else {
                    checkAll.setVisible(true);
                    unCheckAll.setVisible(false);
                    cardIdsArrayList.clear();
                    for (int i=0;i<accountList.size();i++)
                    {
                        accountList.get(i).setChecked(false);
                        adapter.notifyDataSetChanged();

                    }


                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        showAccounts(query, Constants.FIRST_PAGE, false);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

