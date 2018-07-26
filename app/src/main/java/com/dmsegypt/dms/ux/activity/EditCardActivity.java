package com.dmsegypt.dms.ux.activity;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.utils.DialogUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCardActivity extends BaseActivity {

    //region Init View
    @BindView(R.id.etCardId)
    EditText etCardId;
    @BindView(R.id.etReason)
    EditText etReason;
    @BindView(R.id.degreespinner)
    MaterialSpinner degreespinner;
    @BindView(R.id.btnSubmit)
    Button SubmitBtn;
    //endregion
    //region references
    Call call;
   //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initview();

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.DegreeList.size()==0){

                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    getClassName();
                                }
                            })
                            .setActionTextColor(Color.GREEN).show();
                    return;
                }
                if (etCardId.getText().toString().isEmpty()){
                    etCardId.setError(getString(R.string.err_empty_field));
                    etCardId.requestFocus();
                    return;
                }
                if (etReason.getText().toString().isEmpty()){
                    etReason.setError(getString(R.string.err_empty_field));
                    etReason.requestFocus();
                    return;
                }

                String CardId=etCardId.getText().toString().trim();
                String CreatedCardId=App.getInstance().getPrefManager().getCurrentUser().getCardId();
                String emp_class=Constants.DegreeList.get(degreespinner.getSelectedIndex()).toString();
                String reason=etReason.getText().toString().trim();
                 updateEmploye(CardId,emp_class,reason,CreatedCardId);


            }
        });

    }
        // send request to update employee class with reason and cardid
    private void updateEmploye(String CardId,String emp_class,String reason,String CreatedCardId) {
        DialogUtils.showDialog(this,true);
        call=  App.getInstance().getService().updateEmployeeRequest(CardId,emp_class,reason,getAppLanguage(),CreatedCardId);
        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {


                DialogUtils.showDialog(EditCardActivity.this,false);

                if (response != null && response.body() != null) {

                    if (response.body().getMessage() !=null  && response.body().getMessage().getCode() == 1) {
                        Snackbar.make(findViewById(android.R.id.content),R.string.msg_request_sent,Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();
                    }


                }}


            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                DialogUtils.showDialog(EditCardActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_edit_card;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    public void initview(){
        Constants.DegreeList=new ArrayList<>();

        String cardId=getIntent().getExtras().getString("CARD_ID");
        etCardId.setText(cardId);
        getClassName();


    }

    @Override
    public void onBackPressed() {
        finish();
    }


    //get class name of employee
    private void getClassName() {
        DialogUtils.showDialog(this,true);
        call =App.getInstance().getService().getClassNameList(App.getInstance().getPrefManager().getUsername(),getAppLanguage());
        call.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                DialogUtils.showDialog(EditCardActivity.this,false);
                if(response != null && response.body().getList().size() != 0){

                        Constants.DegreeList.addAll(response.body().getList());
                    degreespinner.setItems(Constants.DegreeList);

                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    getClassName();
                                }
                            }).setActionTextColor(Color.GREEN).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable throwable) {
                DialogUtils.showDialog(EditCardActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                getClassName();
                            }
                        }).setActionTextColor(Color.GREEN).show();
            }
        });
    }
    @OnClick(R.id.iBtnCloseBtn)
    void closeActivity(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}
