package com.dmsegypt.dms.ux.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseChangePassword;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.dmsegypt.dms.ux.custom_view.coordinatorView.StatusBarView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.meetLengthValidation;
import static com.dmsegypt.dms.utils.utils.playAnimation;

/**
 * Created by Mohamed Abdallah on 1/04/2017.
 **/

public class SettingsActivity extends BaseActivity
        implements SwipeBackCoordinatorLayout.OnSwipeListener {




    //region Declare Variables and Views
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.activity_settings_container)
    CoordinatorLayout container;
    @BindView(R.id.btnNotificationSwitch)
    Switch btnNotificationSwitch;
    @BindView(R.id.activity_settings_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;


    //region changePassword Views
    @BindView(R.id.changePassSwipeBackView)
    SwipeBackCoordinatorLayout changePassSwipeBackView;
    @BindView(R.id.layoutChangePassContainer)
    CoordinatorLayout layoutChangePassContainer;
    @BindView(R.id.etOldPass)
    EditText etOldPass;
    @BindView(R.id.etNewPass)
    EditText etNewPass;
    @BindView(R.id.changePassStatusBar)
    StatusBarView changePassStatusBar;
    @BindView(R.id.tinputOldPass)
    TextInputLayout tinputOldPass;
    @BindView(R.id.tinputNewPass)
    TextInputLayout tinputNewPass;
    //endregion
     @BindView(R.id.lang_spinner)
    MaterialSpinner langSpinner;
    Call call;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      ButterKnife.bind(this);

        initView();
    }



//region Declare Toolbar
    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_settings;
    }

    //endregion



    private void initView() {
        swipeBackView.setOnSwipeListener(this);
        btnNotificationSwitch.setChecked(App.getInstance().getPrefManager().getNotificationPref());
        langSpinner.setItems(Constants.Langs);
        if (LocaleHelper.getLanguage(this).equals("en")){
            langSpinner.setSelectedIndex(0);
        }else {
            langSpinner.setSelectedIndex(1);

        }
        langSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
                switch (i){
                    case 0:updateLang("en"); break;
                    case 1:updateLang("ar");break;

                }

            }
        });
    }


    //update languae
private void updateLang(String lang){
        LocaleHelper.setLocale(this,lang);
    IntentManager.startSettingsActivity(this);

}




//region swipeback
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


    //endregion



    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        onBackPressed();
    }



    @OnClick({R.id.btnAccount, R.id.btnNotificationSwitch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAccount:
//                IntentManager.startActivity(this, IntentManager.ACTIVITY_PROFILE, R.anim.activity_in);
                break;
            case R.id.btnNotificationSwitch:
                App.getInstance().getPrefManager().setNotificationPref(btnNotificationSwitch.isChecked());
                break;
        }
    }



    @OnClick({R.id.btnChangePassword, R.id.btnSubmit, R.id.iBtnCloseBtn})
    public void ChangePassOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePassword:
                handleChangePassOpen();
                break;
            case R.id.btnSubmit:
                boolean cancel = false;
                View focusView = null;

                String oldPass = etOldPass.getText().toString().trim();
                String newPass = etNewPass.getText().toString().trim();

                //region Check for a valid new_image password, if the user entered one.
                if (TextUtils.isEmpty(newPass)) {
                    tinputNewPass.setError(getString(R.string.error_field_required));
                    focusView = etNewPass;
                    cancel = true;
                } else {
                    switch (meetLengthValidation(newPass)) {
                        case 0:
                            tinputNewPass.setError(getString(R.string.error_invalid_short_password));
                            focusView = etNewPass;
                            cancel = true;
                            break;
                        case 1:
                            tinputNewPass.setError(getString(R.string.error_invalid_long_password));
                            focusView = etNewPass;
                            cancel = true;
                            break;
                    }
                }
                //endregion
                //region Check for a valid old password, if the user entered one.
                if (TextUtils.isEmpty(oldPass)) {
                    tinputOldPass.setError(getString(R.string.error_field_required));
                    focusView = etOldPass;
                    cancel = true;
                } else {
                    switch (meetLengthValidation(oldPass)) {
                        case 0:
                            tinputOldPass.setError(getString(R.string.error_invalid_short_password));
                            focusView = etOldPass;
                            cancel = true;
                            break;
                        case 1:
                            tinputOldPass.setError(getString(R.string.error_invalid_long_password));
                            focusView = etOldPass;
                            cancel = true;
                            break;
                    }
                }
                //endregion

                if (cancel) focusView.requestFocus();
                else {
                   call= App.getInstance().getService().changePassword(App.getInstance().getPrefManager().getUser().getCardId(),
                            oldPass, newPass,getAppLanguage() );
                    call.enqueue(new Callback<ResponseChangePassword>() {
                        @Override
                        public void onResponse(Call<ResponseChangePassword> call, Response<ResponseChangePassword> response) {
                            if (response.body() != null) {
                                Message message = response.body();
                                if (message.getCode() == 1) {
                                    new MaterialDialog.Builder(SettingsActivity.this)
                                            .content(getString(R.string.password_changed_success))
                                            .positiveText(getString(R.string.Ok))
                                            .onPositive(null)
                                            .negativeText(null)
                                            .onNegative(null)
                                            .cancelable(true)
                                            .show();

                                    App.getInstance().getPrefManager().makeLogout();
                                    Intent intent = new Intent(SettingsActivity.this, AuthActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    SettingsActivity.this.finish();
                                } else {
                                    new MaterialDialog.Builder(SettingsActivity.this)
                                            .content(message.getDetails())
                                            .positiveText(getString(R.string.Ok))
                                            .onPositive(null)
                                            .negativeText(null)
                                            .onNegative(null)
                                            .cancelable(true)
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseChangePassword> call, Throwable t) {
                            Log.e(TAG, "API ForgetPassword error : " + t.toString());
                            new MaterialDialog.Builder(SettingsActivity.this)
                                    .content(getString(R.string.unknown_error))
                                    .positiveText("Ok")
                                    .onPositive(null)
                                    .negativeText(null)
                                    .onNegative(null)
                                    .cancelable(true)
                                    .show();
                        }
                    });
                }
                break;
            case R.id.iBtnCloseBtn:
                handleChangePassClose(SwipeBackCoordinatorLayout.DOWN_DIR);
                break;
        }
    }




    private void handleChangePassClose(int dir) {
        switch (dir) {
            case SwipeBackCoordinatorLayout.UP_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_top);
                break;

            case SwipeBackCoordinatorLayout.DOWN_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                break;
        }
        layoutChangePassContainer.setVisibility(View.GONE);
        changePassSwipeBackView.setOnSwipeListener(null);
        swipeBackView.setOnSwipeListener(this);
    }




    private void handleChangePassOpen() {
//        Toast.makeText(this, "pass  clicked", Toast.LENGTH_SHORT).show();
        swipeBackView.setOnSwipeListener(null);
        changePassSwipeBackView.setOnSwipeListener(new SwipeBackCoordinatorLayout.OnSwipeListener() {
            @Override
            public boolean canSwipeBack(int dir) {
                return layoutChangePassContainer.getVisibility() == View.VISIBLE;
            }

            @Override
            public void onSwipeProcess(float percent) {
                changePassStatusBar.setAlpha(1 - percent);
                layoutChangePassContainer.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
            }

            @Override
            public void onSwipeFinish(int dir) {
                handleChangePassClose(dir);
            }
        });
        etOldPass.setText("");
        etNewPass.setText("");
        etOldPass.requestFocus();
        layoutChangePassContainer.setVisibility(View.VISIBLE);
        playAnimation(this, layoutChangePassContainer, R.anim.activity_in);
    }



    //destroy Callback to avoid leak memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
