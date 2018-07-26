package com.dmsegypt.dms.ux.dialogs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Account;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Pharmacy;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.dmsegypt.dms.rest.model.Response.GetPharmaciesReponse;
import com.dmsegypt.dms.rest.model.Response.ResponseProviderTypes;
import com.dmsegypt.dms.rest.model.Response.ResponseRegister;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.activity.AdminAddAccountsActivity;
import com.dmsegypt.dms.ux.activity.BaseActivity;
import com.dmsegypt.dms.ux.adapter.InfiniteRecyclerViewAdapter;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
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
 * Created by mahmoud on 20/03/18.
 */

public class AdminEditUserActivity extends BaseActivity implements ProviderDailogFragment.OnPharmcyClickListner {
    public static final String EXTRA_USER_ACCOUNT="account";
    public static final int REQUEST_CODE =22 ;
    public static final String EXTRA_LOADING ="extra_loading" ;

    @BindArray(R.array.usertype)
    String[]usertype;

    @BindView(R.id.name)
    EditText et_name;

    @BindView(R.id.phone)
    EditText et_phone;

    @BindView(R.id.cardId)
    EditText et_cardId;

    @BindView(R.id.email)
    EditText et_email;

    @BindView(R.id.password)
    EditText et_password;

    @BindView(R.id.birthdate)
    EditText et_birthdate;

    @BindView(R.id.typespinner)
    MaterialSpinner ms_type;


    @BindView(R.id.ib_edit)
    Button ib_edit;

    Account user_account;
    @BindView(R.id.etCompanyId)
    EditText companyIdEdit;
    @BindView(R.id.tinputCompanyId)
    TextInputLayout companyIdInput;
    @BindView(R.id.departmentSpinner)
    MaterialSpinner departSpinner;

    @BindView(R.id.pharmacySpinner)
    ProgressableSpinner pharmacySpinner;


    @BindView(R.id.providerTypeSpinner)
    ProgressableSpinner providerTypeSpinner;

    @BindArray(R.array.dms_departement)
    String[] departmentList;
    @BindView(R.id.progress)
    View LoadingView;
    @BindView(R.id.UsernameEdit)
    EditText usernameEdit;


    List<String> providersName;
    List<Pharmacy> providersList;
    String selectedProviderId="-1",selectedProviderType="-1";
    private boolean isRefreshed=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        user_account= (Account) getIntent().getSerializableExtra(EXTRA_USER_ACCOUNT);
        if(user_account != null) {
            et_name.setText(user_account.getFirstName() + " " + user_account.getSecondName() +" "+ user_account.getThirdName());
            et_password.setText(user_account.getPassword());
            et_cardId.setText(user_account.getCardId().equals("-1")?"None":user_account.getCardId());
            et_email.setText(user_account.getEmail());
            et_phone.setText(user_account.getMobile());
            usernameEdit.setText(user_account.getName());
            et_birthdate.setText(user_account.getBirthdate());
            ms_type.setItems(usertype);
            departSpinner.setItems(departmentList);
            ms_type.setSelectedIndex(Integer.parseInt(user_account.getType()));
            if (user_account.getType().equals(Constants.USER_TYPE_HR)){
                companyIdInput.setVisibility(View.VISIBLE);
                companyIdEdit.setText(user_account.getCompId());
            }else if (user_account.getType().equals(Constants.USER_TYPE_DMS)){
                departSpinner.setVisibility(View.VISIBLE);
                departSpinner.setSelectedIndex(Integer.parseInt(user_account.getDepartmentId()));
            }
            else if (user_account.getType().equals(Constants.USER_TYPE_PROVIDER)){
                pharmacySpinner.setVisibility(View.VISIBLE);
                providerTypeSpinner.setVisibility(View.VISIBLE);
                companyIdInput.setVisibility(View.GONE);
                departSpinner.setVisibility(View.GONE);
                providerTypeSpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        getProidersTypes();
                    }
                });

                //complete provider
                getProidersTypes();
                getProvider();

                providerTypeSpinner.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {

                        selectedProviderType=Constants.providerTypesList.get(i).getId();
                      //  getProvidersBytype(Constants.providerTypesList.get(i).getId());

                    }

                });

                pharmacySpinner.getSpinner().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (providerTypeSpinner.getSpinner().getSelectedIndex() == 0) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.msg_sel_type, Snackbar.LENGTH_SHORT).show();
                        } else {
                            ProviderDailogFragment fragment=ProviderDailogFragment.newInstance(
                                    Constants.providerTypesList.
                                            get(providerTypeSpinner.getSpinner().getSelectedIndex()).getId());
                            fragment.setListner(AdminEditUserActivity.this);
                            fragment.show(getSupportFragmentManager(),"providerdialog");
                        }
                    }
                });


            }
            else {

                companyIdInput.setVisibility(View.GONE);
                departSpinner.setVisibility(View.GONE);

            }


            ms_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    //Toast.makeText(this,"position "+position,Toast.LENGTH_LONG).show();
                    if (position==1){
                        companyIdInput.setVisibility(View.VISIBLE);
                        departSpinner.setVisibility(View.GONE);
                        companyIdEdit.setText(user_account.getCompId());
                        pharmacySpinner.setVisibility(View.GONE);
                        providerTypeSpinner.setVisibility(View.GONE);
                    }
                    else if (position==2)
                    {
                        pharmacySpinner.setVisibility(View.VISIBLE);
                        providerTypeSpinner.setVisibility(View.VISIBLE);
                        departSpinner.setVisibility(View.GONE);
                        companyIdInput.setVisibility(View.GONE);
                        getProidersTypes();

                        providerTypeSpinner.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {

                                selectedProviderType=Constants.providerTypesList.get(i).getId();
                                //getProvidersBytype(Constants.providerTypesList.get(i).getId());

                            }

                        });

                        pharmacySpinner.getSpinner().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (providerTypeSpinner.getSpinner().getSelectedIndex() == 0) {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_sel_type, Snackbar.LENGTH_SHORT).show();
                                } else {
                                    ProviderDailogFragment fragment=ProviderDailogFragment.newInstance(  selectedProviderType);
                                    fragment.setListner(AdminEditUserActivity.this);
                                    fragment.show(getSupportFragmentManager(),"providerdialog");
                                }
                            }
                        });
                    }
                    else if (position==3){
                        pharmacySpinner.setVisibility(View.GONE);
                        providerTypeSpinner.setVisibility(View.GONE);
                        departSpinner.setVisibility(View.VISIBLE);
                        companyIdInput.setVisibility(View.GONE);
                        try{
                            departSpinner.setSelectedIndex(Integer.parseInt(user_account.getDepartmentId()));

                        }catch (Exception e){
                            departSpinner.setSelectedIndex(0);

                        }

                    }else {
                        companyIdInput.setVisibility(View.GONE);
                        departSpinner.setVisibility(View.GONE);
                        pharmacySpinner.setVisibility(View.GONE);
                        providerTypeSpinner.setVisibility(View.GONE);
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
        return R.layout.dialog_admin_edit_user;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @OnFocusChange({R.id.birthdate})
    void datePicker(boolean hasfocuse) {
        if (hasfocuse) {
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
                                et_birthdate.setText(newDate);
                                et_birthdate.setError(null);
                            } else if (nowyear == year) {
                                if (nowmonth > monthOfYear) {
                                    et_birthdate.setText(newDate);
                                    et_birthdate.setError(null);
                                } else if (nowmonth == monthOfYear) {
                                    if (nowday > dayOfMonth) {
                                        et_birthdate.setText(newDate);
                                        et_birthdate.setError(null);
                                    } else {
                                        et_birthdate.setError("Invalide Date");
                                    }
                                } else {
                                    et_birthdate.setError("Invalide Date");
                                }
                            } else {
                                et_birthdate.setError("Invalide Date");
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
    }


    @OnClick({R.id.ib_edit})
    void ButtonFinish(){

        String fullName=et_name.getText().toString().trim();
        String cardID=et_cardId.getText().toString().trim().isEmpty() || et_cardId.getText().toString().trim().equals("None")?"-1":et_cardId.getText().toString().trim();
        String username=usernameEdit.getText().toString();
        String nationalID=user_account.getEmpId();
        String mobileNumber=et_phone.getText().toString().trim();
        String email=et_email.getText().toString().trim();
        String password=et_password.getText().toString().trim();
        String version="m";
        String device="android";
        String compId="-1";
        if (ms_type.getSelectedIndex()==1){
            compId=companyIdEdit.getText().toString().trim();
        }else if (ms_type.getSelectedIndex()==3){
            compId="10000";
        }
        String departmentId=ms_type.getSelectedIndex()==3?departSpinner.getSelectedIndex()+"":"-1";
        String userType=ms_type.getSelectedIndex()+"";
        String birthDate=et_birthdate.getText().toString().trim();

        //region Check for a valid fullName.
        if (TextUtils.isEmpty(fullName)) {
            et_name.setError(getString(R.string.error_field_required));
            et_name.requestFocus();
            return;
        } else if (fullName.split(" ").length < 3) {
            et_name.setError(getString(R.string.error_invalid_fullname));
            et_name.requestFocus();
            return;
        }
        //region Check for a valid BirthDate.
        if (TextUtils.isEmpty(username)) {
            usernameEdit.setError(getString(R.string.error_field_required));
            usernameEdit.requestFocus();
            return;
        }
        //endregion

        //region Check for a valid card ID.
        /*if (TextUtils.isEmpty(cardID)) {
            et_cardId.setError(getString(R.string.error_field_required));
            et_cardId.requestFocus();
            return;
        }*/
        //endregion

        //region Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            et_email.setError(getString(R.string.error_field_required));
            et_email.requestFocus();
            return;
        } else if (!isEmailValid(email)) {
            et_email.setError(getString(R.string.error_invalid_email));
            et_email.requestFocus();
            return;
        }
        //endregion


        //region  Check for a valid mobile number.
        if (TextUtils.isEmpty(mobileNumber)) {
            et_phone.setError(getString(R.string.error_field_required));
            et_phone.requestFocus();
            return;
        } else if (!isMobileNumberValid(mobileNumber)) {
            et_phone.setError(getString(R.string.error_invalid_mobile));
            et_phone.requestFocus();
            return;
        }
        //endregion

        //region Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            et_password.setError(getString(R.string.error_field_required));
            et_password.requestFocus();
            return;
        } else {
            switch (meetLengthValidation(password)) {
                case 0:
                    et_password.setError(getString(R.string.error_invalid_short_password));
                    et_password.requestFocus();
                    return;
                case 1:
                    et_password.setError(getString(R.string.error_invalid_long_password));
                    et_password.requestFocus();
                    return;
            }
        }
        //endregion

        //region Check for a valid BirthDate.
        if (TextUtils.isEmpty(birthDate)) {
            et_birthdate.setError(getString(R.string.error_field_required));
            et_birthdate.requestFocus();
            return;
        }

        if (userType.equals(Constants.USER_TYPE_DMS)){
            if (departSpinner.getSelectedIndex()==0){
                Snackbar.make(findViewById(android.R.id.content),R.string.msg_select_department, Snackbar.LENGTH_SHORT).show();
                return;
            }
        }
        if (userType.equals(Constants.USER_TYPE_PROVIDER)){
            if (providerTypeSpinner.getSpinner().getSelectedIndex()==0){
                Snackbar.make(findViewById(android.R.id.content),"Select provider type ", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (selectedProviderId.equals("-1")){

                 Snackbar.make(findViewById(android.R.id.content),R.string.err_select_provider,Snackbar.LENGTH_SHORT).show();
                 return;
            }



        }
        LoadingView.setVisibility(View.VISIBLE);
        ib_edit.setVisibility(View.GONE);

        App.getInstance().getService().addAccount(user_account.getName(),username,fullName,cardID,nationalID,mobileNumber,email,password, LocaleHelper.getLanguage(this).equals("ar") ? Constants.Language.AR : Constants.Language.EN,version,device,birthDate.replaceAll("/", "-"), Constants.ACTIVE,userType,compId,departmentId,selectedProviderId,selectedProviderType,"1").enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                LoadingView.setVisibility(View.GONE);
                ib_edit.setVisibility(View.VISIBLE);
                if (response.body()!=null){
                    Message message=response.body().getMessage();
                    if (message.getCode()==1) {
                        isRefreshed=true;
                        Snackbar.make(findViewById(android.R.id.content),R.string.sucess_updated_account,Snackbar.LENGTH_LONG).show();

                    }else {
                        Snackbar.make(findViewById(android.R.id.content),message.getDetails(),Snackbar.LENGTH_LONG).show();

                    }
                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                LoadingView.setVisibility(View.GONE);
                ib_edit.setVisibility(View.VISIBLE);
                Snackbar.make(AdminEditUserActivity.this.findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();

            }
        });

    }


    private void getProidersTypes() {
         providerTypeSpinner.showLoading();
        App.getInstance().getService().getProviderTypes(Constants.Language.EN, Constants.KEY_SEARCH_ID).enqueue(new Callback<ResponseProviderTypes>() {
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
                        for (int i=0;i<Constants.providerTypesList.size();i++){
                            if (Constants.providerTypesList.get(i).getId().equals(user_account.getPrvType())){
                                providerTypeSpinner.getSpinner().setSelectedIndex(i);
                            }
                        }
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



    @Override
    public void OnPharmcyClicked(Pharmacy item) {
        selectedProviderId=item.getCode();
        pharmacySpinner.getSpinner().setText(item.getName());
    }
    @OnClick(R.id.imgv_close)
    void close(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra(EXTRA_LOADING,isRefreshed);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void getProvider() {
        pharmacySpinner.showLoading();

        App.getInstance().getService().getPharmacies(user_account.getPrvCode(),user_account.getPrvType(), "0").enqueue(new Callback<GetPharmaciesReponse>() {
            @Override
            public void onResponse(Call<GetPharmaciesReponse> call, Response<GetPharmaciesReponse> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().getCode() == 1) {
                        if (response.body().getList().isEmpty()) {
                               pharmacySpinner.showSucess();
                        } else {
                            pharmacySpinner.showSucess();
                            for (Pharmacy pharmacy:response.body().getList()){

                                if (pharmacy.getCode().equals(user_account.getPrvCode())){
                                    selectedProviderId=pharmacy.getCode();
                                    pharmacySpinner.getSpinner().setText(pharmacy.getName());
                                }
                            }

                        }
                    } else {
                        pharmacySpinner.showFailure();
                    }

                } else {

                    pharmacySpinner.showFailure();
                }

            }

            @Override
            public void onFailure(Call<GetPharmaciesReponse> call, Throwable t) {
                pharmacySpinner.showFailure();
            }
        });
    }



}
