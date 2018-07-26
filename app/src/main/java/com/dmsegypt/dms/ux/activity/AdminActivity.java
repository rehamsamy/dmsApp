package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 13/02/2018.
 */

public class AdminActivity extends BaseActivity {
    @BindView(R.id.usernameEdit)
    EditText usernameEdit;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    @BindView(R.id.admin_login_btn)
    Button loginButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    private void attempToLogin(){
        String username=usernameEdit.getText().toString().trim();
        String password=passwordEdit.getText().toString().trim();
        if (username.isEmpty()){
            usernameEdit.setError(getString(R.string.err_empty_field));
            usernameEdit.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordEdit.setError(getString(R.string.err_empty_field));
            passwordEdit.requestFocus();
            return;

        }

        DialogUtils.showDialog(this,true);
        App.getInstance().getService().loginAdmin(username,password).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                DialogUtils.showDialog(AdminActivity.this,false);
                if (response.body()!=null){
                    if (response.body().getCode()==1){
                        Snackbar.make(findViewById(android.R.id.content),R.string.sucees_login,Snackbar.LENGTH_SHORT).show();
                        IntentManager.startActivity(AdminActivity.this,IntentManager.ACTIVITY_ACCOUNTS);
                    }else {
                        Snackbar.make(findViewById(android.R.id.content),R.string.failed_login,Snackbar.LENGTH_LONG).show();

                    }
                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                DialogUtils.showDialog(AdminActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();

            }
        });


    }

    @OnClick(R.id.admin_login_btn)
    void loginAdmin(){
     attempToLogin();
    }




    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_admin;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }
}
