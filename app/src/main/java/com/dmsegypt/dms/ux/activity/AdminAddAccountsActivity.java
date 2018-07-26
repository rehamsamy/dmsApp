package com.dmsegypt.dms.ux.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Pharmacy;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.dmsegypt.dms.rest.model.Response.GetPharmaciesReponse;
import com.dmsegypt.dms.rest.model.Response.ResponseProviderTypes;
import com.dmsegypt.dms.rest.model.Response.ResponseRegister;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
import com.dmsegypt.dms.ux.dialogs.ProviderDailogFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.isEmailValid;
import static com.dmsegypt.dms.utils.utils.isMobileNumberValid;
import static com.dmsegypt.dms.utils.utils.meetLengthValidation;

/**
 * Created by amr on 14/02/2018.
 */

public class AdminAddAccountsActivity extends BaseActivity implements ProviderDailogFragment.OnPharmcyClickListner {
    public static final String EXTRA_USER_TYPE = "extra_user_type";

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
    @BindView(R.id.etCompanyId)
    EditText etSignupCompanyid;
    @BindView(R.id.btnSignup)
    Button btnSignup;
    String userType;
    @BindView(R.id.departmentSpinner)
    MaterialSpinner departSpinner;

    @BindView(R.id.pharmacySpinner)
    MaterialSpinner pharmacySpinner;


    @BindView(R.id.providerTypeSpinner)
    ProgressableSpinner providerTypeSpinner;


    @BindArray(R.array.dms_departement)
    String[] departmentList;


    List<String> providersName;
    List<Pharmacy> providersList;
    String selectedProviderId="-1",selectedProviderType="-1";
    private String typeId="3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        userType = getIntent().getStringExtra(EXTRA_USER_TYPE);
        if (userType.equals(Constants.USER_TYPE_HR)) {

            departSpinner.setVisibility(View.GONE);
            etSignupCompanyid.setVisibility(View.VISIBLE);

        } else if ((userType.equals(Constants.USER_TYPE_DMS))) {
            departSpinner.setVisibility(View.VISIBLE);
            etSignupCompanyid.setVisibility(View.GONE);
            departSpinner.setItems(departmentList);
        } else if (userType.equals(Constants.USER_TYPE_PROVIDER)) {

            pharmacySpinner.setVisibility(View.VISIBLE);
            providerTypeSpinner.setVisibility(View.VISIBLE);
            etSignupCompanyid.setVisibility(View.GONE);
            departSpinner.setVisibility(View.GONE);
            providerTypeSpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                @Override
                public void onRetry() {
                    getProidersTypes();
                }
            });
            getProidersTypes();
            providerTypeSpinner.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

                @Override
                public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {

                    selectedProviderType=Constants.providerTypesList.get(i).getId();

                }

            });

            pharmacySpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (providerTypeSpinner.getSpinner().getSelectedIndex() == 0) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_sel_type, Snackbar.LENGTH_SHORT).show();
                    } else {
                        ProviderDailogFragment fragment=ProviderDailogFragment.newInstance(   Constants.providerTypesList.
                                get(providerTypeSpinner.getSpinner().getSelectedIndex()).getId());
                        fragment.setListner(AdminAddAccountsActivity.this);
                        fragment.show(getSupportFragmentManager(),"providerdialog");
                    }
                }
            });
        }


    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_account;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @OnClick(R.id.btnSignup)
    void attempAddAccount() {

        //region Store values at the time of the login attempt.
        final String username = etSignupUsername.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String nationalID = etNationalID.getText().toString().trim();
        String cardID = !etSignupCardID.getText().toString().trim().isEmpty()?etSignupCardID.getText().toString().trim():"-1";
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String mobileNumberConfirm = etConfirmMobileNumber.getText().toString().trim();
        String email = etSignupEmail.getText().toString().trim();
        final String password = etSignupPassword.getText().toString().trim();
        String passwordConfirm = etConfirmPassword.getText().toString().trim();
        String companyId = etSignupCompanyid.getText().toString().trim();
        String departmentId = departSpinner.getSelectedIndex() + "";

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
        }else {
            tinputConfirmPassword.setError(null);
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
                    default:tinputSignupPassword.setError(null);

            }
        }
        //endregion

        //region Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            tinputSignupUsername.setError(getString(R.string.error_field_required));
            focusView = etSignupUsername;
            cancel = true;
        }else {
            tinputSignupUsername.setError(null);

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
        }else {
            tinputSignupEmail.setError(null);


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
        }else {

            tinputConfirmMobileNumber.setError(null);


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
        }else {
            tinputMobileNumber.setError(null);

        }
        //endregion

        //region Check for a valid BirthDate.
        if (TextUtils.isEmpty(etBirthDate.getText().toString().trim())) {
            tinputBirthDate.setError(getString(R.string.error_field_required));
            focusView = etBirthDate;
            cancel = true;
        }else {
            tinputBirthDate.setError(null);

        }
        //endregion

        //region Check for a valid card ID.
        /*if (TextUtils.isEmpty(cardID)) {
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
        }*/
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
        }else {
            tinputNationalID.setError(null);

        }
        //endregion

        if (TextUtils.isEmpty(fullName)) {
            tinputFullName.setError(getString(R.string.error_field_required));
            focusView = etFullName;
            cancel = true;
        } else if (fullName.split(" ").length < 3) {
            tinputFullName.setError(getString(R.string.error_invalid_fullname));
            focusView = etFullName;
            cancel = true;
        }else {
            tinputFullName.setError(null);

        }
        //endregion
        if (userType.equals(Constants.USER_TYPE_HR)) {
            if (TextUtils.isEmpty(companyId)) {
                etSignupCompanyid.setError(getString(R.string.error_field_required));
                focusView = etSignupCompanyid;
                cancel = true;

            }else {
                etSignupCompanyid.setError(null);

            }
        } else if (userType.equals(Constants.USER_TYPE_DMS)) {
            if (departSpinner.getSelectedIndex() == 0) {
                Snackbar.make(findViewById(android.R.id.content), R.string.msg_select_department, Snackbar.LENGTH_LONG).show();
                cancel = true;
            }
        } else if (userType.equals(Constants.USER_TYPE_PROVIDER)) {
            if (selectedProviderId.equals("-1")) {
                Snackbar.make(findViewById(android.R.id.content), "Select Pharmacy !", Snackbar.LENGTH_LONG).show();
                cancel = true;
            }
        }


        if (cancel) {
            if (focusView == etBirthDate) datePicker();
            else focusView.requestFocus();
        } else {
            DialogUtils.showDialog(this, true);
            String version = "android";
            String device = "sam";
            if (userType.equals(Constants.USER_TYPE_HR)) {
                departmentId = "null";
                selectedProviderType="-1";
                selectedProviderId="-1";
            } else if (userType.equals(Constants.USER_TYPE_DMS)) {
                companyId = "10000";
                selectedProviderType="-1";
                selectedProviderId="-1";
            }else if (userType.equals(Constants.USER_TYPE_PROVIDER)){
                companyId="null";
                departmentId="null";
            }



            App.getInstance().getService().addAccount(username, username, fullName, cardID, nationalID, mobileNumber, email, password, getAppLanguage(), version, device, etBirthDate.getText().toString().replaceAll("/", "-"), Constants.ACTIVE, userType, companyId, departmentId, selectedProviderId,selectedProviderType,"0").enqueue(new Callback<ResponseRegister>() {
                @Override
                public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                    DialogUtils.showDialog(AdminAddAccountsActivity.this, false);
                    if (response.body() != null) {
                        Message message = response.body().getMessage();
                        if (message.getCode() == 1) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.sucess_create_account, Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), message.getDetails(), Snackbar.LENGTH_LONG).show();

                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.label_no_conenction, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseRegister> call, Throwable t) {
                    DialogUtils.showDialog(AdminAddAccountsActivity.this, false);
                    Snackbar.make(findViewById(android.R.id.content), R.string.label_no_conenction, Snackbar.LENGTH_LONG).show();
                }
            });


        }


    }


    @OnFocusChange(R.id.etBirthDate)
    void datePickerFocus(boolean hasFocus) {
        if (hasFocus) datePicker();
    }

    @OnClick({R.id.tinputBirthDate, R.id.etBirthDate})
    void datePicker()
    {

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
                        if (nowyear > year) {
                            etBirthDate.setText(newDate);
                            etBirthDate.setError(null);
                        } else if (nowyear == year) {
                            if (nowmonth > monthOfYear) {
                                etBirthDate.setText(newDate);
                                etBirthDate.setError(null);
                            } else if (nowmonth == monthOfYear) {
                                if (nowday > dayOfMonth) {
                                    etBirthDate.setText(newDate);
                                    etBirthDate.setError(null);
                                } else {
                                    etBirthDate.setError(getString(R.string.invalid_date));
                                }
                            } else {
                                etBirthDate.setError(getString(R.string.invalid_date));
                            }
                        } else {
                            etBirthDate.setError(getString(R.string.invalid_date));
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


    private void getProidersTypes() {
         providerTypeSpinner.showLoading();
        App.getInstance().getService().getProviderTypes(getAppLanguage(), Constants.KEY_SEARCH_ID).enqueue(new Callback<ResponseProviderTypes>() {
            @Override
            public void onResponse(Call<ResponseProviderTypes> call, Response<ResponseProviderTypes> response) {

                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (Constants.providerTypesList == null)
                        Constants.providerTypesList = new ArrayList<ProviderType>();

                    if (message.getCode() != 1) {

                         providerTypeSpinner.showFailure();
                    } else {
                        // if return data call getCities function
                        Constants.providerTypesList = response.body().getList();
                        Constants.providerTypesList.set(0, new ProviderType("-1", getString(R.string.label_select_type)));

                        providerTypeSpinner.getSpinner().setItems(Constants.providerTypesList);
                        providerTypeSpinner.showSucess();

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseProviderTypes> call, Throwable throwable) {

providerTypeSpinner.showFailure();
            }
        });
    }

    private void getProvidersBytype(final String type) {
        App.getInstance().getService().getPharmacies("-1", type, "0").enqueue(new Callback<GetPharmaciesReponse>() {
            @Override
            public void onResponse(Call<GetPharmaciesReponse> call, Response<GetPharmaciesReponse> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().getCode() == 1) {
                        if (response.body().getList().size() > 0) {

                            providersName=new ArrayList<>();
                            providersList=new ArrayList<>();
                            for (Pharmacy pharmacy:response.body().getList())
                            {
                                providersList.add(pharmacy);
                                providersName.add(pharmacy.getName());
                            }
                            pharmacySpinner.setItems(providersName);

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getProvidersBytype(type);
                                        }
                                    }).setActionTextColor(Color.GREEN).show();
                        }

                    } else {

                        Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getProvidersBytype(type);
                                    }
                                }).setActionTextColor(Color.GREEN).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPharmaciesReponse> call, Throwable throwable) {

                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getProvidersBytype(type);
                            }
                        }).setActionTextColor(Color.GREEN).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void OnPharmcyClicked(Pharmacy item) {
        selectedProviderId=item.getCode();
        pharmacySpinner.setText(item.getName());
    }
    @OnClick(R.id.imgv_close)
    void close(){
        finish();
    }



}
