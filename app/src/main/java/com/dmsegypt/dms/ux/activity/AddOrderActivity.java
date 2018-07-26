package com.dmsegypt.dms.ux.activity;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Company;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseCities;
import com.dmsegypt.dms.rest.model.Response.ResponseCompany;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.rest.model.Response.ResponseStatus;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.CompanylistAdapter;
import com.dmsegypt.dms.ux.adapter.ReasonsAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.isMobileNumberValid;

public class AddOrderActivity extends BaseActivity {

    @BindView(R.id.table_searchforcompany)
    TableLayout table_searchforcompany;


    @BindView(R.id.table_new_order)
    TableLayout table_new_order;

    @BindView(R.id.etSearchText)
    EditText etSearchText;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    @BindView(R.id.tinputOrderdate)
    TextInputLayout tinputOrderdate;

    @BindView(R.id.tinputCompanyName)
    TextInputLayout tinputCompanyName;



    @BindView(R.id.etOrderDate)
    EditText etOrderDate;

    @BindView(R.id.etCompanyName)
    EditText etCompanyName;

    @BindView(R.id.tinputCompanyPhone1)
    TextInputLayout tinputCompanyPhone1;

    @BindView(R.id.etCompanyPhone1)
    EditText etCompanyPhone1;

    @BindView(R.id.tinputCompanyAddress1)
    TextInputLayout tinputCompanyAddress1;

    @BindView(R.id.etCompanyAddress1)
    EditText etCompanyAddress1;

    @BindView(R.id.back_btn)
    ImageView back_btn;

    @BindView(R.id.iBtnCloseBtn)
    ImageButton iBtnCloseBtn;

    @BindView(R.id.tinputCompanyPhone2)
    TextInputLayout tinputCompanyPhone2;

    @BindView(R.id.etCompanyPhone2)
    EditText etCompanyPhone2;

    @BindView(R.id.tinputCompanyAddress2)
    TextInputLayout tinputCompanyAddress2;

    @BindView(R.id.etCompanyAddress2)
    EditText etCompanyAddress2;

    @BindView(R.id.tinputCompanyId)
    TextInputLayout tinputCompanyId;

    @BindView(R.id.tv_list_reasons)
    TextView tv_list_reasons;

    @BindView(R.id.linear_back)
    LinearLayout linear_back;

    @BindView(R.id.etCompanyId)
    EditText etCompanyId;


    @BindView(R.id.tinputContactname)
    TextInputLayout tinputContactname;

    @BindView(R.id.etContactname)
    EditText etContactname;

    @BindView(R.id.reload_btn)
    TextView reload_btn;

    @BindView(R.id.aoCity)
    MaterialSpinner aoCity_spinner;

    @BindView(R.id.aoArea)
    MaterialSpinner aoArea_spinner;

    @BindView(R.id.reasonslist)
    RecyclerView reasonslist;

    @BindView(R.id.et_any_comment)
    EditText et_any_notes;

    @BindView(R.id.progressreasons)
    ProgressBar progressreasons;

    @BindView(R.id.progressarea)
    ProgressBar progressarea;


    @BindView(R.id.checkBoxvip)
    CheckBox checkBoxvip;

    @BindView(R.id.typespinner)
    MaterialSpinner typespinner;

    @BindView(R.id.mprogress)
    ProgressBar mprogress;

    @BindArray(R.array.massengertype)
    String[]massengertype;

    @BindView(R.id.btn_add)
    Button add_order_btn;


    Call call;
    CompanylistAdapter listAdapter;
    List<Company> list=new ArrayList<>();
    ReasonsAdapter listReasonsAdapter;
    static Company companyselected=new Company();
    ArrayList<String> selectedReasons = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        reasonslist.setLayoutManager(new LinearLayoutManager(this));
        typespinner.setItems(massengertype);
        add_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewOrder();
            }
        });
        //spinner Select city when City Selected call function get Area at position
        aoCity_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if (position !=0) {
                    progressarea.setVisibility(View.VISIBLE);
                    getArea(position);

                }
            }});
        reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListReasons();
            }
        });

        if(table_searchforcompany.getVisibility() == View.VISIBLE){
            linear_back.setVisibility(View.GONE);
        }else if(table_searchforcompany.getVisibility() == View.GONE) {
            linear_back.setVisibility(View.VISIBLE);
        }

        linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_new_order.setVisibility(View.GONE);
                table_searchforcompany.setVisibility(View.VISIBLE);
            }
        });

        iBtnCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentManager.StartMassengerActivity(AddOrderActivity.this);
            }
        });
    }

    @OnTouch(R.id.etSearchText)
    public boolean onAddressTouched(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (etSearchText.getRight() -    etSearchText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                try {
                    if(!etSearchText.getText().toString().trim().equals("")) {
                        getCompanyData(etSearchText.getText().toString().trim());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private void getCompanyData(String search) {
        DialogUtils.showDialog(AddOrderActivity.this, true);
        App.getInstance().getService().getCompanies(App.getInstance().getPrefManager().getUsername(),getAppLanguage(),search,"0").enqueue(new Callback<ResponseCompany>() {
            @Override
            public void onResponse(Call<ResponseCompany> call, final Response<ResponseCompany> response) {
                DialogUtils.showDialog(AddOrderActivity.this, false);
                if(response != null){
                    if(response.body().getMessage().getCode() ==1){
                        if(response.body().getList().size() != 0){
                            list=response.body().getList();
                            listAdapter=new CompanylistAdapter(AddOrderActivity.this,R.layout.item_company_detials,list);
                            recyclerview.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                            listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                                    table_searchforcompany.setVisibility(View.GONE);
                                    mprogress.setVisibility(View.VISIBLE);
                                    companyselected=response.body().getList().get(i);
                                    String company_name=companyselected.getComp_ename();
                                    String company_phone=companyselected.getComp_tel1();
                                    String company_address=companyselected.getComp_address1();
                                    String company_id=companyselected.getComp_id();
                                    String company_phone2=companyselected.getComp_tel2();
                                    String company_address2=companyselected.getComp_address2();
                                    StartAddOrderGetData(company_id,company_name,company_phone,company_address,company_phone2,company_address2);

                                }
                            });


                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseCompany> call, Throwable throwable) {
                DialogUtils.showDialog(AddOrderActivity.this, false);

            }
        });

    }

    private void StartAddOrderGetData(String company_id, String company_name, String company_phone, String company_address,String company_phone2,String company_address2) {
        getCities();
        etCompanyName.setText(company_name);
        etCompanyId.setText(company_id);
        etCompanyPhone1.setText(company_phone);
        etCompanyAddress1.setText(company_address);
        etCompanyPhone2.setText(company_phone2);
        etCompanyAddress2.setText(company_address2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();

        }
    }

    //region Retrive Data (get ProviderTypes/language/searchId
    void getCities(){
        App.getInstance().getService().getCities(getAppLanguage(),Constants.KEY_SEARCH_ID).enqueue(new Callback<ResponseCities>() {
            @Override
            public void onResponse(Call<ResponseCities> call, Response<ResponseCities> response) {
                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (Constants.providerCities == null)
                        Constants.providerCities = new ArrayList<City>();
                    if (message.getCode() != 1){

                        Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.msg_reolad, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getCities();
                                    }
                                }).setActionTextColor(Color.GREEN).show();

                    }
                    else {
                        linear_back.setVisibility(View.VISIBLE);
                        table_new_order.setVisibility(View.VISIBLE);
                        mprogress.setVisibility(View.GONE);
                        getListReasons();
                        Constants.providerCities = response.body().getList();
                        Constants.providerCities.set(0,new City("-1",getString(R.string.label_select_city)));

                        aoCity_spinner.setItems(Constants.providerCities);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCities> call, Throwable t) {
                Constants.providerCities = new ArrayList<City>();
            }
        });

    }



    //region datePicker to get date from pickerDialog
    @OnTouch({R.id.etOrderDate,R.id.tinputOrderdate})
    boolean datePicker(MotionEvent e) {
        if (e.getAction()==MotionEvent.ACTION_DOWN) {

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
                            if (nowyear < year) {
                                etOrderDate.setText(newDate);
                                etOrderDate.setError(null);
                            } else if (nowyear == year) {
                                if (nowmonth < monthOfYear) {
                                    etOrderDate.setText(newDate);
                                    etOrderDate.setError(null);
                                } else if (nowmonth == monthOfYear) {
                                    if (nowday < dayOfMonth) {
                                        etOrderDate.setText(newDate);
                                        etOrderDate.setError(null);
                                    } else {
                                        etOrderDate.setError(getString(R.string.invalid_date));
                                    }
                                } else {
                                    etOrderDate.setError(getString(R.string.invalid_date));
                                }
                            } else {
                                etOrderDate.setError(getString(R.string.invalid_date));
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
            return true;
        }
        return false;
    }

    //endregion

    private void getListReasons() {
        call=App.getInstance().getService().getReasonsList(getAppLanguage());
        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                if (response.body() != null) {
                    progressreasons.setVisibility(View.GONE);
                    if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {
                        listReasonsAdapter=new ReasonsAdapter(R.layout.item_reasons_list,response.body().getList());
                        reasonslist.setAdapter(listReasonsAdapter);
                    }
                }else {
                    reload_btn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable throwable) {
                progressreasons.setVisibility(View.GONE);
                reload_btn.setVisibility(View.VISIBLE);

            }
        });

    }
    //endregion


    //region get Areas of city by position (get City id)
    void getArea(final int pos){


        //Call function getAreas/CityId/language/key
        call=App.getInstance().getService().getAreas(Constants.providerCities.get(pos).getId(), getAppLanguage(), Constants.KEY_COMPLAINTS_ID);
        call.enqueue(new Callback<ResponseAreas>() {
            @Override
            public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                progressarea.setVisibility(View.GONE);
                aoArea_spinner.setVisibility(View.VISIBLE);
                if (response.body() != null) {

                    if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {
                        Constants.providerAreas = response.body().getList();
                        Constants.providerAreas.set(0,new AreaItem("-1",getString(R.string.label_select_area)));
                        aoArea_spinner.setItems(Constants.providerAreas);
                        aoArea_spinner.setEnabled(true);

                    }else {
                        aoArea_spinner.setItems("");
                        aoArea_spinner.setText(getString(R.string.area));
                        aoArea_spinner.setEnabled(false);

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

    @OnTextChanged(value = R.id.etCompanyName,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void OnCompanyNameTextChanged(){
        validateCompanyName();
    }
    boolean validateCompanyName() {
        String companyname = etCompanyName.getText().toString().trim();

        if (companyname.isEmpty()) {
            tinputCompanyName.setErrorEnabled(true);

            tinputCompanyName.setError(getString(R.string.err_empty_field));
            etCompanyName.requestFocus();
            return false;
        }else if(companyname.length()<3){

            tinputCompanyName.setErrorEnabled(true);

            tinputCompanyName.setError(getString(R.string.err_short_field));
            etCompanyName.requestFocus();
            return false;
        }

        else {
            tinputCompanyName.setErrorEnabled(false);
            tinputCompanyName.setError(null);

        }

        return true;
    }
    @OnTextChanged(value = R.id.etContactname,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void OnContactnameTextChanged(){
        validateContactname();
    }
    boolean validateContactname() {
        String Contactname = etContactname.getText().toString().trim();

        if (Contactname.isEmpty()) {
            tinputContactname.setErrorEnabled(true);

            tinputContactname.setError(getString(R.string.err_empty_field));
            etContactname.requestFocus();
            return false;
        }else if(Contactname.length()<3){

            tinputContactname.setErrorEnabled(true);

            tinputContactname.setError(getString(R.string.err_short_field));
            etContactname.requestFocus();
            return false;
        }

        else {
            tinputContactname.setErrorEnabled(false);
            tinputContactname.setError(null);

        }

        return true;
    }
    @OnTextChanged(value = R.id.etCompanyId,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void OnCompanyIdTextChanged(){
        validateCompanyId();
    }
    boolean validateCompanyId() {
        String companyId = etCompanyId.getText().toString().trim();

        if (companyId.isEmpty()) {
            tinputCompanyId.setErrorEnabled(true);

            tinputCompanyId.setError(getString(R.string.err_empty_field));
            etCompanyId.requestFocus();
            return false;
        }else if(companyId.length()<5){

            tinputCompanyId.setErrorEnabled(true);

            tinputCompanyId.setError(getString(R.string.err_short_field));
            etCompanyId.requestFocus();
            return false;
        }

        else {
            tinputCompanyId.setErrorEnabled(false);
            tinputCompanyId.setError(null);

        }

        return true;
    }
    @OnTextChanged(value = R.id.etCompanyAddress1,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void OnCompanyAddressTextChanged(){
        validateCompanyAddress();
    }
    boolean validateCompanyAddress() {
        String companyaddress = etCompanyAddress1.getText().toString().trim();

        if (companyaddress.isEmpty()) {
            tinputCompanyAddress1.setErrorEnabled(true);

            tinputCompanyAddress1.setError(getString(R.string.err_empty_field));
            etCompanyAddress1.requestFocus();
            return false;
        }else if(companyaddress.length()<5){

            tinputCompanyAddress1.setErrorEnabled(true);

            tinputCompanyAddress1.setError(getString(R.string.err_short_field));
            etCompanyAddress1.requestFocus();
            return false;
        }

        else {
            tinputCompanyAddress1.setErrorEnabled(false);
            tinputCompanyAddress1.setError(null);

        }

        return true;
    }
    boolean ValidateReasons(){
        if(listReasonsAdapter.getSelectedReasons().size() ==0){

            tv_list_reasons.setError(getString(R.string.err_empty_field));
            tv_list_reasons.requestFocus();
            return false;
        }else {
            tv_list_reasons.setError(null);
        }
        return true;
    }
    boolean validateProvCity(){
        if (Constants.providerCities==null||aoCity_spinner.getSelectedIndex()==0){
            aoCity_spinner.requestFocus();
            Snackbar.make(findViewById(android.R.id.content),R.string.select_city,Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
    boolean validateProvArea(){
        if (Constants.providerAreas==null||aoArea_spinner.getSelectedIndex()==0){
            aoArea_spinner.requestFocus();
            Snackbar.make(findViewById(android.R.id.content),R.string.select_area,Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
    boolean validateOrderDate(){
        if(etOrderDate.getText().toString().isEmpty()){
            tinputOrderdate.setErrorEnabled(true);
            tinputOrderdate.setError(getString(R.string.err_empty_field));
            etOrderDate.requestFocus();
            return false;
        }else {
            tinputOrderdate.setErrorEnabled(false);
            tinputOrderdate.setError(null);
        }
        return true;
    }

    boolean ValidateMobileNum(){
        String CompanyPhone = etCompanyPhone1.getText().toString().trim();

        if (TextUtils.isEmpty(CompanyPhone)) {
            tinputCompanyPhone1.setError(getString(R.string.error_field_required));
            etCompanyPhone1.requestFocus();
            return false;
        } else if (!isMobileNumberValid(CompanyPhone)) {
            tinputCompanyPhone1.setError(getString(R.string.error_invalid_mobile));
            etCompanyPhone1.requestFocus();
        }else {
            tinputCompanyPhone1.setError(null);
            tinputCompanyPhone1.setErrorEnabled(false);
        }
        return true;

    }

    private void addNewOrder() {
        if (!validateCompanyName())return;
        if (!validateContactname())return;
        if (!validateCompanyId())return;
        if (!validateCompanyAddress())return;
        if (!ValidateReasons())return;
        if (!validateProvCity())return;
        if (!validateProvArea())return;
        if (!ValidateMobileNum())return;
        if (!validateOrderDate())return;
        String massengertype="";
        if(Constants.MassengerType.get(typespinner.getSelectedIndex()).toString().equals("Technical Support")){
            massengertype="1";

        }else if(Constants.MassengerType.get(typespinner.getSelectedIndex()).toString().equals("Customer Service")){

            massengertype="0";
        }
        selectedReasons=listReasonsAdapter.getSelectedReasons();
        String order_notes=et_any_notes.getText().toString().trim();
        String comp_id=etCompanyId.getText().toString().trim();
        String comp_name=etCompanyName.getText().toString().trim();
        String comp_phone1;
        String comp_phone2;
        String comp_address1;
        String comp_address2;
        String comp_person=etContactname.getText().toString().trim();
        String comp_city=Constants.providerCities.get(aoCity_spinner.getSelectedIndex()).toString().trim();
        String comp_area=Constants.providerAreas.get(aoArea_spinner.getSelectedIndex()).toString().trim();
        String comp_reasons="";
        String order_date=etOrderDate.getText().toString().trim();
        String comp_cc="DEFULT";
        String isCheckVip;
        String CreatedBy=App.getInstance().getPrefManager().getCurrentUser().getCardId();

        if(selectedReasons.size()== 1){
            for (int i=0;i<selectedReasons.size();i++){
                comp_reasons+=selectedReasons.get(i).toString().trim();
            }
        }else {
            for (int i = 0; i < selectedReasons.size(); i++) {
                if(selectedReasons.size() == i+1) {
                    comp_reasons += selectedReasons.get(i).toString().trim();
                }else {
                    comp_reasons += selectedReasons.get(i).toString().trim() + "-";
                }
            }
        }
        if(etCompanyPhone1.getText().toString().trim().equals("")){
            comp_phone1=etCompanyPhone2.getText().toString().trim();
        }else {
            comp_phone1=etCompanyPhone1.getText().toString().trim();
        }
        if(etCompanyPhone2.getText().toString().trim().equals("")){
            comp_phone2=etCompanyPhone1.getText().toString().trim();
        }else {
            comp_phone2=etCompanyPhone2.getText().toString().trim();
        }

        if(checkBoxvip.isChecked()){
            isCheckVip="1";
        }else {
            isCheckVip="0";
        }
        if(etCompanyAddress1.getText().toString().trim().equals("")){
            comp_address1 =etCompanyAddress2.getText().toString().trim();
        }else {
            comp_address1=etCompanyAddress1.getText().toString().trim();
        }
        if(etCompanyAddress2.getText().toString().trim().equals("")){
            comp_address2=etCompanyAddress1.getText().toString().trim();
        }else {
            comp_address2=etCompanyAddress2.getText().toString().trim();
        }
        DialogUtils.showDialog(AddOrderActivity.this, true);
        call=App.getInstance().getService().addMassengerOrder(order_date.replaceAll("/","-"),massengertype,order_notes,comp_id,comp_name,
                comp_address1,comp_phone1,comp_phone2,comp_person,comp_city,comp_area,comp_cc,CreatedBy,getAppLanguage(),comp_reasons,isCheckVip);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                DialogUtils.showDialog(AddOrderActivity.this,false);
                if (response != null && response.body() != null) {
                    if (response.body().getCode() != null && response.body().getCode() == 1) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_request_sent, Snackbar.LENGTH_LONG).show();
                        IntentManager.StartMassengerActivity(AddOrderActivity.this);

                    }

                }else {
                    DialogUtils.showDialog(AddOrderActivity.this,false);
                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                DialogUtils.showDialog(AddOrderActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                        .show();

            }
        });


    }


    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_order;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


}
