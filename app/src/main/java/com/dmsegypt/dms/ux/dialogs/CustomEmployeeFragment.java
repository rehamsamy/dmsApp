package com.dmsegypt.dms.ux.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.ux.adapter.EmployeesFragmentAdapter;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahmoud on 4/1/2018.
 */

public class CustomEmployeeFragment extends DialogFragment implements
        SwipeBackCoordinatorLayout.OnSwipeListener {
    public static final String EXTRA_ACTIVITY_TYPE ="extra_activity_type" ;
    //region Declare View and Variables
    private final String TAG = this.getClass().getSimpleName();

    private OnCompleteListener mListener;

    @BindView(R.id.activity_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;

    @BindView(R.id.activity_container)
    CoordinatorLayout container;


@BindView(R.id.progressDialog)
    ProgressBar progressBar;
    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView rvEmployees;

    @BindView(R.id.llNoResult)
    LinearLayout llNoResult;

    private EmployeesFragmentAdapter employeesAdapter;
    private List<User> empList=new ArrayList<>();
    private List<User> EmpViewList =new ArrayList<>();
//    private MemberAdapter memberApprovalAdpater;
    int itemcount=0;
    private Unbinder unbinder;
    private MaterialDialog loadingDialog;
    private int loadingCount = -1;
    private boolean isloaded;
    private Call call;



    //region search View on text Change
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

                employeesAdapter.notifyDataSetChanged();
                employeesAdapter.setEnableLoadMore(false);


            } else {
                EmpViewList.clear();
                EmpViewList.addAll(empList);
                employeesAdapter.notifyDataSetChanged();
                employeesAdapter.setEnableLoadMore(true);
                employeesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        getMembers(employeesAdapter.getItemCount() - 1);
                    }
                }, rvEmployees);
            }

        }

    };


    private void getMembers(final int lastIndex) {

        if (lastIndex == 0)
           // DialogUtils.showDialog(getActivity(),true);
            progressBar.setVisibility(View.VISIBLE);
        call= App.getInstance().getService().getEmployees(App.getInstance().getPrefManager().getUsername(),App.getInstance().getPrefManager().getPassword(),
                lastIndex, "En");
        call.enqueue(new Callback<ResponseMembers>() {
            @Override
            public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {

                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (message.getCode() != 1) {
                        if ((lastIndex == 0) && (message.getDetails().equals(getString(R.string.error_no_data_available)))) {
                            llNoResult.setVisibility(View.VISIBLE);
                            rvEmployees.setVisibility(View.GONE);
                            SuperToast.create(getActivity(), message.getDetails(), 3000).show();
                        } else if ((lastIndex != 0) && (message.getDetails().equals(getString(R.string.error_no_data_available)))) {
                            employeesAdapter.setEnableLoadMore(false);
                        }
                    } else {
                        itemcount+=response.body().getList().size();
                        for (User user:response.body().getList()){
                            if (user.getStatus().equals("1")){
                                empList.add(user);
                            }
                        }

                        if (lastIndex == 0) {
                            llNoResult.setVisibility(View.GONE);
                            rvEmployees.setVisibility(View.VISIBLE);
                            EmpViewList.clear();
                            EmpViewList.addAll(empList);
                            employeesAdapter.notifyDataSetChanged();
                            searchView.setOnQueryTextListener(listener);

                            employeesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                                    if (view.getId() == R.id.switch_emp_enable) {

                                        setUserStatus(position, ((Switch) view).isChecked());

                                            if(((Switch) view).isChecked()) empList.get(position).setStatus("1");
                                                else empList.get(position).setStatus("0");
                                    }else if(view.getId() == R.id.tv_editcard){
                                        IntentManager.startEditCardActivity(getActivity(),empList.get(position).getCardId());

                                    }

                                }
                            });

                            employeesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getMembers(itemcount);
                                }
                            }, rvEmployees);


                        } else {
                            if ((response.body().getList().size() % 20) != 0)
                                employeesAdapter.setEnableLoadMore(false);
                            EmpViewList.clear();
                            EmpViewList.addAll(empList);

                        }
                        employeesAdapter.notifyDataSetChanged();
                    }
                }

                if (lastIndex == 0)
                    progressBar.setVisibility(View.GONE);
                 //   DialogUtils.showDialog(getActivity(),false);

                employeesAdapter.loadMoreComplete();

            }

            @Override
            public void onFailure(Call<ResponseMembers> call, Throwable t) {
                if (lastIndex == 0){
                    progressBar.setVisibility(View.GONE);
                  //  DialogUtils.showDialog(getActivity(),false);
              /*      Snackbar.make(FAB, R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getMembers(0);
                                }
                            }).setActionTextColor(Color.GREEN).show();*/

                }
            }
        });
    }


    private void setUserStatus(final int position, boolean status) {


        String state = "" ;
        String cardId = empList.get(position).getCardId() ;



        if (!status) {
            state = "1" ;

            progressBar.setVisibility(View.VISIBLE);
          //  DialogUtils.showDialog(getActivity(),true);
            call= App.getInstance().getService().changeEmployeeStatus(cardId,  state, "En");
            call.enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                    if (response != null) {

                        //   Toast.makeText(EmployeesActivity.this, response.body().getDetails(), Toast.LENGTH_SHORT).show();
                        if(response.body().getDetails() != null) {
                            SuperToast.create(getActivity(), response.body().getDetails(), 3000).show();
                        }
                        empList.remove(position);
                        employeesAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                       // DialogUtils.showDialog(getActivity(),false);
                    }

                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {

                    Log.e(TAG, "API changeEmployeeStatus Error " + t.toString());
                    //DialogUtils.showDialog(getActivity(),false);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else {
            state = "0" ;
            SuperToast.create(getActivity()
                    ,getString(R.string.action_move_to), 3000).show();
            IntentManager.startHREditCard(getActivity());
            //finish();

        }




    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final  View view = inflater.inflate(R.layout.employee_fragment, container, false);


        ButterKnife.bind(this,view);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
        initView();
        getMembers(0);
        return view;
    }





//region switch back region

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
        //finishActivity(dir);
    }



    private void initView() {

      //  FAB.setVisibility(View.GONE);

        swipeBackView.setOnSwipeListener(this);

        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_by_emp));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEmployees.setLayoutManager(layoutManager);
        rvEmployees.setNestedScrollingEnabled(false);

        employeesAdapter=new EmployeesFragmentAdapter(R.layout.list_item_emp_fragment, EmpViewList);
        rvEmployees.setAdapter(employeesAdapter);
        employeesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
           //opearation
                mListener.onComplete(EmpViewList.get(i).getCardId());
                dismiss();
                //Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
            }
        });

    }

    public static interface OnCompleteListener {
        public abstract void onComplete(String time);
    }


    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
