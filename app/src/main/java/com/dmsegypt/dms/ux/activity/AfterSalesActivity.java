package com.dmsegypt.dms.ux.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.AfterSales;
import com.dmsegypt.dms.rest.model.AfterSalesItem;
import com.dmsegypt.dms.rest.model.Agent;
import com.dmsegypt.dms.rest.model.Company;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.AgentResponse;
import com.dmsegypt.dms.rest.model.Response.ResponseCompany;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
import com.dmsegypt.dms.ux.dialogs.CustomCompanyFragment;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AfterSalesActivity extends BaseActivity implements CustomCompanyFragment.OnCompleteListener{


    @BindView(R.id.visit_number)
    EditText visitNumber;

    @BindView(R.id.companyNameSpinner)
    MaterialSpinner companyNameSpinner;

    @BindView(R.id.branshSpinner)
    ProgressableSpinner branshSpinner;


    @BindView(R.id.employeeNameSpinner)
    ProgressableSpinner employeeNameSpinner;

    @BindView(R.id.contact_person_name)
    EditText contactPersonName;

    @BindView(R.id.visit_reasone_spinner)
    ProgressableSpinner visitReasoneSpinner;

    @BindView(R.id.visitDateET)
    EditText visitDateET;

    @BindView(R.id.visit_date_btn)
    ImageButton visitDateBtn;

    @BindView(R.id.feedbackET)
    EditText feedbackET;


    @BindView(R.id.addAfterSalesBtn)
    Button Button;

    boolean updateState=false;
    String []   reasons={"حل مشكلة ال Network","متابعة دورية","حل مشكة تحصيل","اخري"};

    public static final String EXTRA_OBJ = "extra_obj";

    String companyID="-1";
    String companyName="";
    String branchName="";
    String visitCode="";
    List<String>branchesList;
    List<String>agentsList;
    String afterSalesDate="";
    String afterSalesTime="";
    AfterSalesItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    void  initView()
    {
        visitNumber.setEnabled(false);
        branshSpinner.getSpinner().setText("Branch Spinner");
        employeeNameSpinner.getSpinner().setText("Employees");
        visitReasoneSpinner.getSpinner().setText("Visit Reasone");
        visitReasoneSpinner.getSpinner().setItems(reasons);
        employeeNameSpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
            @Override
            public void onRetry() {
                getAgents();
            }
        });
        branshSpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
            @Override
            public void onRetry() {
                getCompanyBranches(companyID);
            }
        });
        getAgents();
        item = getIntent().getParcelableExtra(EXTRA_OBJ);

        if (item!=null)
        {
            updateState=true;
            visitNumber.setText(item.getId());
            String compName=item.getCompanyName();
            companyName=compName;
            companyNameSpinner.setText(compName);
            branchName=item.getBranchName();
            branshSpinner.getSpinner().setText(branchName);
            visitCode=item.getId();
            contactPersonName.setText(item.getClientName());
            afterSalesDate=item.getVisitDate();
            afterSalesTime=item.getTime();
            visitDateET.setText(afterSalesDate+"&"+afterSalesTime);
            feedbackET.setText(item.getFeedback());

        }



    }

    private void getAgents()
    {

            employeeNameSpinner.showLoading();
        App.getInstance().getService().getDepAgent("After Sales").enqueue(new Callback<AgentResponse>() {
            @Override
            public void onResponse(Call<AgentResponse> call, Response<AgentResponse> response) {

                if (response.body().getMessage().getCode()==1)
                {
                    employeeNameSpinner.showSucess();
                    agentsList=new ArrayList<>();

                    if (response.body().getList().size()>0)
                    {
                        for (Agent agent:response.body().getList())
                        {
                            agentsList.add(agent.getAgentName());
                        }
                        employeeNameSpinner.getSpinner().setItems(agentsList);

                    }else {
                        employeeNameSpinner.getSpinner().setText("No Comapnies");

                    }

                }else {
                    employeeNameSpinner.showFailure();

                }
            }

            @Override
            public void onFailure(Call<AgentResponse> call, Throwable throwable) {
                employeeNameSpinner.showFailure();

            }
        });


    }
    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_after_sales;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

   @OnClick(R.id.button_close)
    void closeActivity(){
        finish();
   }
    @OnClick(R.id.companyNameSpinner)
    void showCompanyNameDialoge(){

        final CustomCompanyFragment cusdialog=new CustomCompanyFragment();
        FragmentManager fragmentManager=getFragmentManager();
        cusdialog.show(fragmentManager,"companies");

    }


    @OnClick(R.id.addAfterSalesBtn)
    void setButton() {

         if (TextUtils.isEmpty(companyName))
        {
            companyNameSpinner.requestFocus();
            Snackbar.make(findViewById(android.R.id.content),"No item was selected !",Snackbar.LENGTH_LONG).show();
        return;}

        else if (TextUtils.isEmpty(branchName))
        {
            branchName="-1";
       return;
        }

        else if (TextUtils.isEmpty(afterSalesDate) || TextUtils.isEmpty(afterSalesTime))
        {
            visitDateET.requestFocus();
            visitDateET.setError(getString(R.string.error_field_required));
          return;
        }



        String feedback = feedbackET.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();


        AfterSalesItem item=new AfterSalesItem();
        item.setCompanyName(companyName);

        item.setEmpName(agentsList.get(employeeNameSpinner.getSpinner().getSelectedIndex()));
        item.setClientName(contactPersonName.getText().toString().trim());
        item.setVisitDate(afterSalesDate);
        item.setTime(afterSalesTime);
        item.setVisitReason(reasons[visitReasoneSpinner.getSpinner().getSelectedIndex()]);
        item.setCreatedBy(App.getInstance().getPrefManager().getCurrentUser().getFirstName());
        item.setDeletedFlag("N");
        item.setUpdatedBy(App.getInstance().getPrefManager().getCurrentUser().getFirstName());
        item.setUpdatedDate(formatter.format(date));
        item.setReasonEdit(reasons[visitReasoneSpinner.getSpinner().getSelectedIndex()]);
        item.setFeedback(feedback);

        AfterSales afterSales=new AfterSales();
        afterSales.setAfterSalesItem(item);
        String s = new Gson().toJson(afterSales);
        Log.d("Tag", s);

        if (updateState)
        {
            item.setId(visitCode);
            item.setBranchName(branchName);
            item.setUpdatedDate("01-01-2018");
            item.setUpdatedBy(App.getInstance().getPrefManager().getCurrentUser().getFirstName());
            updateAferSales(afterSales);

        }else {
            item.setId("");
            item.setBranchName(branchesList.get(branshSpinner.getSpinner().getSelectedIndex()));
            addAferSales(afterSales);
        }


    }

   private void updateAferSales(AfterSales afterSales)
    {
       String s=new Gson().toJson(afterSales);
        DialogUtils.showDialog(AfterSalesActivity.this,true);
        App.getInstance().getService().updateAfterSales(afterSales).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                DialogUtils.showDialog(AfterSalesActivity.this,false);
                if (response.body()!=null)
                {
                    if (response.body().getCode()==1) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.sucess_update, Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.failed_update, Snackbar.LENGTH_LONG).show();


                    }
                }else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.error_inernet_connection, Snackbar.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable throwable) {
                DialogUtils.showDialog(AfterSalesActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();

            }
        });

    }


    private void addAferSales(AfterSales afterSales)
    {
        DialogUtils.showDialog(AfterSalesActivity.this,true);
        App.getInstance().getService().addAfterSales(afterSales).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                DialogUtils.showDialog(AfterSalesActivity.this,false);
                if (response.body()!=null)
                {
                   if (response.body().getCode()==1)
                    Snackbar.make(findViewById(android.R.id.content),R.string.sucess_add,Snackbar.LENGTH_LONG).show();
                    else
                       Snackbar.make(findViewById(android.R.id.content),R.string.failed_add,Snackbar.LENGTH_LONG).show();

                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection,Snackbar.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable throwable) {
                DialogUtils.showDialog(AfterSalesActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @OnClick(R.id.visit_date_btn)
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

                                visitDateET.setText(newDate + "&" + time);
                                afterSalesDate=newDate;
                                afterSalesTime=time;

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



    @Override
    public void onComplete(String compName,String compID) {

        companyNameSpinner.setText(compName);
        companyName=compName;
        companyID=compID;

        getCompanyBranches(compID);

    }


    void getCompanyBranches(String compID)
    {

        branshSpinner.showLoading();
        App.getInstance().getService().getCompanyBranch(compID).enqueue(new Callback<ResponseCompany>() {
            @Override
            public void onResponse(Call<ResponseCompany> call, Response<ResponseCompany> response) {
                DialogUtils.showDialog(AfterSalesActivity.this,false);

                if (response.body().getMessage().getCode()==1)
                {
                    branshSpinner.showSucess();
                    branchesList=new ArrayList<>();

                    if (response.body().getList().size()>0)
                    {
                        for (Company company:response.body().getList())
                        {
                            branchesList.add(company.getComp_ename());
                        }
                        branshSpinner.getSpinner().setItems(branchesList);

                    }else {
                        branshSpinner.getSpinner().setText("No Branches");
                    }

                }else {

                    branshSpinner.showFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseCompany> call, Throwable throwable) {
                branshSpinner.showFailure();

            }
        });
    }

    @Override
    public void onBackPressed() {
      finish();
    }
}
