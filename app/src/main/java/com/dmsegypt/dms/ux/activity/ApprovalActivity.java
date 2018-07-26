package com.dmsegypt.dms.ux.activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// search by card id and get user and add user approval
public class ApprovalActivity extends BaseActivity implements SwipeBackCoordinatorLayout.OnSwipeListener {
     //region Constants
    private static final String SAVE_POSITION ="pos" ;
    private final String TAG=this.getClass().getSimpleName();
    //endregion

    //region Init Views



    @BindView(R.id.idcontainer)
    LinearLayout idcontainer;

    @BindView(R.id.activity_approval_container)
    CoordinatorLayout container;


    @BindView(R.id.lineitem)
    ListView mlistusers;

    @BindView(R.id.search_id)
    Button searchid;



    @BindView(R.id.activity_settings_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;

    @BindView(R.id.fblock1)
    EditText block1;

    @BindView(R.id.fblock2)
    EditText block2;

    @BindView(R.id.fblock3)
    EditText block3;

    @BindView(R.id.fblock4)
    EditText block4;

    @BindView(R.id.Addbtn)
    Button submitbtn;


    @BindView(R.id.codegenerated)
    TextView codegenerated;

    @BindView(R.id.Codelinear)
    LinearLayout codelinear;
    ArrayAdapter<String> arrayAdapter;
    List<String> list=new ArrayList<>();
    Call call;
    //endregion

    private String mSelectedItem;
    @BindArray(R.array.approval_category_list)
    String[]category_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();




    }



    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_approval;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_approve;
    }


    @Override
    public boolean canSwipeBack(int dir) {
        return true;
    }

    @Override
    public void onSwipeProcess(float percent) {
        container.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));

    }

    @Override
    public void onSwipeFinish(int dir) {
        finishActivity(dir);

    }
    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
       onBackPressed();
    }



    /**
     * init function
     */
    public void initView(){
        mlistusers.setItemsCanFocus(true);
        submitbtn.setVisibility(View.GONE);
        swipeBackView.setOnSwipeListener(this);
        mlistusers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adapterView.setSelected(true);
                mSelectedItem=(String) adapterView.getItemAtPosition(position);
                submitbtn.setVisibility(View.VISIBLE);
            }
        });



    }


    @OnClick({R.id.search_id , R.id.Addbtn})
    public void ApproveOnClick(View view){
        switch (view.getId()){
            case R.id.search_id:
            //    View focusView = null;
                list=new ArrayList<>();
                SearchUserByID();

                break;
            case R.id.Addbtn:
                //focusView=null;
                AddUserApproval();
                break;
        }


    }

    private void AddUserApproval() {

        //region Store values at the time of the Approval attempt.

        String Manager="MON_PH";
        User user = App.getInstance().getPrefManager().getCurrentUser();
        String acc_id="0000";
        String acc_name=user.getEmail();
        String[] spilited=mSelectedItem.split("\\s+");
        String Card_id;
        String first_name;
        String second_name;
        String third_name;
        if (getAppLanguage().equals(Constants.Language.AR)){
            first_name=spilited[0];
            second_name=spilited[1];
            third_name=spilited[2];
            Card_id=spilited[3];

        }else {
            Card_id=spilited[0];
            third_name=spilited[1];
            second_name=spilited[2];
            first_name=spilited[3];
        }
        //endregion
        boolean cancel = false;
        if (cancel) {
            // There was an error; don't attempt AddUserApproval and focus the first
            // form field with an error.
        } else {
            // Show a progress spinner, and kick off a background task to
            DialogUtils.showDialog(this,true);

          call=  App.getInstance().getService().addUserApproval(Card_id,first_name,second_name,
                    third_name,Manager,acc_id,acc_name,getAppLanguage());
            call.enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response != null && response.body() != null) {
                        Snackbar.make(findViewById(android.R.id.content), response.body().getDetails().toString(), Snackbar.LENGTH_LONG).show();
                    codegenerated.setText(response.body().getUserid());
                        codelinear.setVisibility(View.VISIBLE);
                        DialogUtils.showDialog(ApprovalActivity.this,false);
                    }
                    DialogUtils.showDialog(ApprovalActivity.this,false);
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_LONG).show();
                    DialogUtils.showDialog(ApprovalActivity.this,false);

                }
            });

        }



    }
    //region search by card id if card id is not empty
    public void SearchUserByID(){
        if (block1.getText().toString().isEmpty()){
            block1.setError(getString(R.string.err_empty_field));
            block1.requestFocus();
            return;
        }

        if (block2.getText().toString().isEmpty()){
            block2.setError(getString(R.string.err_empty_field));
            block2.requestFocus();
            return;
        }

        if (block3.getText().toString().isEmpty()){
            block3.setError(getString(R.string.err_empty_field));
            block3.requestFocus();
            return;
        }

        if (block4.getText().toString().isEmpty()){
            block4.setError(getString(R.string.err_empty_field));
            block4.requestFocus();
            return;
        }

        //region Store values at the time of the Search User.
        final String CardId=block1.getText().toString().trim()+"-"+block2.getText().toString().trim()+"-"+block3.getText().toString().trim()+"-"+block4.getText().toString().trim();
        String UserPassword=App.getInstance().getPrefManager().getPassword();
        final String UserEmail=App.getInstance().getPrefManager().getUsername();
        //endregion

        // cancel for validation
        boolean cancel = false;
       // View focusView = null;
        if(cancel){
            // There was an error; don't attempt Search and focus the first
            // form field with an error.

        }else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Search attempt.
            DialogUtils.showDialog(this,true);


         //   String version = "android";
          //  String device = "sam" ;
            call=App.getInstance().getService().searchByCardId(CardId,UserEmail,UserPassword,getAppLanguage());
                    call.enqueue(new Callback<ResponseMembers>() {
                @Override
                public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {

                    if(response.body() != null && response.body().getMessage() != null) {

                        Message message = response.body().getMessage();
                        if (message.getCode() != 1) {
                            Snackbar.make(findViewById(android.R.id.content), response.body().getMessage().getDetails().toString(), Snackbar.LENGTH_LONG).show();
                            DialogUtils.showDialog(ApprovalActivity.this,false);
                        } else {
                            User user1 = response.body().getUser();
                                list.add(user1.getFirstName() + " " + user1.getSecondName() + " " + user1.getLastName() + " " + user1.getCardId());
                            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_approval, list);
                            mlistusers.setAdapter(arrayAdapter);
                            mlistusers.setVisibility(View.VISIBLE);

                            DialogUtils.showDialog(ApprovalActivity.this,false);
                            Snackbar.make(findViewById(android.R.id.content), response.body().getMessage().getDetails().toString(), Snackbar.LENGTH_LONG).show();


                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseMembers> call, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.err_data_load_failed, Snackbar.LENGTH_LONG).show();
                    DialogUtils.showDialog(ApprovalActivity.this,false);

                }
            });

        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
