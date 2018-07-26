package com.dmsegypt.dms.ux.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseCities;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.rest.model.Response.ResponseProviderTypes;
import com.dmsegypt.dms.utils.DialogUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 29/10/2017.
 */

public class AddProviderActivity extends BaseActivity {


    //region declare View
    @BindView(R.id.msArea)
    MaterialSpinner areaSpinner;
    @BindView(R.id.msCity)
    MaterialSpinner citySpinner;
    @BindView(R.id.msType)
    MaterialSpinner typeSpinner;
    @BindView(R.id.input_contact_name)
    TextInputLayout contactnameInput;
    @BindView(R.id.input_contact_phone)
    TextInputLayout contactPhoneInput;
    @BindView(R.id.input_prov_name)
    TextInputLayout provNameInput;
    @BindView(R.id.inputAddress)
    TextInputLayout addressinput;
    @BindView(R.id.et_contact_name)
    EditText contactnameEdit;
    @BindView(R.id.et_contact_phone)
    EditText contactPhoneEdit;
    @BindView(R.id.et_prov_name)
    EditText provNameEdit;
    @BindView(R.id.et_address)
    EditText addressEdit;

    @BindView(R.id.input_pop_count)
    TextInputLayout popinput;
    @BindView(R.id.et_pop_count)
    EditText popcountEdit;
    @BindView(R.id.Codelinear)
            View codeLayout;
    @BindView(R.id.codegenerated)
    TextView codeTv;

    //endregion


    //region Retrofit Declare
    Call call;

    //endregion


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
         retrieveData();


        //spinner Select city when City Selected call function get Area at position
        citySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if (position !=0) {
                    getArea(position);

            }
        }});
    }


    //region get Areas of city by position (get City id)
    void getArea(final int pos){

        //Dialog Unit Show
       DialogUtils.showDialog(this,true);

        //Call function getAreas/CityId/language/key
        call=App.getInstance().getService().getAreas(Constants.providerCities.get(pos).getId(), getAppLanguage(), Constants.KEY_COMPLAINTS_ID);
        call.enqueue(new Callback<ResponseAreas>() {
            @Override
            public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                DialogUtils.showDialog(AddProviderActivity.this,false);

                if (response.body() != null) {

                    if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {
                        Constants.providerAreas = response.body().getList();
                        Constants.providerAreas.set(0,new AreaItem("-1",getString(R.string.label_select_area)));
                        areaSpinner.setItems(Constants.providerAreas);
                        areaSpinner.setEnabled(true);

                    }else {
                        areaSpinner.setItems("");
                        areaSpinner.setText(getString(R.string.area));
                        areaSpinner.setEnabled(false);

                        Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getArea(pos);
                                    }
                                }).setActionTextColor(Color.GREEN).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseAreas> call, Throwable t) {
                DialogUtils.showDialog(AddProviderActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getArea(pos);
                            }
                        }).setActionTextColor(Color.GREEN).show();

            }
        });
    }
    //endregion







    //region Action Bar Declare

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_new_provider;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.add_new_provider;
    }

    //endregion



    //region Validation on Text Changed
    @OnTextChanged(value = R.id.et_pop_count,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onPopCountTextChnaged(){
        validatePopoultion();
    }
    boolean validatePopoultion(){
        String popcount=popcountEdit.getText().toString().trim();
        if (popcount.isEmpty()){

            popinput.setErrorEnabled(true);

            popinput.setError(getString(R.string.err_empty_field));
            popcountEdit.requestFocus();
            return false;

        }
        else {
            popinput.setErrorEnabled(false);
            popinput.setError(null);

        }

        return true;
    }




    @OnTextChanged(value = R.id.et_prov_name,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void OnProvNameTextChanged(){
           validateProvName();
    }
    boolean validateProvName() {
        String provname = provNameEdit.getText().toString().trim();

        if (provname.isEmpty()) {
            provNameInput.setErrorEnabled(true);

            provNameInput.setError(getString(R.string.err_empty_field));
            provNameEdit.requestFocus();
            return false;
        }else if(provname.length()<3){

            provNameInput.setErrorEnabled(true);

            provNameInput.setError(getString(R.string.err_short_field));
            provNameEdit.requestFocus();
            return false;
        }

        else {
            provNameInput.setErrorEnabled(false);
            provNameInput.setError(null);

        }

        return true;
    }
    @OnTextChanged(value = R.id.et_address,callback =OnTextChanged.Callback.TEXT_CHANGED )
    public void onProivderAddressTextChanged(){

        validateProvAddress();

    }




    private boolean validateProvAddress() {
        String address = addressEdit.getText().toString().trim();

        if (address.isEmpty()) {
            addressinput.setErrorEnabled(true);

            addressinput.setError(getString(R.string.err_empty_field));
            addressEdit.requestFocus();
            return false;
        }
        if (address.length()<3) {
            addressinput.setErrorEnabled(true);

            addressinput.setError(getString(R.string.err_short_field));
            addressEdit.requestFocus();
            return false;
        }


        else {
            addressinput.setErrorEnabled(false);
            addressinput.setError(null);

        }
        return true;

    }

@OnTextChanged(value = R.id.et_contact_name,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public  void OnContactNameTextChanged(){
    validateContactName();
}

    private boolean validateContactName() {
        String name = contactnameEdit.getText().toString().trim();

        if (name.isEmpty()) {
            contactnameInput.setErrorEnabled(true);

            contactnameInput.setError(getString(R.string.err_empty_field));
            contactnameEdit.requestFocus();
            return false;
        }else if (name.length()<3) {
            contactnameInput.setErrorEnabled(true);

            contactnameInput.setError(getString(R.string.err_short_field));
            contactnameEdit.requestFocus();
            return false;
        }
        else {
            contactnameInput.setErrorEnabled(false);
            contactnameInput.setError(null);

        }
        return true;
    }

    @OnTextChanged(value = R.id.et_contact_phone,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public  void OnContactPhoneTextChanged(){
        validateContactPhone();
    }

    private boolean validateContactPhone() {
        String email = contactPhoneEdit.getText().toString().trim();

        if (email.isEmpty()) {
            contactPhoneInput.setErrorEnabled(true);
            contactPhoneInput.setError(getString(R.string.err_empty_field));
            contactPhoneEdit.requestFocus();
            return false;
        }
        else if (email.length()>15) {
            contactPhoneInput.setErrorEnabled(true);
            contactPhoneInput.setError(getString(R.string.err_field_is_big));
            contactPhoneEdit.requestFocus();
            return false;
        }


        else {
            contactPhoneInput.setErrorEnabled(false);
            contactPhoneInput.setError(null);

        }
        return true;
    }



    boolean validateProvCity(){
        if (Constants.providerCities==null||citySpinner.getSelectedIndex()==0){
            citySpinner.requestFocus();
            Snackbar.make(findViewById(android.R.id.content),R.string.select_city,Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
    boolean validateProvArea(){
        if (Constants.providerAreas==null||areaSpinner.getSelectedIndex()==0){
            areaSpinner.requestFocus();
            Snackbar.make(findViewById(android.R.id.content),R.string.select_area,Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
    boolean validateProvType(){
        if (Constants.providerTypesList==null||typeSpinner.getSelectedIndex()==0){
            typeSpinner.requestFocus();
            Snackbar.make(findViewById(android.R.id.content),R.string.select_type,Snackbar.LENGTH_LONG).show();

            return false;
        }
        return true;

    }

    //endregion




    //region Onclick Add Provider
@OnClick(R.id.btn_add_provider)
    void addNewProvider(){
    if (!validateProvName())return;
    if (!validateProvAddress())return;
    if (!validateContactName())return;
    if (!validateContactPhone())return;
    if (!validateProvCity())return;
    if (!validateProvArea())return;
    if (!validateProvType())return;
    if (!validatePopoultion())return;

    String provider_name=provNameEdit.getText().toString().trim();
    String provider_address=addressEdit.getText().toString().trim();
    String contact_name=contactnameEdit.getText().toString().trim();
    String contact_phone=contactPhoneEdit.getText().toString().trim();
    String provider_city=Constants.providerCities.get(citySpinner.getSelectedIndex()).toString();
    String provider_area=Constants.providerAreas.get(areaSpinner.getSelectedIndex()).toString();
    String provider_type=typeSpinner.getSelectedIndex()+"";
    String pop_count=popcountEdit.getText().toString().trim();
    final MaterialDialog dialog = new MaterialDialog.Builder(this)
            .content(R.string.send_msg)
            .progress(true, 0)
            .cancelable(false)
            .show();
    call=App.getInstance().getService().addNewProvider(provider_area,provider_city,provider_type,provider_name
            ,provider_address,contact_name,contact_phone,"android",getAppLanguage(),pop_count);
    call.enqueue(new Callback<ResponseItem>() {
        @Override
        public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
            dialog.dismiss();
            if (response.body().getMessage().getCode()==1){
            Toast.makeText(AddProviderActivity.this,R.string.msg_send_proivder_request, Toast.LENGTH_SHORT).show();
                codeLayout.setVisibility(View.VISIBLE);
                codeTv.setText(response.body().getMessage().getUserid());
            }else {
                Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseItem> call, Throwable t) {
            dialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();
            //Snackbar.make(findViewById(android.R.id.content),t.getMessage().toString(),Snackbar.LENGTH_LONG).show();
        }
    });

}


//endregion











    //region Retrive Data (get ProviderTypes/language/searchId
    private void retrieveData() {


       DialogUtils.showDialog(this,true);

       call= App.getInstance().getService().getProviderTypes(getAppLanguage(), Constants.KEY_SEARCH_ID);
        call.enqueue(new Callback<ResponseProviderTypes>() {
            @Override
            public void onResponse(Call<ResponseProviderTypes> call, Response<ResponseProviderTypes> response) {
                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (Constants.providerTypesList == null)
                        Constants.providerTypesList = new ArrayList<ProviderType>();
                    // if there is no Date
                    if (message.getCode() != 1) {

                        Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        retrieveData();
                                    }
                                }).setActionTextColor(Color.GREEN).show();
                    }else {
                        // if return data call getCities function
                        Constants.providerTypesList = response.body().getList();
                        Constants.providerTypesList.set(0,new ProviderType("-1",getString(R.string.label_select_type)));

                        typeSpinner.setItems(Constants.providerTypesList);
                        //getCities/language/SearchKey
                        App.getInstance().getService().getCities(getAppLanguage(), Constants.KEY_SEARCH_ID).enqueue(new Callback<ResponseCities>() {
                            @Override
                            public void onResponse(Call<ResponseCities> call, Response<ResponseCities> response) {
                                DialogUtils.showDialog(AddProviderActivity.this,false);
                                if (response.body() != null) {
                                    Message message = response.body().getMessage();
                                    if (Constants.providerCities == null)
                                        Constants.providerCities = new ArrayList<City>();
                                    if (message.getCode() != 1){
                                        Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        retrieveData();
                                                    }
                                                }).setActionTextColor(Color.GREEN).show();

                                    }
                                    else {
                                        Constants.providerCities = response.body().getList();
                                        Constants.providerCities.set(0,new City("-1",getString(R.string.label_select_city)));

                                        citySpinner.setItems(Constants.providerCities);

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseCities> call, Throwable t) {
                                Constants.providerCities = new ArrayList<City>();
                                DialogUtils.showDialog(AddProviderActivity.this,false);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProviderTypes> call, Throwable t) {
                Constants.providerTypesList = new ArrayList<ProviderType>();
                DialogUtils.showDialog(AddProviderActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                retrieveData();
                            }
                        }).setActionTextColor(Color.GREEN).show();
            }
        });
    }


    //endregion



    //distroy CallBack to avoid leak Memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
