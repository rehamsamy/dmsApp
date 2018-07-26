package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.ClassCodeContract;
import com.dmsegypt.dms.rest.model.ContractDetail;
import com.dmsegypt.dms.rest.model.MedServiceContract;
import com.dmsegypt.dms.rest.model.Response.ContractDetailResponse;
import com.dmsegypt.dms.rest.model.Response.ContractResponse;
import com.dmsegypt.dms.rest.model.Response.SubServContractResponse;
import com.dmsegypt.dms.rest.model.SubServContract;
import com.dmsegypt.dms.ux.adapter.ContractServiceAdapter;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
import com.dmsegypt.dms.ux.custom_view.StateView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 16/03/2018.
 */

public class SummrayContractActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.contract_long_sp)
    MaterialSpinner longSpinner;
    @BindView(R.id.contract_type_sp)
     MaterialSpinner typeSpinner;
    @BindView(R.id.contract_num_sp)
    MaterialSpinner numSpinner;
    @BindView(R.id.search_button)
    ImageButton searchBtn;
    @BindView(R.id.company_name_tv)
    TextView comapnNameTv;
    @BindView(R.id.company_addres_tv)
    TextView AddressComapnyTv;
    @BindView(R.id.start_date_tv)
    TextView startDateTv;
    @BindView(R.id.end_date_tv)
    TextView endDateTv;
    @BindView(R.id.class_code_sp)
    MaterialSpinner ClassCodeSp;
    @BindView(R.id.ambulance_tv)
    TextView aacDegreeTv;
    @BindView(R.id.max_amount_tv)
    TextView maxAmountTv;
    @BindView(R.id.hospital_cost_tv)
    TextView hospitalDegreeTv;
    @BindView(R.id.serv_code_sp)
    MaterialSpinner ServiceSp;
    @BindView(R.id.sub_serv_code_sp)
    ProgressableSpinner subServiceSp;
    @BindView(R.id.state_view)
    StateView stateView;

    @BindView(R.id.contract_container)
    View ContractContiner;
    @BindView(R.id.detail_container)
    View DetailContainer;
    @BindArray(R.array.contract_long)
    String[]ContractLongList;
    @BindArray(R.array.contract_type)
    String[]ContractTypeList;
    ArrayList<String>contractNums;
    private String companyId;

    ArrayList<ClassCodeContract>classCodeList;
    ArrayList<MedServiceContract>medServiceContractList;
    ArrayList<SubServContract>subServContractList;
    private String contractno;

    @BindView(R.id.serv_code)
    TextView servCodeTv;
    @BindView(R.id.serv_name)
     TextView servNameTv;
    @BindView(R.id.serv_notes)
     TextView servNotesTv;
    @BindView(R.id.celling_amt)
     TextView cellingAmount;
    @BindView(R.id.cielling_pec)
     TextView ceillingPerct;
    @BindView(R.id.refund_flag)
     TextView refundFlagTv;
    @BindView(R.id.indlist_price)
     TextView indListPriceTv;
    @BindView(R.id.carry_amt)
     TextView carryAmountTv;
    @BindView(R.id.sub_serv_code)
    TextView subServCodeTv;
    @BindView(R.id.sub_serv_name)
    TextView subServNameTv;
    @BindView(R.id.sub_serv_notes)
    TextView subServNotesTv;
    @BindView(R.id.sub_celling_amt)
    TextView subCellingAmount;
    @BindView(R.id.sub_cielling_pec)
    TextView subCeillingPerct;
    @BindView(R.id.sub_refund_flag)
    TextView subRefundFlagTv;
    @BindView(R.id.sub_indlist_price)
    TextView subIndListPriceTv;
    @BindView(R.id.sub_carry_amt)
    TextView subCarryAmountTv;


    ContractServiceAdapter serviceAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        medServiceContractList=new ArrayList<>();
/*        servRecycleveiw.setLayoutManager(new LinearLayoutManager(this));
        servRecycleveiw.setHasFixedSize(true);
        serviceAdapter=new ContractServiceAdapter(medServiceContractList);
        servRecycleveiw.setAdapter(serviceAdapter);*/
        ContractContiner.setVisibility(View.GONE);
        DetailContainer.setVisibility(View.GONE);
        longSpinner.setItems(ContractLongList);
        typeSpinner.setItems(ContractTypeList);
        ClassCodeSp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                populateClassCodeSection(position);
            }
        });

        ServiceSp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (medServiceContractList!=null){
                    MedServiceContract medserv=medServiceContractList.get(position);
                    getSubServcieContract(medserv.getServiceId());
                    populateMedService(medserv);

                }
            }
        });
        subServiceSp.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (subServContractList!=null){
                    SubServContract subServContract=subServContractList.get(position);
                    populateSubService(subServContract);
                }
            }
        });
        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_HR)){
            companyId=App.getInstance().getPrefManager().getCurrentUser().getCompanyId();
           getCompanyContract(companyId);
        }

    }
    void populateClassCodeSection(int position){
        if (classCodeList!=null){
            ClassCodeContract classCode=classCodeList.get(position);
            aacDegreeTv.setText(classCode.getAccDegree());
            maxAmountTv.setText(classCode.getMaxAmount());
            hospitalDegreeTv.setText(classCode.getHospitalDegree());
            medServiceContractList=classCode.getMedServiceList();
            if (medServiceContractList.size()>0){
            populateMedService(medServiceContractList.get(0));
            ArrayList<String>items=new ArrayList<String>();
            for (MedServiceContract servContract:medServiceContractList)
            {items.add(servContract.getServiceId()+"|"+(servContract.getServiceName()!=null?servContract.getServiceName():""));
            }
            ServiceSp.setItems(items);
            getSubServcieContract(medServiceContractList.get(0).getServiceId());
        }}
    }
    void populateMedService(MedServiceContract service){
        servCodeTv.setText(service.getServiceId());
        servNameTv.setText(service.getServiceName());
        servNotesTv.setText(service.getNotes());
        cellingAmount.setText(service.getCeilingAmt());
        ceillingPerct.setText(service.getCeilingPert());
        refundFlagTv.setText(service.getRefundFlag());
        carryAmountTv.setText(service.getCarrAmt());
        indListPriceTv.setText(service.getIndListPrice());
    }


    void populateSubService(SubServContract service){
        subServCodeTv.setText(service.getSubServiceId());
        subServNameTv.setText(service.getSubServiceName());
        subServNotesTv.setText(service.getNotes());
        subCellingAmount.setText(service.getSubCeilingAmt());
        subCeillingPerct.setText(service.getSubCeilingPert());
        subRefundFlagTv.setText(service.getRefundFlag());
        subCarryAmountTv.setText(service.getCarrAmt());
        subIndListPriceTv.setText(service.getIndListPrice());
    }





    void getSubServcieContract(final String serv_code){
       subServiceSp.showLoading();
       subServiceSp.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
           @Override
           public void onRetry() {
               getSubServcieContract(serv_code);
           }
       });
        App.getInstance().getService().getSubSeviceContract(companyId,contractno+"",classCodeList.get(ClassCodeSp.getSelectedIndex()).getClassId()+"",serv_code,getAppLanguage()).enqueue(new Callback<SubServContractResponse>() {
            @Override
            public void onResponse(Call<SubServContractResponse> call, Response<SubServContractResponse> response) {


                if (response.body()!=null){
                    subServiceSp.showSucess();
                    ArrayList<String>items=new ArrayList<String>();
                    subServiceSp.getSpinner().setItems("");
                    subServContractList=response.body().getList();
                    for (SubServContract sub:subServContractList){
                        items.add(sub.getSubServiceId()+" | "+(sub.getSubServiceName()!=null?sub.getSubServiceName():""));
                    }
                    if (!items.isEmpty())
                    subServiceSp.getSpinner().setItems(items);
                    if(!subServContractList.isEmpty())
                    populateSubService(subServContractList.get(0));
                    else {
                        subServiceSp.getSpinner().setItems("");
                        subServiceSp.getSpinner().setText("Empty");
                    }
                }else {
                    subServiceSp.showFailure();


                }
            }

            @Override
            public void onFailure(Call<SubServContractResponse> call, Throwable t) {
                subServiceSp.showFailure();


            }
        });

    }










    void getCompanyContract(final String companyId){
        stateView.setVisibility(View.VISIBLE);
        stateView.showState(StateView.LOADING);
        stateView.setListener(new StateView.OnRetrListener() {
            @Override
            public void onRetry() {
                getCompanyContract(companyId);
            }
        });

        ContractContiner.setVisibility(View.GONE);
        DetailContainer.setVisibility(View.GONE);
        App.getInstance().getService().getCompanyContract(companyId,getAppLanguage()).enqueue(new Callback<ContractResponse>() {
            @Override
            public void onResponse(Call<ContractResponse> call, Response<ContractResponse> response) {
                if (response.body()!=null){
                    if (response.body().getMessage().getCode()==1){
                        ContractContiner.setVisibility(View.VISIBLE);
                        stateView.setVisibility(View.GONE);

                        if (!response.body().getList().isEmpty()){
                            contractNums=response.body().getList();
                            numSpinner.setItems("");

                            contractNums.add(0,"اختر رقم العقد");
                            numSpinner.setItems(contractNums);
                        }else {
                            numSpinner.setItems("");
                            numSpinner.setText("لايوجد عقود");

                        }

                    }else {
                        stateView.showState(StateView.NO_CONNECTION);


                    }

                }else {

                    stateView.showState(StateView.NO_CONNECTION);

                }
            }

            @Override
            public void onFailure(Call<ContractResponse> call, Throwable t) {
                stateView.showState(StateView.NO_CONNECTION);

            }
        });


    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.actiivity_contract;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_summray_contract;
    }



    @OnClick(R.id.search_button)
    void searchContractDetail(){


        getContractDetail();







    }


    public void getContractDetail(){
        if (numSpinner.getSelectedIndex()==0){
            Snackbar.make(findViewById(android.R.id.content)
                    ,R.string.err_select_contract_num,Snackbar.LENGTH_LONG).show();
            return;
        }
        if (typeSpinner.getSelectedIndex()==0){
            Snackbar.make(findViewById(android.R.id.content)
                    ,R.string.err_select_contract_type,Snackbar.LENGTH_LONG).show();
            return;
        }
        if (longSpinner.getSelectedIndex()==0){
            Snackbar.make(findViewById(android.R.id.content)
                    ,R.string.err_select_contract_long,Snackbar.LENGTH_LONG).show();
            return;
        }

        stateView.setVisibility(View.VISIBLE);
        stateView.showState(StateView.LOADING);
        stateView.setListener(new StateView.OnRetrListener() {
            @Override
            public void onRetry() {
getContractDetail();
            }
        });
        DetailContainer.setVisibility(View.GONE);
        contractno=contractNums.get(numSpinner.getSelectedIndex());
        App.getInstance().getService().getContractDetail(companyId,contractNums.get(numSpinner.getSelectedIndex())+"",longSpinner.getSelectedIndex()-1+"",typeSpinner.getSelectedIndex()-1+"",getAppLanguage()).enqueue(new Callback<ContractDetailResponse>() {
            @Override
            public void onResponse(Call<ContractDetailResponse> call, Response<ContractDetailResponse> response) {
                if (response.body()!=null){
                    if (response.body().getMessage().getCode()==1) {
                        stateView.setVisibility(View.GONE);
                        ContractDetail detail = response.body().getDetail();
                        if (detail!=null){
                            DetailContainer.setVisibility(View.VISIBLE);
                            comapnNameTv.setText(detail.getEnglishCompanyName());
                            AddressComapnyTv.setText(detail.getAddressCompany());
                            startDateTv.setText(detail.getStartDateContract());
                            endDateTv.setText(detail.getEndDateContract());
                            classCodeList=detail.getClassCodeList();
                            ArrayList<String>classString=new ArrayList<String>();
                            for (ClassCodeContract codeContract:classCodeList)
                            {classString.add(codeContract.getClassId()+"|"+(codeContract.getClassName()!=null?codeContract.getClassName():""));
                            }
                            ClassCodeSp.setItems(classString);
                            populateClassCodeSection(0);

                        }else {
                            stateView.showState(StateView.EMPTY);
                        }}
                }else {
                    stateView.showState(StateView.NO_CONNECTION);


                }
            }

            @Override
            public void onFailure(Call<ContractDetailResponse> call, Throwable t) {
                stateView.showState(StateView.NO_CONNECTION);

            }
        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_HR)){
            searchItem.setVisible(false);
        }
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setIconified(false);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setHint(R.string.hint_search_company);

        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                return true;
            }
        });

        searchView.setOnQueryTextListener(this);
        return true;


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        companyId=query;
        getCompanyContract(companyId);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }




}
