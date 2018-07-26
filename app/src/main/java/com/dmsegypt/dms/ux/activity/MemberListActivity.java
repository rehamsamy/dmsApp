package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Chat;
import com.dmsegypt.dms.rest.model.MemberChat;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.EmployeesAdapter;
import com.dmsegypt.dms.ux.adapter.MemberAdapter;
import com.dmsegypt.dms.ux.adapter.SeletcedUserAdapter;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by amr on 25/12/2017.
 */

public class MemberListActivity extends BaseActivity {
    public static final String EXTRA_GROUP ="extra_group" ;
    public static final String EXTRA_GROUP_TITLE ="extra_title" ;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.search_view)
    SearchView searchView;
    MemberAdapter adapter;
    int itemcount=0;
    private List<User> empList=new ArrayList<>();
    private List<User> EmpViewList =new ArrayList<>();
    @BindView(R.id.emtpy_tv)
    TextView emptyTv;
    @BindView(R.id.progress_bar)
    ProgressBar loadingView;
    @BindView(R.id.selected_recycler_view)
    RecyclerView selectedRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private int loadingCount = -1;
    private boolean isloaded;
    private Call call;
    boolean isGroup;
    SeletcedUserAdapter selectedAdpater;
    ArrayList<User> selectedUserList;
    User current_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        iniView();

    }
    void iniView(){
        current_user=App.getInstance().getPrefManager().getUser();
        isGroup=getIntent().getBooleanExtra(EXTRA_GROUP,false);
        if (isGroup){
            selectedUserList=new ArrayList<>();
            selectedRecyclerView.setHasFixedSize(true);
            selectedRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            selectedAdpater=new SeletcedUserAdapter(R.layout.list_selected_users_group,selectedUserList);
            selectedRecyclerView.setAdapter(selectedAdpater);
            selectedAdpater.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    User user=selectedUserList.get(i);
                    user.setSelected(false);
                    selectedUserList.remove(user);
                    adapter.notifyDataSetChanged();
                    selectedAdpater.notifyDataSetChanged();
                    selectedRecyclerView.setVisibility(selectedUserList.size()==0?View.GONE:View.VISIBLE);
                    fab.setVisibility(selectedUserList.size()>1?View.VISIBLE:View.GONE);

                }
            });
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new MemberAdapter(EmpViewList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
         if (isGroup){
          User user=EmpViewList.get(i);
             if (user.isSelected()){
                 user.setSelected(false);
                 selectedUserList.remove(user);
             }else {
                 user.setSelected(true);
                 selectedUserList.add(user);
             }
             adapter.notifyDataSetChanged();
             selectedAdpater.notifyDataSetChanged();
             selectedRecyclerView.setVisibility(selectedUserList.size()==0?View.GONE:View.VISIBLE);
             fab.setVisibility(selectedUserList.size()>1?View.VISIBLE:View.GONE);


         }else {
             Chat chat = new Chat();
             HashMap<String, MemberChat> memberMap = new HashMap<String, MemberChat>();
             MemberChat member1=new MemberChat();
             member1.setName(current_user.getFirstName());
             member1.setPush_token(App.getInstance().getPrefManager().getRegId());
             member1.setId(current_user.getCardId());
             memberMap.put(EmpViewList.get(i).getCardId(),member1);
             memberMap.put(current_user.getCardId(),member1);
             MemberChat member2=new MemberChat();
             member2.setName(EmpViewList.get(i).getFirstName());
             member2.setPush_token(App.getInstance().getPrefManager().getRegId());
             member2.setId(EmpViewList.get(i).getCardId());
             memberMap.put(EmpViewList.get(i).getCardId(),member2);
             chat.setMembers(memberMap);
             chat.setIsGroup(0);
             chat.setPrivate(true);
             chat.setId(current_user.getCardId().compareTo(EmpViewList.get(i).getCardId())>0?current_user.getCardId() + "_" + EmpViewList.get(i).getCardId():EmpViewList.get(i).getCardId()+"_"+current_user.getCardId());
             Intent intent = new Intent(MemberListActivity.this, MessageActivity.class);
             intent.putExtra(MessageActivity.EXTRA_CHAT_INFO, chat);
             startActivity(intent);
         }
            }
        });
        getMembers(0);
    }
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {

            setQuery(query);

            return true;
        }


        public boolean onQueryTextSubmit(String query) {

            setQuery(query);
            return false;
        }


        private void setQuery(String query) {
            if (!query.isEmpty()) {

                query = query.toLowerCase();

                //final List<User> filteredList = new_image ArrayList<>();
                EmpViewList.clear();
                for (int i = 0; i < empList.size(); i++) {

                    final String text = empList.get(i).getFirstName().toLowerCase()
                            + empList.get(i).getSecondName().toLowerCase()
                            + empList.get(i).getLastName().toLowerCase();

                    if (text.contains(query) ||
                            empList.get(i).getCardId().toLowerCase().contains(query)) {

                        EmpViewList.add(empList.get(i));
                    }
                }

                adapter.notifyDataSetChanged();
                adapter.setEnableLoadMore(false);


            } else {
                EmpViewList.clear();
                EmpViewList.addAll(empList);
                adapter.notifyDataSetChanged();
                adapter.setEnableLoadMore(true);
                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        getMembers(adapter.getItemCount() - 1);
                    }
                }, recyclerView);
            }

        }

    };
    private void getMembers(final int lastIndex) {

        if (lastIndex == 0){
          loadingView.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
        call= App.getInstance().getService().getEmployees(App.getInstance().getPrefManager().getUsername(),App.getInstance().getPrefManager().getPassword(),
                lastIndex, getAppLanguage());
        call.enqueue(new Callback<ResponseMembers>() {
            @Override
            public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {

                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (message.getCode() != 1) {
                        if ((lastIndex == 0) && (message.getDetails().equals(getString(R.string.error_no_data_available)))) {
                            emptyTv.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            loadingView.setVisibility(View.GONE);
                            SuperToast.create(MemberListActivity.this, message.getDetails(), 3000).show();
                        } else if ((lastIndex != 0) && (message.getDetails().equals(getString(R.string.error_no_data_available)))) {
                            adapter.setEnableLoadMore(false);
                        }
                    } else {
                        itemcount+=response.body().getList().size();
                        for (User user:response.body().getList()){
                            if (user.getStatus().equals("1")){
                                empList.add(user);
                            }
                        }

                        if (lastIndex == 0) {
                            emptyTv.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            loadingView.setVisibility(View.GONE);
                            EmpViewList.clear();
                            EmpViewList.addAll(empList);
                            adapter.notifyDataSetChanged();
                            searchView.setOnQueryTextListener(listener);


                            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getMembers(itemcount);
                                }
                            }, recyclerView);


                        } else {
                            if ((response.body().getList().size() % 20) != 0)
                                adapter.setEnableLoadMore(false);
                            EmpViewList.clear();
                            EmpViewList.addAll(empList);

                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                if (lastIndex == 0)
                    DialogUtils.showDialog(MemberListActivity.this,false);

                adapter.loadMoreComplete();

            }

            @Override
            public void onFailure(Call<ResponseMembers> call, Throwable t) {
                if (lastIndex == 0){
                    loadingView.setVisibility(View.GONE);
                    emptyTv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getMembers(0);
                                }
                            }).setActionTextColor(Color.GREEN).show();

                }
            }
        });
    }
    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_list_members;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.members_title;
    }


    public void onCreateGroup(View view) {
        String title=getIntent().getStringExtra(EXTRA_GROUP_TITLE);
        Chat chat = new Chat();
        HashMap<String, MemberChat> memberMap = new HashMap<String, MemberChat>();
        chat.setMembers(memberMap);
        chat.setIsGroup(1);
        chat.setPrivate(true);


        for (User u:selectedUserList){
            MemberChat member=new MemberChat();
            member.setName(u.getFirstName());
            member.setPush_token(App.getInstance().getPrefManager().getRegId());
            member.setId(u.getCardId());
            memberMap.put(u.getCardId(), member);
        }
        chat.setMembers(memberMap);
        chat.setId(title+System.currentTimeMillis());
        chat.setTitle(title);
        Intent intent = new Intent(MemberListActivity.this, MessageActivity.class);
        intent.putExtra(MessageActivity.EXTRA_CHAT_INFO, chat);
        startActivity(intent);
         finish();

    }
}
