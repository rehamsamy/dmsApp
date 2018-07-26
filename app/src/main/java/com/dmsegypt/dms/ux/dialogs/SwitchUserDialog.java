package com.dmsegypt.dms.ux.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DataUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.activity.FamilyMemberActivity;
import com.dmsegypt.dms.ux.adapter.MembersAdapter;
import com.dmsegypt.dms.ux.adapter.UsersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Android on 6/10/2017.
 */

public class SwitchUserDialog extends DialogFragment {

    private View rootView;
    private Unbinder unbinder;

    @BindView(R.id.rv_users)
    RecyclerView rvUsers;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    private String TAG = getClass().getSimpleName();
    private UsersAdapter usersAdapter;

    UserListener userListener;
    @BindView(R.id.emtpy_tv)
    TextView llNoResult;

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialog_swich_user, container, false);

        getDialog().setTitle(getString(R.string.action_switch_user));
        unbinder = ButterKnife.bind(this, rootView);
        initUI();

        return rootView;
    }

    private void initUI() {

        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        getMembers();

    }

    private void getMembers() {

        showLoading(true);

        App.getInstance().getService().getMembers( App.getInstance().getPrefManager().getUser().getCardId(), "en")
                .enqueue(new Callback<ResponseMembers>() {
                    @Override
                    public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {
                        if (response.body() != null) {
                            List<User> userList = new ArrayList<User>();
                            User u=App.getInstance().getPrefManager().getUser();
                            u.setSwitch_user_type(u.getUserType());
                            userList.add(u);
                            if ((u.getUserType().equalsIgnoreCase(Constants.USER_TYPE_HR)
                                    ||u.getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS))
                                    && DataUtils.hasCardID(u.getCardId())){
                                User u2=App.getInstance().getPrefManager().getUser();
                                u2.setUserType(Constants.USER_TYPE_NORMAL);
                                u2.setSwitch_user_type(u2.getUserType());
                                userList.add(u2);
                            }
                            if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {


                                for (User user: response.body().getList()) {
                                    user.setSwitch_user_type(Constants.USER_TYPE_FAMILY_MEMBER);
                                    userList.add(user);
                                }




                            }
                            llNoResult.setVisibility(View.GONE);
                            rvUsers.setVisibility(View.VISIBLE);
                            usersAdapter = new UsersAdapter(getActivity(), R.layout.item_user, userList);
                            rvUsers.setAdapter(usersAdapter);
                            usersAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    App.getInstance().getPrefManager().setCurrentUser(usersAdapter.getItem(position));
                                    userListener.setOnUserSelectListener(usersAdapter.getItem(position).getUserType());
                                    dismiss();
                                }
                            });

                            /*else {
                                llNoResult.setVisibility(View.VISIBLE);
                                rvUsers.setVisibility(View.GONE);



                            }*/

                        }

                        showLoading(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseMembers> call, Throwable t) {
                        Log.e(TAG, "API getMembers Error " + t.toString());
                        showLoading(false);
                    }
                });
    }

    private void showLoading(boolean b) {

        if(b){

            mProgress.setVisibility(View.VISIBLE);
            rvUsers.setVisibility(View.GONE);

        }else {
            mProgress.setVisibility(View.GONE);
            rvUsers.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface UserListener{

        void setOnUserSelectListener(String userType);
    }
}
