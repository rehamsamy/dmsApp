package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Account;
import com.dmsegypt.dms.rest.model.Response.ResponseUserAccount;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.ux.adapter.UserAccountAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 13/02/2018.
 */

public class UserAccountActivity extends BaseActivity implements SearchView.OnQueryTextListener {
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
    ArrayList<Account>accountList;
    UserAccountAdapter adapter;
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

    private void iniView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        accountList=new ArrayList<>();
        adapter=new UserAccountAdapter(R.layout.account_user_item,accountList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                if (view.getId()==R.id.switchBtn){
                     final Account account=accountList.get(i);
                     account.setActive(account.getActive().equals(Constants.NOT_ACTIVE)?Constants.ACTIVE:Constants.NOT_ACTIVE);
                      App.getInstance().getService().activateUserAccount(account.getCardId(),account.getActive(),getAppLanguage()).enqueue(new Callback<StatusResponse>() {
                          @Override
                          public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                              if (response.body()!=null){
                                  if (response.body().getCode()==1){
                                      Toast.makeText(UserAccountActivity.this, account.getActive().equals(Constants.ACTIVE)?"Active":"Not Active", Toast.LENGTH_SHORT).show();
                                      adapter.notifyItemChanged(i);
                                  }{
                                      Toast.makeText(UserAccountActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();

                                  }
                              }else {
                                  Toast.makeText(UserAccountActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();
                              }
                          }

                          @Override
                          public void onFailure(Call<StatusResponse> call, Throwable t) {
                              Toast.makeText(UserAccountActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();

                          }
                      });



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
        return R.layout.activity_user_accounts;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_accounts;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
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
        showAccounts(query, Constants.FIRST_PAGE, false);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

