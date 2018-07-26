package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Account;
import com.dmsegypt.dms.rest.model.Response.ResponseUserAccount;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.UserAccountAdapter;
import com.dmsegypt.dms.ux.dialogs.AdminEditUserActivity;
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

public class AdminViewAccountsActivity extends BaseActivity implements SearchView.OnQueryTextListener {
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        accountList=new ArrayList<>();
        adapter=new UserAccountAdapter(R.layout.account_user_item,accountList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                final Account account=accountList.get(i);
                account.setActive(account.getActive().equals(Constants.NOT_ACTIVE)?Constants.ACTIVE:Constants.NOT_ACTIVE);
                App.getInstance().getService().activateUserAccount(account.getCardId(),account.getActive(),getAppLanguage()).enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.body()!=null){
                            if (response.body().getCode()==1){
                                Toast.makeText(AdminViewAccountsActivity.this, account.getActive().equals(Constants.ACTIVE)?"Active":"Not Active", Toast.LENGTH_SHORT).show();
                                adapter.notifyItemChanged(i);
                            }else{
                                Toast.makeText(AdminViewAccountsActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Toast.makeText(AdminViewAccountsActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Toast.makeText(AdminViewAccountsActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();

                    }
                });



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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== AdminEditUserActivity.REQUEST_CODE&&resultCode==RESULT_OK){
            if (data.getBooleanExtra(AdminEditUserActivity.EXTRA_LOADING,false)){
            typeSpinner.setSelectedIndex(0);
            showAccounts(Constants.NONE,Constants.FIRST_PAGE,false);
            }
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

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setHint(R.string.hint_search_account);

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

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT){
                DialogUtils.ConfirmDialog(AdminViewAccountsActivity.this, getString(R.string.msg_delete_account), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        materialDialog.dismiss();
                        DialogUtils.showDialog(AdminViewAccountsActivity.this, true);
                        App.getInstance().getService().deleteAccount(accountList.get(position).getCardId(), getAppLanguage()).enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                DialogUtils.showDialog(AdminViewAccountsActivity.this, false);

                                StatusResponse res = response.body();
                                if (res != null) {
                                    if (res.getCode() == 1) {
                                        adapter.remove(position);
                                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_sucess_delete, Snackbar.LENGTH_LONG).show();
                                    } else {

                                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_err_delete, Snackbar.LENGTH_LONG).show();

                                    }

                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.label_no_conenction, Snackbar.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                DialogUtils.showDialog(AdminViewAccountsActivity.this, false);
                                Snackbar.make(findViewById(android.R.id.content), R.string.label_no_conenction, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        materialDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            Bitmap icon;
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                Paint p=new Paint();
                if(dX > 0){

                }
                else {
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background,p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dialog_close_dark);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                    c.drawBitmap(icon,null,icon_dest,p);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}

