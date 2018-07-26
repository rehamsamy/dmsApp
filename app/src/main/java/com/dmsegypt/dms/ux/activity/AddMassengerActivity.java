package com.dmsegypt.dms.ux.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMassengerActivity extends BaseActivity {

    @BindView(R.id.tinputAddress)
    TextInputLayout tinputAddress;

    @BindView(R.id.tinputFullName)
    TextInputLayout tinputFullName;

    @BindView(R.id.tinputFullNamearab)
    TextInputLayout tinputFullNamearab;

    @BindView(R.id.tinputNationalID)
    TextInputLayout tinputNationalID;

    @BindView(R.id.tinputBirthDate)
    TextInputLayout tinputBirthDate;

    @BindView(R.id.tinputUserName)
    TextInputLayout tinputUserName;

    @BindView(R.id.tinputPassword)
    TextInputLayout tinputPassword;

    @BindView(R.id.tinputLicence)
    TextInputLayout tinputLicence;




    @BindView(R.id.tvAddress)
    EditText etaddress;

    @BindView(R.id.etFullName)
    EditText etFullName;

    @BindView(R.id.etFullNamearab)
    EditText etFullNamearab;

    @BindView(R.id.etNationalID)
    EditText etNationalID;

    @BindView(R.id.etBirthDate)
    EditText etBirthDate;

    @BindView(R.id.etUserName)
    EditText etUserName;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etlicence)
    EditText etlicence;

    @BindView(R.id.typespinner)
    MaterialSpinner typespinner;

    @BindView(R.id.ownspinner)
    MaterialSpinner spinnermassengerown;

    @BindView(R.id.tr_licence)
    TableRow tr_licence;

    @BindView(R.id.btn_add)
    Button btnAdd;

    @BindArray(R.array.massengerown)
    String[]massengerown;

    @BindArray(R.array.massengertype)
    String[]massengertype;


    final int PLACE_PICKER_REQUEST=1;
    Call call;
    AnimUtils a;
    boolean havelicence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        spinnermassengerown.setItems(massengerown);
        typespinner.setItems(massengertype);
        spinnermassengerown.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
                if(i != 0 || i != 1){
                    tr_licence.setVisibility(View.VISIBLE);
                    havelicence=true;

                }else {
                    tr_licence.setVisibility(View.GONE);
                    havelicence=false;
                }
            }
        });
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_massenger;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


    @OnTouch(R.id.tvAddress)
    public boolean onAddressTouched(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (etaddress.getRight() -    etaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent=builder.build(AddMassengerActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        IntentManager.StartMassengerActivity(this);
    }

    @OnClick(R.id.iBtnCloseBtn)
    void close(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        }else {
            Intent intent = new Intent(this, MainMassengerActivity.class);
            ActivityOptions options=ActivityOptions.makeCustomAnimation(this,R.anim.slide_in_right,R.anim.slide_out_left);
            this.startActivity(intent,options.toBundle());
            finish();
        }
    }



    @OnTextChanged(R.id.etFullName)
    public void handleFullNameTextChange(Editable editable) {
        tinputFullName.setError(null);
    }

    @OnTextChanged(R.id.tvAddress)
    public void handleAddressTextChange(Editable editable){
        tinputAddress.setError(null);
    }

    @OnTextChanged(R.id.etNationalID)
    public void handleNationalIDTextChange(Editable editable) {
        tinputNationalID.setError(null);
    }

    //region onClick handle
    @OnClick({R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                AddMassenger();
                break;
        }

    }

    private void AddMassenger() {
        //region Store values at the time of add member attempt.
        String fullName = etFullName.getText().toString().trim();
        String fullNamearabic=etFullNamearab.getText().toString().trim();
        String nationalID = etNationalID.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String address=etaddress.getText().toString().trim();
        String username=etUserName.getText().toString().trim();
        String password=etPassword.getText().toString().trim();
        String licence=etlicence.getText().toString().trim();
        String CreatedBy=App.getInstance().getPrefManager().getCurrentUser().getCardId();
        String Massengerown= Constants.MassengerOwn.get(spinnermassengerown.getSelectedIndex()-1).toString();
        String Massengertype="2";

        if(Constants.MassengerType.get(typespinner.getSelectedIndex()-1).toString().equals("Technical Support")){
            Massengertype="1";

        }else if(Constants.MassengerType.get(typespinner.getSelectedIndex()-1).toString().equals("Customer Service")){

            Massengertype="0";
        }

        boolean cancel = false;
        View focusView = null;


        if(TextUtils.isEmpty(address)){
            tinputAddress.setError(getString(R.string.error_field_required));
            focusView=etaddress;
            cancel=true;
        }

        //region Check for a valid BirthDate.
        if (TextUtils.isEmpty(birthDate)) {
            tinputBirthDate.setError(getString(R.string.error_field_required));
            focusView = etBirthDate;
            cancel = true;
        }
        //endregion

        if(havelicence){
            if (TextUtils.isEmpty(licence)) {
                tinputLicence.setError(getString(R.string.error_field_required));
                focusView = etlicence;
                cancel = true;
            }
        }

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
        if (TextUtils.isEmpty(fullNamearabic)) {
            etFullNamearab.setError(getString(R.string.error_field_required));
            focusView = etFullNamearab;
            cancel = true;
        } else if (fullName.split(" ").length < 3) {
            etFullNamearab.setError(getString(R.string.error_invalid_fullname));
            focusView = etFullNamearab;
            cancel = true;
        }

        //endregion

        //region Check for a valid Password.
        if (TextUtils.isEmpty(password)) {
            tinputPassword.setError(getString(R.string.error_field_required));
            focusView = tinputPassword;
            cancel = true;
        }
        //endregion

        //region Check for a valid Username.
        if (TextUtils.isEmpty(username)) {
            tinputUserName.setError(getString(R.string.error_field_required));
            focusView = tinputUserName;
            cancel = true;
        }
        //endregion

        if(licence.equals("")){
            licence="0";
        }

        if (cancel) {
            // There was an error; don't attempt send data and focus the first
            // form field with an error.
            if (focusView == etBirthDate) datePicker();
            else focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform sne request attempt.
            DialogUtils.showDialog(AddMassengerActivity.this, true);
            call= App.getInstance().getService().AddRunner(fullNamearabic,fullName,birthDate.replaceAll("/","-"),nationalID,address,Massengerown,licence,Massengertype,
                    CreatedBy,getAppLanguage(),username,password);
            call.enqueue(new AddMassengerRequestCallBack(this));

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
                                    etBirthDate.setError(getString(R.string.invalid_date));
                                }
                            }else {
                                etBirthDate.setError(getString(R.string.invalid_date));
                            }
                        }else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();

        }
    }

    private static class AddMassengerRequestCallBack implements Callback<StatusResponse>{


        WeakReference<AppCompatActivity> activity;

        public AddMassengerRequestCallBack(AppCompatActivity activity) {
            this.activity =new WeakReference<AppCompatActivity>(activity);
        }

        @Override
        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
            AppCompatActivity context=activity.get();
            DialogUtils.showDialog(context,false);

            if (response != null && response.body() != null) {


                if (response.body().getCode() != null && response.body().getCode() == 1) {
                    Snackbar.make(context.findViewById(android.R.id.content), R.string.msg_request_sent, Snackbar.LENGTH_LONG).show();

                }

            }else {
                DialogUtils.showDialog(context,false);
                Snackbar.make(context.findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                        .show();

            }

        }

        @Override
        public void onFailure(Call<StatusResponse> call, Throwable throwable) {
            AppCompatActivity context=activity.get();
            DialogUtils.showDialog(context,false);
            Snackbar.make(context.findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                    .show();

        }
    }


}
