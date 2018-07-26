package com.dmsegypt.dms.ux.activity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.Review;
import com.google.api.services.androidpublisher.model.ReviewsListResponse;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.isEmailValid;
import static com.dmsegypt.dms.utils.utils.isMobileNumberValid;

public class AddFamilyMember extends BaseActivity implements SwipeBackCoordinatorLayout.OnSwipeListener {


    //region Init View

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etSignupCardID)
    EditText etRelationCard;

    @BindView(R.id.etNationalID)
    EditText etNationalID;

    @BindView(R.id.etBirthDate)
    EditText etBirthDate;

    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.etConfirmMobileNumber)
    EditText etConfirmMobileNumber;

    @BindView(R.id.etSignupEmail)
    EditText etSignupEmail;

    @BindView(R.id.btn_add)
    Button btn_add;

    @BindView(R.id.iBtnGuide)
    ImageButton iBtnGuide;


    @BindView(R.id.tinputCard)
    TextInputLayout tinputRelationCard;

    @BindView(R.id.tinputFullName)
    TextInputLayout tinputFullName;

    @BindView(R.id.tinputNationalID)
    TextInputLayout tinputNationalID;

    @BindView(R.id.tinputBirthDate)
    TextInputLayout tinputBirthDate;

    @BindView(R.id.tinputMobileNumber)
    TextInputLayout tinputMobileNumber;

    @BindView(R.id.tinputConfirmMobileNumber)
    TextInputLayout tinputConfirmMobileNumber;

    @BindView(R.id.tinputSignupEmail)
    TextInputLayout tinputSignupEmail;

    AnimUtils a;
    @BindView(R.id.root)
    View rootLayout;



    //endregion


    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        startRevealAnimation();
        initView();


    }



    private void startRevealAnimation(){
        Intent intent = this.getIntent();
        a= new AnimUtils(rootLayout, intent, this, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        a.unRevealActivity();


    }

    private void initView() {
        iBtnGuide.setVisibility(View.GONE);
    }



    //region onClick handle
    @OnClick({R.id.btn_add ,R.id.iBtnGuide})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                addMember();
                break;
            case R.id.iBtnGuide:
              //  presentShowcaseView();
                break;


        }

    }




    @OnClick(R.id.iBtnCloseBtn)
    void close(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        }else {
            Intent intent = new Intent(this, EmployeesActivity.class);
            ActivityOptions options=ActivityOptions.makeCustomAnimation(this,R.anim.slide_in_right,R.anim.slide_out_left);
            this.startActivity(intent,options.toBundle());
            finish();
        }
    }

    private void addMember() {
        //region Store values at the time of add member attempt.
        String fullName = etFullName.getText().toString().trim();
        String nationalID = etNationalID.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String mobileNumberConfirm = etConfirmMobileNumber.getText().toString().trim();
        String email = etSignupEmail.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String RelationCardId=etRelationCard.getText().toString().trim();
        String SigninCardId= App.getInstance().getPrefManager().getUser().getCardId();
        boolean cancel = false;
        View focusView = null;

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
        if (TextUtils.isEmpty(birthDate)) {
            tinputBirthDate.setError(getString(R.string.error_field_required));
            focusView = etBirthDate;
            cancel = true;
        }
        //endregion


        //region Check for a valid CardId.
        if (TextUtils.isEmpty(RelationCardId)) {
            tinputRelationCard.setError(getString(R.string.error_field_required));
            focusView = etRelationCard;
            cancel = true;
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
            // There was an error; don't attempt send data and focus the first
            // form field with an error.
            if (focusView == etBirthDate) datePicker();
            else focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform sne request attempt.
            DialogUtils.showDialog(AddFamilyMember.this,true);
            App.getInstance().getService().addMembers(fullName,RelationCardId,nationalID,mobileNumber,email,birthDate.replaceAll("/", "-"),SigninCardId,getAppLanguage()).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    DialogUtils.showDialog(AddFamilyMember.this,false);
                    if (response != null && response.body() != null) {
                        if (response.body().getCode() == 1) {
                            Snackbar.make(findViewById(android.R.id.content), response.body().getDetails(), Snackbar.LENGTH_LONG).show();
                            onBackPressed();
                        }else if(response.body().getCode() ==0){
                            Snackbar.make(findViewById(android.R.id.content), response.body().getDetails(), Snackbar.LENGTH_LONG).show();
                        }else if(response.body().getCode()== 2) {
                            Snackbar.make(findViewById(android.R.id.content), response.body().getDetails(), Snackbar.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable throwable) {

                }
            });

        }




    }


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
                                    etBirthDate.setError("Invalide Date");
                                }
                            }else {
                                etBirthDate.setError("Invalide Date");
                            }
                        }else {
                            etBirthDate.setError("Invalide Date");
                        }
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
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_family_member;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    public boolean canSwipeBack(int dir) {
        return false;
    }

    @Override
    public void onSwipeProcess(float percent) {

    }

    @Override
    public void onSwipeFinish(int dir) {

    }
}
