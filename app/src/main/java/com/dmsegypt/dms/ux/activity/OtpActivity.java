package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseOtp;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.github.johnpersano.supertoasts.library.SuperToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 18/04/2017.
 **/

public class OtpActivity extends BaseActivity {
    //region UI VIEW
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tinputCode)
    TextInputLayout tinputCode;
//endregion
    //region references
    MaterialDialog dialog = null;
    Call call;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_otp;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


    @Override
    public void onBackPressed() {
        IntentManager.startActivityAndFinishMe(this, IntentManager.ACTIVITY_AUTH);
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmit() {

        try {
            if (etCode.getText().toString().trim().length() == 5 ) {
                String cardId = "";
                cardId = App.getInstance().getPrefManager().getUser().getCardId() ;
                sendOtp(cardId);





            } else tinputCode.setError(getString(R.string.error_verification_code));
        }
        catch (Exception ex) {
            DialogUtils.showDialog(OtpActivity.this,false);
            SuperToast.create(OtpActivity.this, ex.getMessage(), 3000).show();

        }
    }
private void sendOtp(String cardId){
    DialogUtils.showDialog(this,true);
    call=App.getInstance().getService().postOtp(cardId
            , etCode.getText().toString());
    call.enqueue(new Callback<ResponseOtp>() {
        @Override
        public void onResponse(Call<ResponseOtp> call, Response<ResponseOtp> response) {
            if (response.body() != null) {
                if (response.body().getCode() == 1) {
                    App.getInstance().getPrefManager().setLoggedIn(true);
                    Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    OtpActivity.this.finish();
                } else {
                    tinputCode.setError(response.body().getDetails());
                }
            }
            DialogUtils.showDialog(OtpActivity.this,false);
        }

        @Override
        public void onFailure(Call<ResponseOtp> call, Throwable t) {
            //    Toast.makeText(OtpActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            SuperToast.create(OtpActivity.this, getString(R.string.err_data_load_failed), 3000).show();
            DialogUtils.showDialog(OtpActivity.this,false);
        }
    });

}
    @OnTextChanged(R.id.etCode)
    void postOtpCode() {
        tinputCode.setError(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){call.cancel();}
    }
}
