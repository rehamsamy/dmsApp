package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseForgetPassword;
import com.dmsegypt.dms.rest.model.Response.ResponseLogin;
import com.dmsegypt.dms.rest.model.Response.ResponseRegister;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.dmsegypt.dms.ux.custom_view.VerticalTextView;
import com.dmsegypt.dms.ux.custom_view.coordinatorView.StatusBarView;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.isEmailValid;
import static com.dmsegypt.dms.utils.utils.isMobileNumberValid;
import static com.dmsegypt.dms.utils.utils.meetLengthValidation;
import static com.dmsegypt.dms.utils.utils.playAnimation;
import static com.dmsegypt.dms.utils.utils.promptToClose;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/
//login and register activity
public class AuthActivity extends BaseActivity
        implements SwipeBackCoordinatorLayout.OnSwipeListener {
    private static boolean isSigninScreen = true;



    //region Signin Views

    @BindView(R.id.progress)
    ProgressBar Progress;
    @BindView(R.id.tvSigninInvoker)
    VerticalTextView tvSigninInvoker;
    @BindView(R.id.tinputSigninUsername)
    TextInputLayout tinputSigninUsername;
    @BindView(R.id.tinputSigninPassword)
    TextInputLayout tinputSigninPassword;
    @BindView(R.id.etSigninUsername)
    EditText etSigninUsername;
    @BindView(R.id.etSigninPassword)
    EditText etSigninPassword;
    @BindView(R.id.btnSignin)
    Button btnSignin;
    @BindView(R.id.tvForgotPassword)
    Button tvForgotPassword;
    @BindView(R.id.llSignin)
    LinearLayout llSignin;
    //endregion

    //region Signup Views
    @BindView(R.id.tvSignupInvoker)
    VerticalTextView tvSignupInvoker;
    @BindView(R.id.tinputFullName)
    TextInputLayout tinputFullName;
    @BindView(R.id.tinputNationalID)
    TextInputLayout tinputNationalID;
    @BindView(R.id.tinputSignupCardID)
    TextInputLayout tinputSignupCardID;
    @BindView(R.id.tinputBirthDate)
    TextInputLayout tinputBirthDate;
    @BindView(R.id.tinputMobileNumber)
    TextInputLayout tinputMobileNumber;
    @BindView(R.id.tinputConfirmMobileNumber)
    TextInputLayout tinputConfirmMobileNumber;
    @BindView(R.id.tinputSignupEmail)
    TextInputLayout tinputSignupEmail;
    @BindView(R.id.tinputSignupUsername)
    TextInputLayout tinputSignupUsername;
    @BindView(R.id.tinputSignupPassword)
    TextInputLayout tinputSignupPassword;
    @BindView(R.id.tinputConfirmPassword)
    TextInputLayout tinputConfirmPassword;
    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etNationalID)
    EditText etNationalID;
    @BindView(R.id.etSignupCardID)
    EditText etSignupCardID;
    @BindView(R.id.etBirthDate)
    EditText etBirthDate;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.etConfirmMobileNumber)
    EditText etConfirmMobileNumber;
    @BindView(R.id.etSignupEmail)
    AutoCompleteTextView etSignupEmail;
    @BindView(R.id.etSignupUsername)
    EditText etSignupUsername;
    @BindView(R.id.etSignupPassword)
    EditText etSignupPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.btnSignup)
    Button btnSignup;
    @BindView(R.id.llSignup)
    LinearLayout llSignup;
    //endregion

    //region Forgot password Views
    @BindView(R.id.forgotStatusBar)
    StatusBarView forgotStatusBar;
    @BindView(R.id.iBtnCloseBtn)
    ImageButton iBtnCloseBtn;
    @BindView(R.id.etForgotPassCardId)
    EditText etForgotPassCardId;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.forgotSwipeBackView)
    SwipeBackCoordinatorLayout forgotSwipeBackView;
    @BindView(R.id.layoutForgotContainer)
    CoordinatorLayout layoutForgotContainer;
    @BindView(R.id.tinputEt)
    TextInputLayout tinputEt;
    //endregion

    //endregion

    //region Values references.
    @BindInt(R.integer.loginImeActionID)
    int loginImeActionID;
    @BindInt(R.integer.registerImeActionID)
    int registerImeActionID;
    private View mFormView;
    private int count = 0;
    private String TAG = this.getClass().getSimpleName();
    Call call;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getInstance().getPrefManager().isLoggedIn()
                &&App.getInstance().getPrefManager().getCurrentUser().getStatus().equals("1")) {
            IntentManager.startActivity(this, IntentManager.ACTIVITY_MAIN);
            finish();
            
        }
        ButterKnife.bind(this);

        showSigninForm();
        forgotSwipeBackView.setOnSwipeListener(this);
        mFormView = llSignin;
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_auth;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


    @Override
    public void onBackPressed() {
        if (layoutForgotContainer.getVisibility() == View.VISIBLE)
            onSwipeFinish(SwipeBackCoordinatorLayout.DOWN_DIR);
        else if (isTaskRoot())
            promptToClose(this);
        else super.onBackPressed();
    }

    //region OnTextChanged handle
    @OnTextChanged(R.id.etSigninUsername)
    public void handleSigninCardIDTextChange(Editable editable) {
        tinputSigninUsername.setError(null);
    }

    @OnTextChanged(R.id.etSigninPassword)
    public void handleSigninPasswordTextChange(Editable editable) {
        tinputSigninPassword.setError(null);
    }

    @OnTextChanged(R.id.etSignupUsername)
    public void handleUsernameTextChange(Editable editable) {
        tinputSignupUsername.setError(null);
    }

    @OnTextChanged(R.id.etFullName)
    public void handleFullNameTextChange(Editable editable) {
        tinputFullName.setError(null);
    }

    @OnTextChanged(R.id.etNationalID)
    public void handleNationalIDTextChange(Editable editable) {
        tinputNationalID.setError(null);
    }

    @OnTextChanged(R.id.etSignupCardID)
    public void handleSignupCardIDTextChange(Editable editable) {
        tinputSignupCardID.setError(null);
    }

    @OnTextChanged(R.id.etMobileNumber)
    public void handleMobileNumberTextChange(Editable editable) {
        tinputMobileNumber.setError(null);
    }

    @OnTextChanged(R.id.etSignupEmail)
    public void handleSignupEmailTextChange(Editable editable) {
        tinputSignupEmail.setError(null);
    }

    @OnTextChanged(R.id.etSignupPassword)
    public void handleSignupPasswordTextChange(Editable editable) {
        tinputSignupPassword.setError(null);
    }

    @OnTextChanged(R.id.etForgotPassCardId)
    public void handleFogotPasswordTextChange(Editable editable) {
        tinputEt.setError(null);
    }
    //endregion

    //region OnEditorAction handle
    @OnEditorAction({R.id.etSigninPassword, R.id.etSignupPassword})
    public boolean onEditorActionSignin(TextView textView, int id, KeyEvent keyEvent) {
        int imeActionID = textView.getImeActionId();
        if (imeActionID == loginImeActionID && id == EditorInfo.IME_ACTION_DONE) {
            attemptLogin();
            return true;
        } else if (imeActionID == registerImeActionID && id == EditorInfo.IME_ACTION_DONE) {
            attemptRegister();
            return true;
        }
        return false;
    }

    //endregion

    //region onClick handle
    @OnClick({R.id.tvSigninInvoker, R.id.btnSignin,
            R.id.tvForgotPassword, R.id.tvSignupInvoker,
            R.id.btnSignup, R.id.btnSubmit,
            R.id.iBtnCloseBtn, R.id.btnSkip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSigninInvoker:
                isSigninScreen = true;
                removeAllErrors();
                showSigninForm();
                etSigninUsername.requestFocus();
                break;

            case R.id.btnSignin:
                attemptLogin();
                break;

            case R.id.tvForgotPassword:
                layoutForgotContainer.setVisibility(View.VISIBLE);
                playAnimation(this, layoutForgotContainer, R.anim.activity_in);
                etForgotPassCardId.requestFocus();
                break;

            case R.id.tvSignupInvoker:
                isSigninScreen = false;
                removeAllErrors();
                showSignupForm();
                etFullName.requestFocus();
                break;

            case R.id.btnSignup:
                attemptRegister();
                break;

            case R.id.btnSubmit:
                //region Check for a valid email address and call th API.
                String cardID = etForgotPassCardId.getText().toString().trim();
                if (TextUtils.isEmpty(cardID))
                    tinputEt.setError(getString(R.string.error_field_required));
                else
                    switch (meetLengthValidation(cardID)) {
                        case 0:
                        case 1:
                            tinputSignupCardID.setError(getString(R.string.error_invalid_cardID));
                            break;
                        case 2:
                           call= App.getInstance().getService().forgetPassword(cardID, getAppLanguage());
                            call.enqueue(new Callback<ResponseForgetPassword>() {
                                @Override
                                public void onResponse(Call<ResponseForgetPassword> call, Response<ResponseForgetPassword> response) {
                                    if (response.body() != null) {
                                        Message message = response.body();
                                        if (message.getCode() == 1) {
                                            SuperToast.create(AuthActivity.this, getString(R.string.password_sent_to_email), 3000).show();

                                        } else
                                        new MaterialDialog.Builder(AuthActivity.this)
                                                .content(message.getDetails())
                                                .positiveText(R.string.ok)
                                                .onPositive(null)
                                                .negativeText(null)
                                                .onNegative(null)
                                                .cancelable(true)
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseForgetPassword> call, Throwable t) {
                                    Log.e(TAG, "API ForgetPassword error : " + t.toString());
                                    SuperToast.create(AuthActivity.this,getString(R.string.unknown_error), 3000).show();

                                }
                            });
                            break;
                    }
                //endregion
                break;

            case R.id.iBtnCloseBtn:
                onSwipeFinish(SwipeBackCoordinatorLayout.DOWN_DIR);
                break;

            case R.id.btnSkip:
                IntentManager.startActivity(this, IntentManager.ACTIVITY_MAIN);
        }
    }

    //endregion

    //region SwipeBack Functions
    @Override
    public boolean canSwipeBack(int dir) {
        return layoutForgotContainer.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onSwipeProcess(float percent) {
        forgotStatusBar.setAlpha(1 - percent);
        layoutForgotContainer.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
    }

    @Override
    public void onSwipeFinish(int dir) {
        switch (dir) {
            case SwipeBackCoordinatorLayout.UP_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_top);
                break;

            case SwipeBackCoordinatorLayout.DOWN_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                break;
        }
        layoutForgotContainer.setVisibility(View.GONE);
    }
    //endregion

    //region Toggle between Signin & Signup forms

    /**
     * to show Signup form
     */
    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        playAnimation(this, llSignup, R.anim.translate_right_to_left);

        playAnimation(this, btnSignup, R.anim.rotate_right_to_left);
    }

    /**
     * to show Signin form
     */
    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        playAnimation(this, llSignin, R.anim.translate_left_to_right);
//        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
//        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        playAnimation(this, btnSignin, R.anim.rotate_left_to_right);
//        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
//        btnSignin.startAnimation(clockwise);
    }


    /**
     * To reset all errors in the ETs
     */
    private void removeAllErrors() {
        tinputSigninUsername.setError(null);
        tinputSigninPassword.setError(null);
        etSigninUsername.setError(null);
        etSigninPassword.setError(null);

        tinputBirthDate.setError(null);
        tinputSignupUsername.setError(null);
        tinputFullName.setError(null);
        tinputConfirmMobileNumber.setError(null);
        tinputConfirmPassword.setError(null);
        tinputNationalID.setError(null);
        tinputSignupCardID.setError(null);
        tinputMobileNumber.setError(null);
        tinputSignupEmail.setError(null);
        tinputSignupPassword.setError(null);
        etSignupUsername.setError(null);
        etFullName.setError(null);
        etNationalID.setError(null);
        etSignupCardID.setError(null);
        etMobileNumber.setError(null);
        etSignupEmail.setError(null);
        etSignupPassword.setError(null);
        etConfirmMobileNumber.setError(null);
        etConfirmPassword.setError(null);
        etBirthDate.setError(null);
    }
    //endregion

    //region to attempt Login & Register fields

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        // Store values at the time of the login attempt.
        final String username = etSigninUsername.getText().toString().trim();
        final String password = etSigninPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            tinputSigninPassword.setError(getString(R.string.error_field_required));
            focusView = etSigninPassword;
            cancel = true;
        } else {
            switch (meetLengthValidation(password)) {
                case 0:
                    tinputSigninPassword.setError(getString(R.string.error_invalid_short_password));
                    focusView = etSigninPassword;
                    cancel = true;
                    break;
                case 1:
                    tinputSigninPassword.setError(getString(R.string.error_invalid_long_password));
                    focusView = etSigninPassword;
                    cancel = true;
                    break;
            }
        }

        // Check for a valid card ID.
        if (TextUtils.isEmpty(username)) {
            tinputSigninUsername.setError(getString(R.string.error_field_required));
            focusView = etSigninUsername;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            DialogUtils.showDialog(this,true);

             call=App.getInstance().getService().login(username, password, getAppLanguage(),
                    App.getInstance().getPrefManager().getRegId());
                    call.enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            if (response.body() != null) {
                                App.getInstance().getPrefManager().setUsername(username);
                                App.getInstance().getPrefManager().setPassword(password);
                                Message message = response.body().getMessage();
                                if (message.getCode() == 1) {
                                    App.getInstance().getPrefManager().createLogin(response.body().getUser());
                                    IntentManager.startActivityAndFinishMe(AuthActivity.this, IntentManager.ACTIVITY_MAIN);
                                } else {
                                    new MaterialDialog.Builder(AuthActivity.this)
                                            .content(message.getDetails())
                                            .positiveText(R.string.ok)
                                            .onPositive(null)
                                            .negativeText(null)
                                            .onNegative(null)
                                            .cancelable(true)
                                            .show();

                                    if (message.getCode() == 2) {
                                        App.getInstance().getPrefManager().createLogin(response.body().getUser());
                                        IntentManager.startActivity(AuthActivity.this, IntentManager.ACTIVITY_OTP);
                                    }
                                }
                            }else {
                                SuperToast.create(AuthActivity.this,getString(R.string.unknown_user), 3000).show();

                            }
                            DialogUtils.showDialog(AuthActivity.this,false);
                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            SuperToast.create(AuthActivity.this, getString(R.string.label_no_conenction), 3000).show();
                            DialogUtils.showDialog(AuthActivity.this,false);
                        }
                    });
        }
    }

    /**
     * Attempts to register the account specified by the register form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual register attempt is made.
     */
    private void attemptRegister() {

        //region Store values at the time of the login attempt.
        final String username = etSignupUsername.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String nationalID = etNationalID.getText().toString().trim();
        String cardID = etSignupCardID.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String mobileNumberConfirm = etConfirmMobileNumber.getText().toString().trim();
        String email = etSignupEmail.getText().toString().trim();
        final String password = etSignupPassword.getText().toString().trim();
        String passwordConfirm = etConfirmPassword.getText().toString().trim();
        //endregion

        boolean cancel = false;
        View focusView = null;

        //region Check for a valid password confirmation, if the user entered one.
        if (TextUtils.isEmpty(passwordConfirm)) {
            tinputConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = etConfirmPassword;
            cancel = true;
        } else if (!passwordConfirm.equals(password)) {
            tinputConfirmPassword.setError(getString(R.string.error_not_match_password));
            focusView = etConfirmPassword;
            cancel = true;
        }
        //endregion

        //region Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            tinputSignupPassword.setError(getString(R.string.error_field_required));
            focusView = etSignupPassword;
            cancel = true;
        } else {
            switch (meetLengthValidation(password)) {
                case 0:
                    tinputSignupPassword.setError(getString(R.string.error_invalid_short_password));
                    focusView = etSignupPassword;
                    cancel = true;
                    break;
                case 1:
                    tinputSignupPassword.setError(getString(R.string.error_invalid_long_password));
                    focusView = etSignupPassword;
                    cancel = true;
                    break;
            }
        }
        //endregion

        //region Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            tinputSignupUsername.setError(getString(R.string.error_field_required));
            focusView = etSignupUsername;
            cancel = true;
        }
        //endregion

        //region Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            tinputSignupEmail.setError(getString(R.string.error_field_required));
            focusView = etSignupEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            tinputSignupEmail.setError(getString(R.string.error_invalid_email));
            focusView = etSignupEmail;
            cancel = true;
        }
        //endregion

        //region  Check for a valid mobile number confirmation.
        if (TextUtils.isEmpty(mobileNumberConfirm)) {
            tinputConfirmMobileNumber.setError(getString(R.string.error_field_required));
            focusView = etConfirmMobileNumber;
            cancel = true;
        } else if (!mobileNumberConfirm.equals(mobileNumber)) {
            tinputConfirmMobileNumber.setError(getString(R.string.error_not_match_mobile));
            focusView = etConfirmMobileNumber;
            cancel = true;
        }
        //endregion

        //region  Check for a valid mobile number.
        if (TextUtils.isEmpty(mobileNumber)) {
            tinputMobileNumber.setError(getString(R.string.error_field_required));
            focusView = etMobileNumber;
            cancel = true;
        } else if (!isMobileNumberValid(mobileNumber)) {
            tinputMobileNumber.setError(getString(R.string.error_invalid_mobile));
            focusView = etMobileNumber;
            cancel = true;
        }
        //endregion

        //region Check for a valid BirthDate.
        if (TextUtils.isEmpty(username)) {
            tinputBirthDate.setError(getString(R.string.error_field_required));
            focusView = etBirthDate;
            cancel = true;
        }
        //endregion

        //region Check for a valid card ID.
        if (TextUtils.isEmpty(cardID)) {
            tinputSignupCardID.setError(getString(R.string.error_field_required));
            focusView = etSignupCardID;
            cancel = true;
        } else {
            switch (meetLengthValidation(cardID)) {
                case 0:
                case 1:
                    tinputSignupCardID.setError(getString(R.string.error_invalid_cardID));
                    focusView = etSignupCardID;
                    cancel = true;
                    break;
            }
        }
        //endregion

        //region Check for a valid national ID.
        if (TextUtils.isEmpty(nationalID)) {
            tinputNationalID.setError(getString(R.string.error_field_required));
            focusView = etNationalID;
            cancel = true;
        } else if (14 != nationalID.length()) {
            tinputNationalID.setError(getString(R.string.error_invalid_nationalID));
            focusView = etNationalID;
            cancel = true;
        }
        //endregion

        //region Check for a valid fullName.
        if (TextUtils.isEmpty(fullName)) {
            tinputFullName.setError(getString(R.string.error_field_required));
            focusView = etFullName;
            cancel = true;
        } else if (fullName.split(" ").length < 3) {
            tinputFullName.setError(getString(R.string.error_invalid_fullname));
            focusView = etFullName;
            cancel = true;
        }
        //endregion

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView == etBirthDate) datePicker();
            else focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            DialogUtils.showDialog(this,true);


            String version = "android";
            String device = "sam" ;

           call= App.getInstance().getService().register(username, fullName, cardID, nationalID,
                    mobileNumber, email, password, getAppLanguage(), version, device,
                    etBirthDate.getText().toString().replaceAll("/", "-"));
                    call.enqueue(new Callback<ResponseRegister>() {
                        @Override
                        public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                            if (response.body() != null) {
                                App.getInstance().getPrefManager().setUsername(username);
                                App.getInstance().getPrefManager().setPassword(password);
                                Message message = response.body().getMessage();
                                if (message.getCode() == 1) {
                                    App.getInstance().getPrefManager().createRegister(response.body().getUser());
                                    IntentManager.startActivity(AuthActivity.this, IntentManager.ACTIVITY_OTP);
                                } else {
                                    if (message.getDetails().equals(getString(R.string.error_cardID_registered_before))) {
                                        etSignupCardID.requestFocus();
                                        tinputSignupCardID.setError("this " + message.getDetails());
                                    } else if (message.getDetails().equals(R.string.error_username_not_available)) {
                                        etSignupUsername.requestFocus();
                                        tinputSignupUsername.setError("this" + message.getDetails());
                                    } else {
                                   //     Toast.makeText(AuthActivity.this, message.getDetails(), Toast.LENGTH_SHORT).show();
                                        SuperToast.create(AuthActivity.this, message.getDetails(), 3000).show();

                                       // new_image MaterialDialog.Builder(getParent())
//                                                .content(message.getDetails())
//                                                .positiveText("Ok")
//                                                .onPositive(null)
//                                                .negativeText(null)
//                                                .onNegative(null)
//                                                .cancelable(true)
//                                                .show();

                                    }
                                }
                            }
                            DialogUtils.showDialog(AuthActivity.this,false);
                        }

                        @Override
                        public void onFailure(Call<ResponseRegister> call, Throwable t) {
                         //   Toast.makeText(AuthActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                            SuperToast.create(AuthActivity.this, getString(R.string.label_no_conenction), 3000).show();

                            DialogUtils.showDialog(AuthActivity.this,false);
                        }
                    });
        }
    }
    //endregion

    @OnFocusChange(R.id.etBirthDate)
    void datePickerFocus(boolean hasFocus) {
        if (hasFocus) datePicker();
    }

    @OnClick({R.id.tinputBirthDate, R.id.etBirthDate})
    void datePicker() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String newDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Calendar c = Calendar.getInstance();
                        int nowyear = c.get(Calendar.YEAR);
                        int nowmonth = c.get(Calendar.MONTH);
                        int nowday = c.get(Calendar.DAY_OF_MONTH);
                        if(nowyear > year ){
                            etBirthDate.setText(newDate);
                            etBirthDate.setError(null);
                        }else if(nowyear == year) {
                            if(nowmonth > monthOfYear){
                                etBirthDate.setText(newDate);
                                etBirthDate.setError(null);
                            }else if (nowmonth == monthOfYear) {
                                if(nowday > dayOfMonth){
                                    etBirthDate.setText(newDate);
                                    etBirthDate.setError(null);
                                }else {
                                    etBirthDate.setError(getString(R.string.invalid_date));
                                }
                            }else {
                                etBirthDate.setError(getString(R.string.invalid_date));                            }
                        }else {
                            etBirthDate.setError(getString(R.string.invalid_date));                        }
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "datePickerDialog");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
