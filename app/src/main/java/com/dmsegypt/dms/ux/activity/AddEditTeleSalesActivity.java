package com.dmsegypt.dms.ux.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.MedNetworkItem;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.MedNetworkResponse;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.SalesReply;
import com.dmsegypt.dms.rest.model.Response.SalesReplyResponse;
import com.dmsegypt.dms.rest.model.SalesCall;
import com.dmsegypt.dms.rest.model.Salesitem;
import com.dmsegypt.dms.rest.model.TeleContractDetail;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.utils;
import com.dmsegypt.dms.ux.custom_view.DotLoadingView;
import com.dmsegypt.dms.ux.dialogs.AreaFragmentDialog;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditTeleSalesActivity extends BaseActivity implements AreaFragmentDialog.OnAreaClickListner {

    List<String> medNetworkItems = new ArrayList<>();

    List<String> accomodationItems = new ArrayList<>();

    /*Start table */

    /*start annual ccelling*/
    @BindView(R.id.annualCellingFirst)
    EditText annualCellingFirst;

    @BindView(R.id.annualCellingSecond)
    EditText annualCellingSecond;

    @BindView(R.id.annualCellingThird)
    EditText annualCellingThird;

    @BindView(R.id.annualCellingFourth)
    EditText annualCellingFourth;
    /*end annual ccelling*/


    /*start num Of Subscribers*/
    @BindView(R.id.numOfSubscribersFirst)
    EditText numOfSubscribersFirst;

    @BindView(R.id.numOfSubscribersSecond)
    EditText numOfSubscribersSecond;

    @BindView(R.id.numOfSubscribersThird)
    EditText numOfSubscribersThird;

    @BindView(R.id.numOfSubscribersFourth)
    EditText numOfSubscribersFourth;
    /*end num Of Subscribers*/


    /*start medical network*/
    @BindView(R.id.medicalNetworkFirst)
    MaterialSpinner medicalNetworkFirst;
    @BindView(R.id.medicalNetworkFirst_progress)
    DotLoadingView medicalNetworkFirst_progress;
    @BindView(R.id.medicalNetworkFirst_retry)
    ImageView medicalNetworkFirst_retry;


    @BindView(R.id.medicalNetworkSecond)
    MaterialSpinner medicalNetworkSecond;
    @BindView(R.id.medicalNetworkSecond_progress)
    DotLoadingView medicalNetworkSecond_progress;
    @BindView(R.id.medicalNetworkSecond_retry)
    ImageView medicalNetworkSecond_retry;


    @BindView(R.id.medicalNetworkThird)
    MaterialSpinner medicalNetworkThird;
    @BindView(R.id.medicalNetworkThird_progress)
    DotLoadingView medicalNetworkThird_progress;
    @BindView(R.id.medicalNetworkThird_retry)
    ImageView medicalNetworkThird_retry;


    @BindView(R.id.medicalNetworkFourth)
    MaterialSpinner medicalNetworkFourth;
    @BindView(R.id.medicalNetworkFourth_progress)
    DotLoadingView medicalNetworkFourth_progress;
    @BindView(R.id.medicalNetworkFourth_retry)
    ImageView medicalNetworkFourth_retry;

    /*end medical network*/

    /*start accommodation*/
    @BindView(R.id.accomodationFirst)
    MaterialSpinner accomodationFirst;
    @BindView(R.id.accomodationFirst_progress)
    DotLoadingView accomodationFirst_progress;
    @BindView(R.id.accomodationFirst_retry)
    ImageView accomodationFirst_retry;


    @BindView(R.id.accomodationSecond)
    MaterialSpinner accomodationSecond;
    @BindView(R.id.accomodationSecond_progress)
    DotLoadingView accomodationSecond_progress;
    @BindView(R.id.accomodationSecond_retry)
    ImageView accomodationSecond_retry;


    @BindView(R.id.accomodationThird)
    MaterialSpinner accomodationThird;
    @BindView(R.id.accomodationThird_progress)
    DotLoadingView accomodationThird_progress;
    @BindView(R.id.accomodationThird_retry)
    ImageView accomodationThird_retry;


    @BindView(R.id.accomodationFourth)
    MaterialSpinner accomodationFourth;
    @BindView(R.id.accomodationFourth_progress)
    DotLoadingView accomodationFourth_progress;
    @BindView(R.id.accomodationFourth_retry)
    ImageView accomodationFourth_retry;
    /*end accommodation*/


    /*start inpatient services*/
    @BindView(R.id.inpatientServiceFirst)
    EditText inpatientServiceFirst;

    @BindView(R.id.inpatientServiceSecond)
    EditText inpatientServiceSecond;

    @BindView(R.id.inpatientServiceThird)
    EditText inpatientServiceThird;

    @BindView(R.id.inpatientServiceFourth)
    EditText inpatientServiceFourth;

    /*end inpatient services*/


    /*start consultation*/
    @BindView(R.id.consultationFirst)
    EditText consultationFirst;

    @BindView(R.id.consultationSecond)
    EditText consultationSecond;

    @BindView(R.id.consultationThird)
    EditText consultationThird;

    @BindView(R.id.consultationFourth)
    EditText consultationFourth;

    /*end consultation*/


    /*start investigation*/
    @BindView(R.id.investigationFirst)
    EditText investigationFirst;

    @BindView(R.id.investigationSecond)
    EditText investigationSecond;

    @BindView(R.id.investigationThird)
    EditText investigationThird;

    @BindView(R.id.investigationFourth)
    EditText investigationFourth;

    /*end investigation*/


    /*start physiotherapy*/
    @BindView(R.id.physiotherapyFirst)
    EditText physiotherapyFirst;

    @BindView(R.id.physiotherapySecond)
    EditText physiotherapySecond;

    @BindView(R.id.physiotherapyThird)
    EditText physiotherapyThird;

    @BindView(R.id.physiotherapyFourth)
    EditText physiotherapyFourth;

    /*end physiotherapy*/

    /*start outpatient medication*/
    @BindView(R.id.outPatientFirst)
    EditText outPatientFirst;

    @BindView(R.id.outPatientSecond)
    EditText outPatientSecond;

    @BindView(R.id.outPatientThird)
    EditText outPatientThird;

    @BindView(R.id.outPatientFourth)
    EditText outPatientFourth;

    /*end outpatient medication*/


    /*start normal delivery*/
    @BindView(R.id.normalDeliveryFirst)
    EditText normalDeliveryFirst;

    @BindView(R.id.normalDeliverySecond)
    EditText normalDeliverySecond;

    @BindView(R.id.normalDeliveryThird)
    EditText normalDeliveryThird;

    @BindView(R.id.normalDeliveryFourth)
    EditText normalDeliveryFourth;

    /*end normal delivery*/


    /*start c section*/
    @BindView(R.id.cSectionFirst)
    EditText cSectionFirst;

    @BindView(R.id.cSectionSecond)
    EditText cSectionSecond;

    @BindView(R.id.cSectionThird)
    EditText cSectionThird;

    @BindView(R.id.cSectionFourth)
    EditText cSectionFourth;

    /*end c section*/

    /*start legal abortion*/
    @BindView(R.id.legalAbortionFirst)
    EditText legalAbortionFirst;

    @BindView(R.id.legalAbortionSecond)
    EditText legalAbortionSecond;

    @BindView(R.id.legalAbortionThird)
    EditText legalAbortionThird;

    @BindView(R.id.legalAbortionFourth)
    EditText legalAbortionFourth;

    /*end legal abortion*/


    /*start dental basic*/
    @BindView(R.id.dentalBasicFirst)
    EditText dentalBasicFirst;

    @BindView(R.id.dentalBasicSecond)
    EditText dentalBasicSecond;

    @BindView(R.id.dentalBasicThird)
    EditText dentalBasicThird;

    @BindView(R.id.dentalBasicFourth)
    EditText dentalBasicFourth;

    /*end dental basic*/


    /*start root canal*/
    @BindView(R.id.rootCanalFirst)
    EditText rootCanalFirst;

    @BindView(R.id.rootCanalSecond)
    EditText rootCanalSecond;

    @BindView(R.id.rootCanalThird)
    EditText rootCanalThird;

    @BindView(R.id.rootCanalFourth)
    EditText rootCanalFourth;

    /*end root canal*/


    /*start crowns*/
    @BindView(R.id.dentalCrownsFirst)
    EditText dentalCrownsFirst;

    @BindView(R.id.dentalCrownsSecond)
    EditText dentalCrownsSecond;

    @BindView(R.id.dentalCrownsThird)
    EditText dentalCrownsThird;

    @BindView(R.id.dentalCrownsFourth)
    EditText dentalCrownsFourth;

    /*end crowns*/


    /*start optical eye*/
    @BindView(R.id.opticalEyeFirst)
    EditText opticalEyeFirst;

    @BindView(R.id.opticalEyeSecond)
    EditText opticalEyeSecond;

    @BindView(R.id.opticalEyeThird)
    EditText opticalEyeThird;

    @BindView(R.id.opticalEyeFourth)
    EditText opticalEyeFourth;

    /*end optical eye*/



    /*start pre exisiting*/

    @BindView(R.id.preExistingFirst)
    EditText preExistingFirst;

    @BindView(R.id.preExistingSecond)
    EditText preExistingSecond;

    @BindView(R.id.preExistingThird)
    EditText preExistingThird;

    @BindView(R.id.preExistingFourth)
    EditText preExistingFourth;

    /*end pre exisiting*/

    /*start critical cases*/

    @BindView(R.id.criticalCasesFirst)
    EditText criticalCasesFirst;

    @BindView(R.id.criticalCasesSecond)
    EditText criticalCasesSecond;

    @BindView(R.id.criticalCasesThird)
    EditText criticalCasesThird;

    @BindView(R.id.criticalCasesFourth)
    EditText criticalCasesFourth;

    /*end critical cases*/


    /*End table*/
    public static final String EXTRA_OBJ = "extra_obj";
    @BindView(R.id.operationNumber)
    EditText opeartionNumberEdit;

    @BindView(R.id.personCheckBox)
    CheckBox personCheckBox;

    @BindView(R.id.companyCheckBox)
    CheckBox companyCheckBox;


    @BindView(R.id.companyName)
    EditText companyName;


    @BindView(R.id.arabicName)
    EditText arabicNameEdit;

    @BindView(R.id.engName)
    EditText engNameEdit;

    @BindView(R.id.customerEmail)
    EditText customerEmailEdit;


    @BindView(R.id.faxNumber)
    EditText faxNumberEdit;

    @BindView(R.id.mobileNumber)
    EditText mobileNumberEdit;

    @BindView(R.id.telephoneNumber)
    EditText telephoneNumberEdit;

    @BindView(R.id.areaSpinner)
    MaterialSpinner areaSpinner;

    @BindView(R.id.contractPerson)
    EditText contractPersonEdit;


    @BindView(R.id.contractEmail)
    EditText contractEmailEdit;

    @BindView(R.id.contractMobile)
    EditText contractMobileEdit;

    /**************************/
    @BindView(R.id.calldateEdit)
    EditText calDateEdit;
    @BindView(R.id.nextcalldateEdit)
    EditText nextCallDateEdit;
    @BindView(R.id.meetdateEdit)
    EditText meetingCallDateEdit;
    @BindView(R.id.contractEndDateEditText)
    EditText contractEndDateEditText;
    /**********************************/
    @BindView(R.id.replySpinner)
    MaterialSpinner replySpinner;


    //replyNoteEdit
    @BindView(R.id.responseDescription)
    EditText replyNoteEdit;

    @BindView(R.id.noneInterestReplySpinner)
    MaterialSpinner noneInterestReplySpinner;

    @BindView(R.id.notes)
    EditText nonintReplyEdit;


    @BindView(R.id.addSalesBtn)
    Button addSalesBtn;
    @BindView(R.id.reply_progress)
    DotLoadingView replyProgress;
    @BindView(R.id.nonereply_progress)
    DotLoadingView nonereplyProgress;
    @BindView(R.id.reply_retry)
    ImageView replyretryImgv;
    @BindView(R.id.nonereply_retry)
    ImageView nonereplyretryImgv;

    @BindView(R.id.area_progress)
    DotLoadingView areaprogress;
    @BindView(R.id.area_retry)
    ImageView areaRetryImgv;

    /**********************************/
    @BindView(R.id.arrowDown)
    ImageView arrowDown;

    @BindView(R.id.arrowUp)
    ImageView arrowUp;

    @BindView(R.id.tableViewContainer)
    ScrollView tableViewContainer;


    /************************************/
    private AreaItem area;
    private MedNetworkItem medNetworkItem;
    private ArrayList<SalesReply> nonReplyList;
    private ArrayList<SalesReply> ReplyList;
    Salesitem salesitem;
    private int replyindex = 0;
    private int noereplyindex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        salesitem = getIntent().getParcelableExtra(EXTRA_OBJ);
        if (salesitem != null) {
            opeartionNumberEdit.setText(salesitem.getId());
            companyName.setText(salesitem.getCompName());
            arabicNameEdit.setText(salesitem.getAgentAname());
            engNameEdit.setText(salesitem.getAgentEname());
            telephoneNumberEdit.setText(salesitem.getTeleNo());
            faxNumberEdit.setText(salesitem.getFaxNo());
            mobileNumberEdit.setText(salesitem.getMobile());
            contractEmailEdit.setText(salesitem.getContactMail());
            contractMobileEdit.setText(salesitem.getContacatTele());
            contractPersonEdit.setText(salesitem.getContactName());

            calDateEdit.setText(salesitem.getCallDate().split(" ")[0] + "&" + salesitem.getCallTime());
            nextCallDateEdit.setText(salesitem.getNextCallDate().split(" ")[0] + "&" + salesitem.getNextDateTime());
            meetingCallDateEdit.setText(salesitem.getMeetingDate().split(" ")[0] + "&" + salesitem.getMeetingTime());
            contractEndDateEditText.setText(salesitem.getEndDateContract().split(" ")[0]);

            if (salesitem.equals("COMP")) {
                companyCheckBox.setChecked(true);
            } else {
                personCheckBox.setChecked(true);
            }
            customerEmailEdit.setText(salesitem.getMail());
            replyNoteEdit.setText(salesitem.getReplyNotes() == null ? "" : salesitem.getReplyNotes());
            nonintReplyEdit.setText(salesitem.getNotInterestReply() == null ? "" : salesitem.getNotInterestReply());
            getAreas();

            if (salesitem.getContractDetail() != null) {
                if (salesitem.getContractDetail().size() > 0) {
                    for (TeleContractDetail item : salesitem.getContractDetail()) {
                        initializeContarctCategory(item);
                    }
                }

            }


        }
        getReplyList();
        getNoneInterstReplyList();


    }

    @OnClick(R.id.arrowDown)
    void setArrowDown() {
        arrowDown.setVisibility(View.GONE);
        arrowUp.setVisibility(View.VISIBLE);
        tableViewContainer.setVisibility(View.VISIBLE);
        getMedicalNetworkList();
        getAccomodationList();

    }


    @OnClick(R.id.arrowUp)
    void setArrowUp() {
        arrowDown.setVisibility(View.VISIBLE);
        arrowUp.setVisibility(View.GONE);
        tableViewContainer.setVisibility(View.GONE);

    }


    void getMedicalNetworkList() {
        medicalNetworkFirst_progress.setVisibility(View.VISIBLE);
        medicalNetworkFirst_retry.setVisibility(View.GONE);

        medicalNetworkSecond_progress.setVisibility(View.VISIBLE);
        medicalNetworkSecond_retry.setVisibility(View.GONE);

        medicalNetworkThird_progress.setVisibility(View.VISIBLE);
        medicalNetworkThird_retry.setVisibility(View.GONE);

        medicalNetworkFourth_progress.setVisibility(View.VISIBLE);
        medicalNetworkFourth_retry.setVisibility(View.GONE);

        App.getInstance().getService().getMedNetworkList().enqueue(new Callback<MedNetworkResponse>() {
            @Override
            public void onResponse(Call<MedNetworkResponse> call, Response<MedNetworkResponse> response) {
                medicalNetworkFirst_progress.setVisibility(View.GONE);
                medicalNetworkSecond_progress.setVisibility(View.GONE);
                medicalNetworkThird_progress.setVisibility(View.GONE);
                medicalNetworkFourth_progress.setVisibility(View.GONE);

                medNetworkItems.clear();
                if (!response.body().getList().isEmpty()) {
                    for (MedNetworkItem item : response.body().getList()) {
                        medNetworkItems.add(item.getName());
                    }
                    medicalNetworkFirst.setItems(medNetworkItems);
                    medicalNetworkSecond.setItems(medNetworkItems);
                    medicalNetworkThird.setItems(medNetworkItems);
                    medicalNetworkFourth.setItems(medNetworkItems);

                } else {
                    medicalNetworkFirst_retry.setVisibility(View.VISIBLE);
                    medicalNetworkSecond_retry.setVisibility(View.VISIBLE);
                    medicalNetworkThird_retry.setVisibility(View.VISIBLE);
                    medicalNetworkFourth_retry.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<MedNetworkResponse> call, Throwable throwable) {
                medicalNetworkFirst_progress.setVisibility(View.GONE);
                medicalNetworkSecond_progress.setVisibility(View.GONE);
                medicalNetworkThird_progress.setVisibility(View.GONE);
                medicalNetworkFourth_progress.setVisibility(View.GONE);

                medicalNetworkFirst_retry.setVisibility(View.VISIBLE);
                medicalNetworkSecond_retry.setVisibility(View.VISIBLE);
                medicalNetworkThird_retry.setVisibility(View.VISIBLE);
                medicalNetworkFourth_retry.setVisibility(View.VISIBLE);
            }
        });


    }


    void getAccomodationList() {
        accomodationFirst_progress.setVisibility(View.VISIBLE);
        accomodationFirst_retry.setVisibility(View.GONE);

        accomodationSecond_progress.setVisibility(View.VISIBLE);
        accomodationSecond_retry.setVisibility(View.GONE);

        accomodationThird_progress.setVisibility(View.VISIBLE);
        accomodationThird_retry.setVisibility(View.GONE);

        accomodationFourth_progress.setVisibility(View.VISIBLE);
        accomodationFourth_retry.setVisibility(View.GONE);

        App.getInstance().getService().getAccommodationList().enqueue(new Callback<MedNetworkResponse>() {
            @Override
            public void onResponse(Call<MedNetworkResponse> call, Response<MedNetworkResponse> response) {
                accomodationFirst_progress.setVisibility(View.GONE);
                accomodationSecond_progress.setVisibility(View.GONE);
                accomodationThird_progress.setVisibility(View.GONE);
                accomodationFourth_progress.setVisibility(View.GONE);


                if (!response.body().getList().isEmpty()) {

                    accomodationItems.clear();
                    for (MedNetworkItem item : response.body().getList()) {
                        accomodationItems.add(item.getName());
                    }
                    accomodationFirst.setItems(accomodationItems);
                    accomodationSecond.setItems(accomodationItems);
                    accomodationThird.setItems(accomodationItems);
                    accomodationFourth.setItems(accomodationItems);

                } else {
                    accomodationFirst_retry.setVisibility(View.VISIBLE);
                    accomodationSecond_retry.setVisibility(View.VISIBLE);
                    accomodationThird_retry.setVisibility(View.VISIBLE);
                    accomodationFourth_retry.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<MedNetworkResponse> call, Throwable throwable) {
                accomodationFirst_progress.setVisibility(View.GONE);
                accomodationSecond_progress.setVisibility(View.GONE);
                accomodationThird_progress.setVisibility(View.GONE);
                accomodationFourth_progress.setVisibility(View.GONE);

                accomodationFirst_retry.setVisibility(View.VISIBLE);
                accomodationSecond_retry.setVisibility(View.VISIBLE);
                accomodationThird_retry.setVisibility(View.VISIBLE);
                accomodationFourth_retry.setVisibility(View.VISIBLE);
            }
        });


    }

    void getAreas() {
        areaprogress.setVisibility(View.VISIBLE);
        areaRetryImgv.setVisibility(View.GONE);
        App.getInstance().getService().getAreas("-1", "En", 0).enqueue(new Callback<ResponseAreas>() {
            @Override
            public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                areaprogress.setVisibility(View.GONE);
                List<AreaItem> list = response.body().getList();
                if (!list.isEmpty()) {
                    for (AreaItem item : list) {
                        if (item.getId().equals(salesitem.getAreaCode())) {
                            areaSpinner.setText(item.getName().toString());
                            area = item;
                        }
                    }
                } else {
                    areaRetryImgv.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ResponseAreas> call, Throwable t) {
                areaprogress.setVisibility(View.GONE);
                areaRetryImgv.setVisibility(View.VISIBLE);
            }
        });
    }

    @NonNull
    @OnClick(R.id.areaSpinner)
    void showAreaDialog() {
        AreaFragmentDialog dialog = new AreaFragmentDialog();
        dialog.setListner(this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_edit_tele_sales;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.add_tele_sales;
    }

    @OnClick(R.id.personCheckBox)
    void setPersonCheckBox() {

        if (personCheckBox.isChecked()) {
            personCheckBox.setChecked(true);
            companyCheckBox.setChecked(false);

        } else if (!personCheckBox.isChecked()) {

            personCheckBox.setChecked(false);
        }

    }

    @OnClick(R.id.companyCheckBox)
    void setcompanyCheckBox() {

        if (companyCheckBox.isChecked()) {
            companyCheckBox.setChecked(true);
            personCheckBox.setChecked(false);

        } else if (!companyCheckBox.isChecked()) {

            companyCheckBox.setChecked(false);
        }
    }


    @OnClick(R.id.calldate_btn)
    void editCallDateBtn() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final String newDate = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                        TimePickerDialog tdp = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int second) {
                                String time = ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                                calDateEdit.setText(newDate + "&" + time);

                            }
                        }, 12, 0, 0, false);
                        tdp.enableSeconds(false);
                        tdp.setTitle(getString(R.string.time_pick));
                        tdp.show(getFragmentManager(), "datePickerDialog");


                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "timePickerDialog");
    }


    @OnClick(R.id.nextcalldate_btn)
    void nextCallDateBtn() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final String newDate = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                        TimePickerDialog tdp = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int second) {
                                String time = ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                                nextCallDateEdit.setText(newDate + "&" + time);

                            }
                        }, 12, 0, 0, false);
                        tdp.enableSeconds(false);
                        tdp.setTitle(getString(R.string.time_pick));
                        tdp.show(getFragmentManager(), "datePickerDialog");


                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "timePickerDialog");
    }

    @OnClick(R.id.contractEndDate_btn)
    void contractEndDateBtn() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final String newDate = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;

                        contractEndDateEditText.setText(newDate);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "timePickerDialog");
    }

    @OnClick(R.id.meetdate_btn)
    void meetCallDateBtn() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final String newDate = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                        TimePickerDialog tdp = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int second) {
                                String time = ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                                meetingCallDateEdit.setText(newDate + "&" + time);

                            }
                        }, 12, 0, 0, false);
                        tdp.enableSeconds(false);
                        tdp.setTitle(getString(R.string.time_pick));
                        tdp.show(getFragmentManager(), "datePickerDialog");


                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle(getString(R.string.birthdate_pick));
        dpd.show(getFragmentManager(), "timePickerDialog");
    }

    @OnClick(R.id.addSalesBtn)
    void addSales() {


        if (
                checkEditText(companyName) &&
                        checkEditText(arabicNameEdit) &&
                        checkEditText(engNameEdit) &&
                        checkEditText(customerEmailEdit) &&
                        checkEditText(faxNumberEdit) &&
                        checkEditText(mobileNumberEdit) &&
                        checkEditText(telephoneNumberEdit) &&
                        checkEditText(contractPersonEdit) &&
                        checkEditText(contractEmailEdit) &&
                        checkEditText(contractMobileEdit) &&
                        checkEditText(calDateEdit)

                ) {
            if (!personCheckBox.isChecked() && !companyCheckBox.isChecked()) {
                Snackbar.make(findViewById(android.R.id.content), R.string.select_customer_type, Snackbar.LENGTH_LONG).show();

            } else if (!utils.isEmailValid(customerEmailEdit.getText().toString())) {
                customerEmailEdit.setError(getString(R.string.info_email));
                customerEmailEdit.requestFocus();

            } else if (!utils.isMobileNumberValid(mobileNumberEdit.getText().toString())) {
                mobileNumberEdit.setError(getString(R.string.error_invalid_mobile));
                mobileNumberEdit.requestFocus();

            } else if (area == null) {
                Snackbar.make(findViewById(android.R.id.content), R.string.msg_select_area, Snackbar.LENGTH_LONG).show();
            } else {

                String[] calldatetime = calDateEdit.getText().toString().trim().split("&");
                String[] nextcalldatetime = nextCallDateEdit.getText().toString().trim().split("&");
                String[] meetdatetime = meetingCallDateEdit.getText().toString().trim().split("&");
                List<TeleContractDetail> teleContractDetailList = new ArrayList<TeleContractDetail>();


                TeleContractDetail teleContractDetail1 = checkContratCategory(1);
                TeleContractDetail teleContractDetail2 = checkContratCategory(2);
                TeleContractDetail teleContractDetail3 = checkContratCategory(3);
                TeleContractDetail teleContractDetail4 = checkContratCategory(4);
                if (teleContractDetail1 != null) {
                    teleContractDetailList.add(teleContractDetail1);
                }
                if (teleContractDetail2 != null) {
                    teleContractDetailList.add(teleContractDetail2);
                }
                if (teleContractDetail3 != null) {
                    teleContractDetailList.add(teleContractDetail3);
                }
                if (teleContractDetail4 != null) {
                    teleContractDetailList.add(teleContractDetail4);
                }


                Salesitem item = new Salesitem();
                item.setDevice("M");
                item.setCallTime(calldatetime[1]);
                item.setCallCount("0");
                item.setActive("Y");
                item.setFinish("N");
                item.setCompName(companyName.getText().toString());
                item.setNotInterestReply(nonReplyList.get(noneInterestReplySpinner.getSelectedIndex()).getCode());
                item.setReplyNotes(replyNoteEdit.getText().toString().isEmpty() ? "none" : replyNoteEdit.getText().toString());
                item.setAreaCode(area.getId());
                item.setReplyType(ReplyList.get(replySpinner.getSelectedIndex()).getCode());
                item.setReplyNotes(replyNoteEdit.getText().toString().isEmpty() ? "none" : replyNoteEdit.getText().toString());
                item.setMeetingDate(meetdatetime[0]);
                item.setMeetingTime(meetdatetime[1]);
                item.setContactName(contractPersonEdit.getText().toString().trim());
                item.setContactMail(contractEmailEdit.getText().toString().trim());
                item.setContacatTele(contractMobileEdit.getText().toString().trim());
                item.setMail(customerEmailEdit.getText().toString().trim());
                item.setMobile(mobileNumberEdit.getText().toString().trim());
                item.setFaxNo(faxNumberEdit.getText().toString().trim());
                item.setTeleNo(telephoneNumberEdit.getText().toString().trim());
                item.setCallDate(calldatetime[0]);
                item.setAgentAname(arabicNameEdit.getText().toString().trim());
                item.setAgentEname(engNameEdit.getText().toString().trim());
                item.setCreatedBy(App.getInstance().getPrefManager().getCurrentUser().getFirstName());
                item.setCreatedDate("10-5-2018");
                item.setId("0");
                item.setParentId(salesitem != null ? salesitem.getParentId() : "0");
                item.setCustomerType(companyCheckBox.isChecked() ? "COMP" : "EMP");
                item.setNextCallDate(nextcalldatetime[0]);
                item.setNextDateTime(nextcalldatetime[1]);
                item.setNotInterestReplyNotes(nonintReplyEdit.getText().toString().isEmpty() ? "none" : nonintReplyEdit.getText().toString());
                item.setContractDetail(teleContractDetailList);
                item.setEndDateContract(contractEndDateEditText.getText().toString());
                SalesCall salesCall = new SalesCall();
                salesCall.setSalesitem(item);
                String s = new Gson().toJson(salesCall);
                Log.d("Tag", s);
                DialogUtils.showDialog(this, true);
                Call call = null;
                if (salesitem != null) {
                    call = App.getInstance().getService().updateSalesCall(salesCall);
                } else {
                    call = App.getInstance().getService().addSalesCall(salesCall);

                }


                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        DialogUtils.showDialog(AddEditTeleSalesActivity.this, false);
                        Message message = response.body();
                        if (message.getCode() == 1) {
                            opeartionNumberEdit.requestFocus();
                            opeartionNumberEdit.setText(message.getUserid());
                            Snackbar.make(findViewById(android.R.id.content), R.string.sucess_add_sales, Snackbar.LENGTH_LONG).show();

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), message.getDetails(), Snackbar.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        DialogUtils.showDialog(AddEditTeleSalesActivity.this, false);
                        Snackbar.make(findViewById(android.R.id.content), R.string.cant_connect_to_server, Snackbar.LENGTH_LONG).show();

                    }
                });
            }

        }

    }

    public boolean checkEditText(EditText editText) {
        String temp = editText.getText().toString().trim();
        boolean result = false;
        if (!TextUtils.isEmpty(temp)) {
            result = true;
        } else {
            editText.setError("Required");
            editText.requestFocus();
            result = false;
        }

        return result;
    }


    private TeleContractDetail checkContratCategory(int categoryNumber) {

        //start first category
        if (categoryNumber == 1) {
            if (
                    !TextUtils.isEmpty(annualCellingFirst.getText().toString()) ||
                            !TextUtils.isEmpty(numOfSubscribersFirst.getText().toString()) ||
                            medicalNetworkFirst.getSelectedIndex() != 0 ||
                            accomodationFirst.getSelectedIndex() != 0 ||
                            !TextUtils.isEmpty(inpatientServiceFirst.getText().toString()) ||
                            !TextUtils.isEmpty(consultationFirst.getText().toString()) ||
                            !TextUtils.isEmpty(investigationFirst.getText().toString()) ||
                            !TextUtils.isEmpty(physiotherapyFirst.getText().toString()) ||
                            !TextUtils.isEmpty(outPatientFirst.getText().toString()) ||
                            !TextUtils.isEmpty(normalDeliveryFirst.getText().toString()) ||
                            !TextUtils.isEmpty(cSectionFirst.getText().toString()) ||
                            !TextUtils.isEmpty(legalAbortionFirst.getText().toString()) ||
                            !TextUtils.isEmpty(dentalBasicFirst.getText().toString()) ||
                            !TextUtils.isEmpty(rootCanalFirst.getText().toString()) ||
                            !TextUtils.isEmpty(dentalCrownsFirst.getText().toString()) ||
                            !TextUtils.isEmpty(opticalEyeFirst.getText().toString()) ||
                            !TextUtils.isEmpty(preExistingFirst.getText().toString()) ||
                            !TextUtils.isEmpty(criticalCasesFirst.getText().toString())) {
                //start condition
                TeleContractDetail item = new TeleContractDetail();
                item.setCategory("1");
                item.setAnnualCeiling(annualCellingFirst.getText().toString());
                item.setSubscriberNum(numOfSubscribersFirst.getText().toString());
                item.setMedicalNetwork(medNetworkItems.get(medicalNetworkFirst.getSelectedIndex()));//edit
                item.setAccommodation(accomodationItems.get(accomodationFirst.getSelectedIndex()));//edit
                item.setInpatientService(inpatientServiceFirst.getText().toString());
                item.setConsulation(consultationFirst.getText().toString());
                item.setInVestigations(investigationFirst.getText().toString());
                item.setPhysiotherapy(physiotherapyFirst.getText().toString());
                item.setOutpatientMedications(outPatientFirst.getText().toString());
                item.setNormalDelivery(normalDeliveryFirst.getText().toString());
                item.setSection(cSectionFirst.getText().toString());
                item.setLegalAbortion(legalAbortionFirst.getText().toString());
                item.setBasic(dentalBasicFirst.getText().toString());
                item.setRootCanal(rootCanalFirst.getText().toString());
                item.setCrowns(dentalCrownsFirst.getText().toString());
                item.setOptical(opticalEyeFirst.getText().toString());
                item.setPreExisting(preExistingFirst.getText().toString());
                item.setCriticalCases(criticalCasesFirst.getText().toString());


                return item;
            }
        }
        //end first category


        //start second category
        else if (categoryNumber == 2) {
            if (
                    !TextUtils.isEmpty(annualCellingSecond.getText().toString()) ||
                            !TextUtils.isEmpty(numOfSubscribersSecond.getText().toString()) ||
                            medicalNetworkSecond.getSelectedIndex() != 0 ||
                            accomodationSecond.getSelectedIndex() != 0 ||
                            !TextUtils.isEmpty(inpatientServiceSecond.getText().toString()) ||
                            !TextUtils.isEmpty(consultationSecond.getText().toString()) ||
                            !TextUtils.isEmpty(investigationSecond.getText().toString()) ||
                            !TextUtils.isEmpty(physiotherapySecond.getText().toString()) ||
                            !TextUtils.isEmpty(outPatientSecond.getText().toString()) ||
                            !TextUtils.isEmpty(normalDeliverySecond.getText().toString()) ||
                            !TextUtils.isEmpty(cSectionSecond.getText().toString()) ||
                            !TextUtils.isEmpty(legalAbortionSecond.getText().toString()) ||
                            !TextUtils.isEmpty(dentalBasicSecond.getText().toString()) ||
                            !TextUtils.isEmpty(rootCanalSecond.getText().toString()) ||
                            !TextUtils.isEmpty(dentalCrownsSecond.getText().toString()) ||
                            !TextUtils.isEmpty(opticalEyeSecond.getText().toString()) ||
                            !TextUtils.isEmpty(preExistingSecond.getText().toString()) ||
                            !TextUtils.isEmpty(criticalCasesSecond.getText().toString())) {
                //start condition
                TeleContractDetail item = new TeleContractDetail();
                item.setCategory("2");
                item.setAnnualCeiling(annualCellingSecond.getText().toString());
                item.setSubscriberNum(numOfSubscribersSecond.getText().toString());
                item.setMedicalNetwork(medNetworkItems.get(medicalNetworkSecond.getSelectedIndex()));//edit
                item.setAccommodation(accomodationItems.get(accomodationSecond.getSelectedIndex()));//edit
                item.setInpatientService(inpatientServiceSecond.getText().toString());
                item.setConsulation(consultationSecond.getText().toString());
                item.setInVestigations(investigationSecond.getText().toString());
                item.setPhysiotherapy(physiotherapySecond.getText().toString());
                item.setOutpatientMedications(outPatientSecond.getText().toString());
                item.setNormalDelivery(normalDeliverySecond.getText().toString());
                item.setSection(cSectionSecond.getText().toString());
                item.setLegalAbortion(legalAbortionSecond.getText().toString());
                item.setBasic(dentalBasicSecond.getText().toString());
                item.setRootCanal(rootCanalSecond.getText().toString());
                item.setCrowns(dentalCrownsSecond.getText().toString());
                item.setOptical(opticalEyeSecond.getText().toString());
                item.setPreExisting(preExistingSecond.getText().toString());
                item.setCriticalCases(criticalCasesSecond.getText().toString());


                return item;
            }
        }
        //end second category


        //start third category
        else if (categoryNumber == 3) {
            if (
                    !TextUtils.isEmpty(annualCellingThird.getText().toString()) ||
                            !TextUtils.isEmpty(numOfSubscribersThird.getText().toString()) ||
                            medicalNetworkThird.getSelectedIndex() != 0 ||
                            accomodationThird.getSelectedIndex() != 0 ||
                            !TextUtils.isEmpty(inpatientServiceThird.getText().toString()) ||
                            !TextUtils.isEmpty(consultationThird.getText().toString()) ||
                            !TextUtils.isEmpty(investigationThird.getText().toString()) ||
                            !TextUtils.isEmpty(physiotherapyThird.getText().toString()) ||
                            !TextUtils.isEmpty(outPatientThird.getText().toString()) ||
                            !TextUtils.isEmpty(normalDeliveryThird.getText().toString()) ||
                            !TextUtils.isEmpty(cSectionThird.getText().toString()) ||
                            !TextUtils.isEmpty(legalAbortionThird.getText().toString()) ||
                            !TextUtils.isEmpty(dentalBasicThird.getText().toString()) ||
                            !TextUtils.isEmpty(rootCanalThird.getText().toString()) ||
                            !TextUtils.isEmpty(dentalCrownsThird.getText().toString()) ||
                            !TextUtils.isEmpty(opticalEyeThird.getText().toString()) ||
                            !TextUtils.isEmpty(preExistingThird.getText().toString()) ||
                            !TextUtils.isEmpty(criticalCasesThird.getText().toString())) {
                //start condition
                TeleContractDetail item = new TeleContractDetail();
                item.setCategory("3");
                item.setAnnualCeiling(annualCellingThird.getText().toString());
                item.setSubscriberNum(numOfSubscribersThird.getText().toString());
                item.setMedicalNetwork(medNetworkItems.get(medicalNetworkThird.getSelectedIndex()));//edit
                item.setAccommodation(accomodationItems.get(accomodationThird.getSelectedIndex()));//edit
                item.setInpatientService(inpatientServiceThird.getText().toString());
                item.setConsulation(consultationThird.getText().toString());
                item.setInVestigations(investigationThird.getText().toString());
                item.setPhysiotherapy(physiotherapyThird.getText().toString());
                item.setOutpatientMedications(outPatientThird.getText().toString());
                item.setNormalDelivery(normalDeliveryThird.getText().toString());
                item.setSection(cSectionThird.getText().toString());
                item.setLegalAbortion(legalAbortionThird.getText().toString());
                item.setBasic(dentalBasicThird.getText().toString());
                item.setRootCanal(rootCanalThird.getText().toString());
                item.setCrowns(dentalCrownsThird.getText().toString());
                item.setOptical(opticalEyeThird.getText().toString());
                item.setPreExisting(preExistingThird.getText().toString());
                item.setCriticalCases(criticalCasesThird.getText().toString());

                return item;
            }
        }
        //end third category


        //start fourth category
        else if (categoryNumber == 4) {
            if (
                    !TextUtils.isEmpty(annualCellingFourth.getText().toString()) ||
                            !TextUtils.isEmpty(numOfSubscribersFourth.getText().toString()) ||
                            medicalNetworkFourth.getSelectedIndex() != 0 ||
                            accomodationFourth.getSelectedIndex() != 0 ||
                            !TextUtils.isEmpty(inpatientServiceFourth.getText().toString()) ||
                            !TextUtils.isEmpty(consultationFourth.getText().toString()) ||
                            !TextUtils.isEmpty(investigationFourth.getText().toString()) ||
                            !TextUtils.isEmpty(physiotherapyFourth.getText().toString()) ||
                            !TextUtils.isEmpty(outPatientFourth.getText().toString()) ||
                            !TextUtils.isEmpty(normalDeliveryFourth.getText().toString()) ||
                            !TextUtils.isEmpty(cSectionFourth.getText().toString()) ||
                            !TextUtils.isEmpty(legalAbortionFourth.getText().toString()) ||
                            !TextUtils.isEmpty(dentalBasicFourth.getText().toString()) ||
                            !TextUtils.isEmpty(rootCanalFourth.getText().toString()) ||
                            !TextUtils.isEmpty(dentalCrownsFourth.getText().toString()) ||
                            !TextUtils.isEmpty(opticalEyeFourth.getText().toString()) ||
                            !TextUtils.isEmpty(preExistingFourth.getText().toString()) ||
                            !TextUtils.isEmpty(criticalCasesFourth.getText().toString())) {
                //start condition
                TeleContractDetail item = new TeleContractDetail();
                item.setCategory("4");
                item.setAnnualCeiling(annualCellingFourth.getText().toString());
                item.setSubscriberNum(numOfSubscribersFourth.getText().toString());
                item.setMedicalNetwork(medNetworkItems.get(medicalNetworkFourth.getSelectedIndex()));//edit
                item.setAccommodation(accomodationItems.get(accomodationFourth.getSelectedIndex()));//edit
                item.setInpatientService(inpatientServiceFourth.getText().toString());
                item.setConsulation(consultationFourth.getText().toString());
                item.setInVestigations(investigationFourth.getText().toString());
                item.setPhysiotherapy(physiotherapyFourth.getText().toString());
                item.setOutpatientMedications(outPatientFourth.getText().toString());
                item.setNormalDelivery(normalDeliveryFourth.getText().toString());
                item.setSection(cSectionFourth.getText().toString());
                item.setLegalAbortion(legalAbortionFourth.getText().toString());
                item.setBasic(dentalBasicFourth.getText().toString());
                item.setRootCanal(rootCanalFourth.getText().toString());
                item.setCrowns(dentalCrownsFourth.getText().toString());
                item.setOptical(opticalEyeFourth.getText().toString());
                item.setPreExisting(preExistingFourth.getText().toString());
                item.setCriticalCases(criticalCasesFourth.getText().toString());

                return item;
            }
        }
        //end third category
        return null;
    }


    private void initializeContarctCategory(TeleContractDetail item) {
        if (item != null) {
            if (item.getCategory().equals("1")) {
                annualCellingFirst.setText(item.getAnnualCeiling());
                numOfSubscribersFirst.setText(item.getSubscriberNum());
                medicalNetworkFirst.setText(item.getMedicalNetwork());
                accomodationFirst.setText(item.getAccommodation());
                inpatientServiceFirst.setText(item.getInpatientService());
                consultationFirst.setText(item.getConsulation());
                investigationFirst.setText(item.getInVestigations());
                physiotherapyFirst.setText(item.getPhysiotherapy());
                outPatientFirst.setText(item.getOutpatientMedications());
                normalDeliveryFirst.setText(item.getNormalDelivery());
                cSectionFirst.setText(item.getSection());
                legalAbortionFirst.setText(item.getLegalAbortion());
                dentalBasicFirst.setText(item.getBasic());
                rootCanalFirst.setText(item.getRootCanal());
                dentalCrownsFirst.setText(item.getCrowns());
                opticalEyeFirst.setText(item.getOptical());
                preExistingFirst.setText(item.getPreExisting());
                criticalCasesFirst.setText(item.getCriticalCases());

            } else if (item.getCategory().equals("2")) {
                annualCellingSecond.setText(item.getAnnualCeiling());
                numOfSubscribersSecond.setText(item.getSubscriberNum());
                medicalNetworkSecond.setText(item.getMedicalNetwork());
                accomodationSecond.setText(item.getAccommodation());
                inpatientServiceSecond.setText(item.getInpatientService());
                consultationSecond.setText(item.getConsulation());
                investigationSecond.setText(item.getInVestigations());
                physiotherapySecond.setText(item.getPhysiotherapy());
                outPatientSecond.setText(item.getOutpatientMedications());
                normalDeliverySecond.setText(item.getNormalDelivery());
                cSectionSecond.setText(item.getSection());
                legalAbortionSecond.setText(item.getLegalAbortion());
                dentalBasicSecond.setText(item.getBasic());
                rootCanalSecond.setText(item.getRootCanal());
                dentalCrownsSecond.setText(item.getCrowns());
                opticalEyeSecond.setText(item.getOptical());
                preExistingSecond.setText(item.getPreExisting());
                criticalCasesSecond.setText(item.getCriticalCases());
            } else if (item.getCategory().equals("3")) {
                annualCellingThird.setText(item.getAnnualCeiling());
                numOfSubscribersThird.setText(item.getSubscriberNum());
                medicalNetworkThird.setText(item.getMedicalNetwork());
                accomodationThird.setText(item.getAccommodation());
                inpatientServiceThird.setText(item.getInpatientService());
                consultationThird.setText(item.getConsulation());
                investigationThird.setText(item.getInVestigations());
                physiotherapyThird.setText(item.getPhysiotherapy());
                outPatientThird.setText(item.getOutpatientMedications());
                normalDeliveryThird.setText(item.getNormalDelivery());
                cSectionThird.setText(item.getSection());
                legalAbortionThird.setText(item.getLegalAbortion());
                dentalBasicThird.setText(item.getBasic());
                rootCanalThird.setText(item.getRootCanal());
                dentalCrownsThird.setText(item.getCrowns());
                opticalEyeThird.setText(item.getOptical());
                preExistingThird.setText(item.getPreExisting());
                criticalCasesThird.setText(item.getCriticalCases());
            } else if (item.getCategory().equals("4")) {
                annualCellingFourth.setText(item.getAnnualCeiling());
                numOfSubscribersFourth.setText(item.getSubscriberNum());
                medicalNetworkFourth.setText(item.getMedicalNetwork());
                accomodationFourth.setText(item.getAccommodation());
                inpatientServiceFourth.setText(item.getInpatientService());
                consultationFourth.setText(item.getConsulation());
                investigationFourth.setText(item.getInVestigations());
                physiotherapyFourth.setText(item.getPhysiotherapy());
                outPatientFourth.setText(item.getOutpatientMedications());
                normalDeliveryFourth.setText(item.getNormalDelivery());
                cSectionFourth.setText(item.getSection());
                legalAbortionFourth.setText(item.getLegalAbortion());
                dentalBasicFourth.setText(item.getBasic());
                rootCanalFourth.setText(item.getRootCanal());
                dentalCrownsFourth.setText(item.getCrowns());
                opticalEyeFourth.setText(item.getOptical());
                preExistingFourth.setText(item.getPreExisting());
                criticalCasesFourth.setText(item.getCriticalCases());
            }
        }
    }

    @Override
    public void OnAreaClicked(AreaItem item) {
        areaSpinner.setText(item.getName());
        area = item;
    }

    void getReplyList() {
        replyProgress.setVisibility(View.VISIBLE);
        replyretryImgv.setVisibility(View.GONE);
        App.getInstance().getService().getReplyList().enqueue(new Callback<SalesReplyResponse>() {
            @Override
            public void onResponse(Call<SalesReplyResponse> call, Response<SalesReplyResponse> response) {
                replyProgress.setVisibility(View.GONE);

                SalesReplyResponse salesReply = response.body();
                if (salesReply.getMessage().getCode() == 1) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("Select Reply Type");
                    for (SalesReply reply : salesReply.getList()) {
                        list.add(reply.getName());
                        if (salesitem != null) {
                            if (salesitem.getReplyType().equals(reply.getCode())) {
                                replyindex = list.size() - 1;
                            }
                        }
                    }
                    replySpinner.setItems(list);
                    replySpinner.setSelectedIndex(replyindex);
                    ReplyList = salesReply.getList();
                } else {
                    replyretryImgv.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<SalesReplyResponse> call, Throwable t) {
                replyProgress.setVisibility(View.GONE);
                replyretryImgv.setVisibility(View.VISIBLE);
            }
        });


    }

    void getNoneInterstReplyList() {
        nonereplyProgress.setVisibility(View.VISIBLE);
        nonereplyretryImgv.setVisibility(View.GONE);
        App.getInstance().getService().getNonInterestReplyList().enqueue(new Callback<SalesReplyResponse>() {
            @Override
            public void onResponse(Call<SalesReplyResponse> call, Response<SalesReplyResponse> response) {
                nonereplyProgress.setVisibility(View.GONE);

                SalesReplyResponse salesReply = response.body();
                if (salesReply.getMessage().getCode() == 1) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("Select Non Interest Reply Type");
                    for (SalesReply reply : salesReply.getList()) {
                        list.add(reply.getName());
                        if (salesitem != null) {
                            if (salesitem.getNotInterestReply().equals(reply.getCode())) {
                                noereplyindex = list.size() - 1;
                            }
                        }
                    }
                    noneInterestReplySpinner.setItems(list);
                    noneInterestReplySpinner.setSelectedIndex(noereplyindex);
                    nonReplyList = salesReply.getList();

                } else {
                    nonereplyretryImgv.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<SalesReplyResponse> call, Throwable t) {
                nonereplyProgress.setVisibility(View.GONE);
                nonereplyretryImgv.setVisibility(View.VISIBLE);
            }
        });


    }

    @OnClick({R.id.medicalNetworkFirst_retry, R.id.medicalNetworkSecond_retry, R.id.medicalNetworkThird_retry, R.id.medicalNetworkFourth_retry})
    void retryGetMedicalNetwork() {
        getMedicalNetworkList();
    }


    @OnClick({R.id.accomodationFirst_retry, R.id.accomodationSecond_retry, R.id.accomodationThird_retry, R.id.accomodationFourth_retry})
    void retryGetAccomodation() {
        getAccomodationList();
    }

    @OnClick(R.id.area_retry)
    void retryGetArea() {
        getAreas();
    }

    @OnClick(R.id.reply_retry)
    void retryGetReply() {
        getReplyList();
    }

    @OnClick(R.id.nonereply_retry)
    void retryGetNoneReply() {
        getNoneInterstReplyList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SearchTeleSalesActivity.class));
        finish();
    }
}
