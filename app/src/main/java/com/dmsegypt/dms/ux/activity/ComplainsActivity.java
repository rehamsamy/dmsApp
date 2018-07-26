package com.dmsegypt.dms.ux.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Lookup;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseCities;
import com.dmsegypt.dms.rest.model.Response.ResponseGeneralLookup;
import com.dmsegypt.dms.rest.model.Response.ResponseProviderTypes;
import com.dmsegypt.dms.rest.model.Response.ResponseProvidersSimple;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/
//activity that send service complain and provider complain
public class ComplainsActivity extends BaseActivity
        implements
        SwipeBackCoordinatorLayout.OnSwipeListener {



    //region Complaint View
    @BindView(R.id.activity_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;

    @BindView(R.id.activity_container)
    CoordinatorLayout container;

    @BindView(R.id.msCity)
    ProgressableSpinner msCity;

    @BindView(R.id.msArea)
    ProgressableSpinner msArea;

    @BindView(R.id.msType)
    ProgressableSpinner msType;

    @BindView(R.id.sp_provider)
    ProgressableSpinner msProvider;

    @BindView(R.id.btn_submit)
    Button containerFilteringViewLargeFeedbackBtn;

    @BindView(R.id.et_title)
    EditText etTitle;

    @BindView(R.id.et_description)
    EditText etDescription;

    @BindView(R.id.activity_appBar)
    NestedScrollView llNoResult;

    @BindView(R.id.ll_filters)
    TableRow llFilters;

    //endregion
//region references
    private final String TAG = this.getClass().getSimpleName();
    private Unbinder unbinder;
    private MaterialDialog loadingDialog;
    private List<Lookup> services;
    private int loadingCount = -1;
    Call call;
    Call nestedCall;
//endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();


    }




    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_complains;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_complaints;
    }


    /**
     * inti View
     */
    private void initView() {
        swipeBackView.setOnSwipeListener(this);
        if (getIntent().getIntExtra(Constants.KEY_COMPLAINTS_TYPE, Constants.KEY_COMPLAINTS_PROVIDER) == Constants.KEY_COMPLAINTS_SERVICE) {
            llFilters.setVisibility(View.GONE);
            msType.setVisibility(View.GONE);
            msProvider.setEnabled(true);
            msProvider.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            msProvider.getSpinner().setText(getString(R.string.service));
            getServices();

        } else {

            msCity.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                         msArea.setEnabled(false);
                        getArea(position);
                    msType.getSpinner().setSelectedIndex(0);
                    msProvider.setEnabled(false);
                    msType.setEnabled(false);
                }
            });



            msArea.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    msProvider.setEnabled(false);
                    if (position==0){
                        msType.setEnabled(false);
                        return;
                    }
                    msType.getSpinner().setSelectedIndex(0);
                    msType.setEnabled(true);

                }
            });

            msType.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (Constants.simpleProviders!=null)
                    Constants.simpleProviders.clear();
                    updateProviders("0");

                }
            });

            getCitiesAndProviderType();

        }



    }

    // get Areea of cities.
    void getArea(final int position){
msArea.showLoading();
msArea.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
    @Override
    public void onRetry() {
        getArea(position);

    }
});
        call= App.getInstance().getService().getAreas(Constants.providerCities.get(position).getId(), getAppLanguage(), Constants.KEY_COMPLAINTS_ID);
        call.enqueue(new Callback<ResponseAreas>() {
            @Override
            public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                if (response.body() != null) {

                    if (response.body().getMessage().getCode() == 1 && response.body().getList().size() > 0) {
                        Constants.providerAreas = response.body().getList();
                        msArea.getSpinner().setItems(Constants.providerAreas);
                        msArea.setEnabled(true);
                        msArea.showSucess();


                    }else {
                        msArea.getSpinner().setItems("");
                        msArea.getSpinner().setText(getString(R.string.area));
                        msArea.setEnabled(false);
                        msArea.showFailure();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseAreas> call, Throwable t) {
                msArea.showFailure();

            }
        });
    }


    /**
     * get Service Department to send Complain like dotcor name and hospital name
     */
    private void getServices() {

        msProvider.showLoading();
        msProvider.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
            @Override
            public void onRetry() {
                getServices();
            }
        });
call=App.getInstance().getService().getServiceDepartments(getAppLanguage());
        call.enqueue(new Callback<ResponseGeneralLookup>() {
            @Override
            public void onResponse(Call<ResponseGeneralLookup> call, Response<ResponseGeneralLookup> response) {

                if (response.body() != null) {

                    Message message = response.body().getMessage();

                    if (message.getCode() != 1) {
                        if (message.getDetails().equals(getString(R.string.error_no_data_available))) {
                            llNoResult.setVisibility(View.VISIBLE);
                            msProvider.getSpinner().setItems("");
                            msProvider.getSpinner().setText(getString(R.string.service));
                            msProvider.showFailure();
                        }
                    } else {

                        if (response.body().getList() != null && response.body().getList().size() > 0) {

                            services = response.body().getList();
                            msProvider.getSpinner().setItems(services);
                            msProvider.showSucess();
                        } else {
                            msProvider.getSpinner().setItems("");
                            msProvider.getSpinner().setText(getString(R.string.service));
                            msProvider.showSucess();
                        }


                    }

                }else {
                    msProvider.getSpinner().setItems("");
                    msProvider.getSpinner().setText(getString(R.string.service));
                    msProvider.showFailure();

                }


            }

            @Override
            public void onFailure(Call<ResponseGeneralLookup> call, Throwable t) {

                msProvider.getSpinner().setItems("");
                msProvider.getSpinner().setText(getString(R.string.service));
                msProvider.showFailure();

            }
        });


    }


    /**
     * on Click meathod to Send complain
     */
    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        if (!msProvider.isEnabled()) {
            msProvider.getSpinner().setError(getString(R.string.no_providers));
            return;
        }


        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError(getString(R.string.error_field_required));
            return;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError(getString(R.string.error_field_required));
            return;
        }

        DialogUtils.showDialog(this,true);

        if (getIntent().getIntExtra(Constants.KEY_COMPLAINTS_TYPE, Constants.KEY_COMPLAINTS_PROVIDER) == Constants.KEY_COMPLAINTS_SERVICE) {

            call=App.getInstance().getService().submitServiceComplaint(etTitle.getText().toString().trim(),
                    etDescription.getText().toString().trim(),
                    services.get(msProvider.getSpinner().getSelectedIndex()).getId(),
                    App.getInstance().getPrefManager().getCurrentUser().getCardId(),
                    "en");
                    call.enqueue(new Callback<ResponseGeneralLookup>() {
                @Override
                public void onResponse(Call<ResponseGeneralLookup> call, Response<ResponseGeneralLookup> response) {
                    DialogUtils.showDialog(ComplainsActivity.this,false);
                  //  Toast.makeText(ComplainsActivity.this, getString(R.string.complaint_successed), Toast.LENGTH_SHORT).show();
                    SuperToast.create(ComplainsActivity.this, getString(R.string.complaint_successed), 3000).show();

                    IntentManager.startActivity(ComplainsActivity.this, IntentManager.ACTIVITY_MAIN);

                }


                @Override
                public void onFailure(Call<ResponseGeneralLookup> call, Throwable t) {
                    DialogUtils.showDialog(ComplainsActivity.this,false);
                 //   Toast.makeText(ComplainsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    SuperToast.create(ComplainsActivity.this,getString(R.string.err_data_load_failed), 3000).show();

                    Log.e(TAG + " SubmitServiceComplaint", t.toString());
                }
            });

        } else {


            call=App.getInstance().getService().submitProviderComplaint(etTitle.getText().toString().trim(),
                    etDescription.getText().toString().trim(),
                    Constants.simpleProviders.get(msProvider.getSpinner().getSelectedIndex()).getId(),
                    App.getInstance().getPrefManager().getCurrentUser().getCardId(),
                    getAppLanguage());
            call.enqueue(new Callback<ResponseGeneralLookup>() {
                @Override
                public void onResponse(Call<ResponseGeneralLookup> call, Response<ResponseGeneralLookup> response) {
                    DialogUtils.showDialog(ComplainsActivity.this,false);
                 //   Toast.makeText(ComplainsActivity.this, getString(R.string.complaint_successed), Toast.LENGTH_SHORT).show();
                    SuperToast.create(ComplainsActivity.this, getString(R.string.complaint_successed), 3000).show();
                    IntentManager.startActivity(ComplainsActivity.this, IntentManager.ACTIVITY_MAIN);
                }

                @Override
                public void onFailure(Call<ResponseGeneralLookup> call, Throwable t) {

                 //   Toast.makeText(ComplainsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    SuperToast.create(ComplainsActivity.this,getString(R.string.err_data_load_failed), 3000).show();

                    Log.e(TAG + " SubmitServiceComplaint", t.toString());
                    DialogUtils.showDialog(ComplainsActivity.this,false);
                }
            });

        }

    }


    /**
     * update provider after Select
     * @param index
     */
    private void updateProviders(final String index) {
        if (Constants.providerTypesList != null ) {

msProvider.showLoading();
msProvider.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
    @Override
    public void onRetry() {
updateProviders("0");
    }
});
        call=App.getInstance().getService().getSimpleProviders(Constants.providerCities.get(msCity.getSpinner().getSelectedIndex()).getId()
                    ,
                    App.getInstance().getPrefManager().getCurrentUser().getDegree(),
                    Constants.providerTypesList.get(msType.getSpinner().getSelectedIndex()).getId(),
                    getAppLanguage(),
                    index,
                    "-1",
                    (msArea.isEnabled()) ? Constants.providerAreas.get(msArea.getSpinner().getSelectedIndex()).getId() : "-1",
                    Constants.KEY_COMPLAINTS_ID,"-1");
                    call.enqueue(new Callback<ResponseProvidersSimple>() {
                        @Override
                        public void onResponse(Call<ResponseProvidersSimple> call, Response<ResponseProvidersSimple> response) {

                            if (response.body() != null) {

                                Message message = response.body().getMessage();

                                if (message.getCode() != 1&&response.body().getList().size()==0) {

                                    if ((index.equals("0")) && (message.getDetails().equals(getString(R.string.error_no_data_available)))) {
                                        llNoResult.setVisibility(View.VISIBLE);
                                        msProvider.getSpinner().setText(R.string.error_no_data_available);
                                        msProvider.setEnabled(false);
                                        msProvider.showSucess();

                                    }else {
                                        msProvider.showFailure();
                                        msProvider.getSpinner().setText(getString(R.string.no_providers));
                                        msProvider.setEnabled(false);

                                    }
                                } else {

                                    if( response.body().getList().size() > 0) {
                                       // llNoResult.setVisibility(View.GONE);
                                        Constants.simpleProviders = response.body().getList();
                                        msProvider.getSpinner().setItems(Constants.simpleProviders);
                                        msProvider.getSpinner().setEnabled(true);
                                        msProvider.getSpinner().setError(null);
                                        msProvider.showSucess();
                                    }
                                    else {
                                        msProvider.getSpinner().setText(getString(R.string.no_providers));
                                        msProvider.setEnabled(false);
                                        msProvider.showSucess();

                                    }
                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<ResponseProvidersSimple> call, Throwable t) {
                            msProvider.showFailure();
                        }});
        }
    }


    /**
     * get provider type like doctor and hospital and cities like cairo ,alex
     */
    private void getCitiesAndProviderType() {
msType.showLoading();
msType.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
    @Override
    public void onRetry() {
        getCitiesAndProviderType();

    }
});
msCity.showLoading();
msCity.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
    @Override
    public void onRetry() {
        getCitiesAndProviderType();

    }
});

        call=App.getInstance().getService().getProviderTypes(getAppLanguage(), Constants.KEY_COMPLAINTS_ID);
        call.enqueue(new Callback<ResponseProviderTypes>() {
            @Override
            public void onResponse(Call<ResponseProviderTypes> call, Response<ResponseProviderTypes> response) {
                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (Constants.providerTypesList == null)
                        Constants.providerTypesList = new ArrayList<ProviderType>();

                    if (message.getCode() != 1){
                        msType.showFailure();


                    } else {

                        if (response.body().getList().size() > 0) {

                            Constants.providerTypesList = response.body().getList();

                            msType.getSpinner().setItems(Constants.providerTypesList);
                            msType.showSucess();

                            nestedCall=App.getInstance().getService().getCities(getAppLanguage(), Constants.KEY_COMPLAINTS_ID);
                            nestedCall.enqueue(new Callback<ResponseCities>() {
                                @Override
                                public void onResponse(Call<ResponseCities> call, Response<ResponseCities> response) {

                                    if (response.body() != null) {

                                        Message message = response.body().getMessage();

                                        if (Constants.providerCities == null)
                                            Constants.providerCities = new ArrayList<City>();

                                        if (message.getCode() != 1){

                                               msCity.showFailure();


                                        }



                                        else {
                                            if (response.body().getList().size() > 0) {
                                                Constants.providerCities = response.body().getList();
                                                msCity.getSpinner().setItems(Constants.providerCities);
                                                msCity.showSucess();
                                            }
                                        }
                                    }


                                }

                                @Override
                                public void onFailure(Call<ResponseCities> call, Throwable t) {
                                    msCity.showFailure();
                                }
                            });
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseProviderTypes> call, Throwable t) {
                msType.showFailure();
                msCity.showFailure();
                Constants.providerTypesList = new ArrayList<ProviderType>();

            }
        });

    }



    @Override
    public boolean canSwipeBack(int dir) {
        return true;
    }

    @Override
    public void onSwipeProcess(float percent) {
//        statusBar.setAlpha(1 - percent);
        container.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
    }

    @Override
    public void onSwipeFinish(int dir) {
//        statusBar.setAlpha(1 - percent);
        finishActivity(dir);
    }


    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
            onBackPressed();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){call.cancel();}
        if (nestedCall!=null){nestedCall.cancel();}
    }
}
