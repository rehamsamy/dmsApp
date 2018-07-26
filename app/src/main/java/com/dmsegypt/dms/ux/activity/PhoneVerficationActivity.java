package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseOtp;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DialogUtils;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 08/02/2018.
 */

public class PhoneVerficationActivity extends BaseActivity {
    private static final String EXTRA_PHONE ="extra_phone" ;
    @BindView(R.id.verfiy_btn)
    Button verfiyBtn;
    @BindView(R.id.verfiy_et)
    EditText codeEdit;
    String phone;
    @BindView(R.id.phoneTV)
    TextView phoneTv;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @BindView(R.id.resend_code)
    TextView resendview;
    MaterialDialog dialog = null;
    Call call;
    User user=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        user = App.getInstance().getPrefManager().getUser();
        phone="+2"+user.getMobile();
        phoneTv.setText(phone);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                   sendOtp(user.getCardId());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(PhoneVerficationActivity.this,"Login Failed"+e.getMessage(), Toast.LENGTH_LONG).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(PhoneVerficationActivity.this,R.string.error_invalid_mobile, Toast.LENGTH_LONG).show();


                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

            }

        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }


    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_verfication;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


    @OnClick(R.id.verfiy_btn)
    public void verifyCode(View view) {
        String code = codeEdit.getText().toString();
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    @OnClick(R.id.resend_code)
    public void resendCode(View view) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                mResendToken);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                sendOtp(user.getCardId());

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneVerficationActivity.this,R.string.err_verf_code, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }



    private void sendOtp(String cardId){
        DialogUtils.showDialog(this,true);
        call= App.getInstance().getService().postOtp(cardId
                ,"-1");
        call.enqueue(new Callback<ResponseOtp>() {
            @Override
            public void onResponse(Call<ResponseOtp> call, Response<ResponseOtp> response) {
                if (response.body() != null) {
                    if (response.body().getCode() == 1) {
                        App.getInstance().getPrefManager().setLoggedIn(true);
                        Intent intent = new Intent(PhoneVerficationActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        PhoneVerficationActivity.this.finish();
                    } else {

                        Toast.makeText(PhoneVerficationActivity.this,R.string.err_verf_code, Toast.LENGTH_SHORT).show();
                    }
                }
                DialogUtils.showDialog(PhoneVerficationActivity.this,false);
            }

            @Override
            public void onFailure(Call<ResponseOtp> call, Throwable t) {
                //    Toast.makeText(OtpActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                SuperToast.create(PhoneVerficationActivity.this, getString(R.string.err_data_load_failed), 3000).show();
                DialogUtils.showDialog(PhoneVerficationActivity.this,false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
