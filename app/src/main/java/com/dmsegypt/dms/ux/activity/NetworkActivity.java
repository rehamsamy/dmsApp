package com.dmsegypt.dms.ux.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.DoctorSpicific;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseCities;
import com.dmsegypt.dms.rest.model.Response.ResponseDoctorSpicific;
import com.dmsegypt.dms.rest.model.Response.ResponseProviderTypes;
import com.dmsegypt.dms.rest.model.Response.ResponseProviders;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.ProviderAdapter;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
import com.dmsegypt.dms.ux.custom_view.StateView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static butterknife.ButterKnife.findById;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class NetworkActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private static final String TAG ="NetworkActivity" ;

    @BindView(R.id.msCity)
    ProgressableSpinner msCity;
    @BindView(R.id.msArea)
    ProgressableSpinner msArea;
    @BindView(R.id.msType)
    ProgressableSpinner msType;
    @BindView(R.id.msSpicific)
    ProgressableSpinner msSpicific;

    @BindView(R.id.rvSearchResult)
    RecyclerView rvSearchResult;
    private ProviderAdapter providerAdapter;
    Call call;
    private Call nestedCall;
    Call retriveDataCall;
    private String qeuryString="";
    @BindView(R.id.state_view)
    StateView stateView;
    @BindString(R.string.label_select_city)
    String cityHint;
    @BindString(R.string.label_select_area)
    String areaHint;
    @BindString(R.string.label_select_type)
    String providerTypeHint;
    @BindString(R.string.label_select_pharm_Spefic)
    String pharmSpeificHint;

    //endregion




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        iniView();


    }

    void iniView(){
        Constants.providerCities = new ArrayList<City>();
        Constants.providerTypesList = new ArrayList<ProviderType>();
        Constants.doctorSpicificList = new ArrayList<DoctorSpicific>();
        Constants.providers=new ArrayList<>();
        rvSearchResult.setHasFixedSize(true);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        providerAdapter = new ProviderAdapter(R.layout.layout_provider_card, Constants.providers);
        rvSearchResult.setAdapter(providerAdapter);

        //select type spinner if type doctor spinner specification apear
        msType.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

stateView.setVisibility(View.GONE);
                rvSearchResult.setVisibility(View.GONE);

                //if type doctor Specification be visible
                if(msType.getSpinner().getText().toString().equals("Doctor")){
                    msSpicific.getSpinner().setHint(R.string.label_all);
                    msSpicific.setEnabled(true);
                    msSpicific.setVisibility(View.VISIBLE);
                    getDoctorSpecific();

                }else {
                    msSpicific.setVisibility(View.GONE);
                    msSpicific.setEnabled(false);
                }
            }
        });

        //spinner select area
        msArea.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {


            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                stateView.setVisibility(View.GONE);
                rvSearchResult.setVisibility(View.GONE);

            }
        });


        //search button



        if ((Constants.providerCities != null) && (Constants.providerCities.size() > 0))
            msCity.getSpinner().setItems(Constants.providerCities);
        if ((Constants.providerTypesList != null) && (Constants.providerTypesList.size() > 0))
            msType.getSpinner().setItems(Constants.providerTypesList);

        msCity.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {


            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                stateView.setVisibility(View.GONE);
                rvSearchResult.setVisibility(View.GONE);


                if (Constants.providerCities.get(position).getId().equals("-1")) {
                    msArea.getSpinner().setSelectedIndex(0);
                    msArea.getSpinner().setEnabled(false);
                }else if(position==0){
                    msArea.getSpinner().setSelectedIndex(0);
                    msArea.getSpinner().setEnabled(false);
                }
                else {
                    getAreas(position);
                }
            }
        });
        //endregion








        getProvider();

    }


    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_network;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_network;
    }










    @Override
    public void finishActivity(int dir) {
        onBackPressed();
    }







    void getAreas(final int position){

        msArea.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
            @Override
            public void onRetry() {
                getAreas(position);
            }
        });
       msArea.showLoading();
        call=  App.getInstance().getService().getAreas(Constants.providerCities.get(position).getId(),getAppLanguage(), Constants.KEY_SEARCH_ID);
        call.enqueue(new Callback<ResponseAreas>() {
            @Override
            public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().getCode() == 1) {
                        msArea.showSucess();
                        Constants.providerAreas=new ArrayList<>();
                        Constants.providerAreas.add(new AreaItem("",areaHint));
                        Constants.providerAreas.addAll(response.body().getList());
                        msArea.getSpinner().setItems(Constants.providerAreas);
                        msArea.getSpinner().setEnabled(true);
                    } else {
                        msArea.showFailure();
                    }
                }else {

                    msArea.showFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseAreas> call, Throwable t) {
                Log.e(TAG, "API getAreas Error " + t.toString());
                msArea.showFailure();

            }
        });

    }



    @OnClick(R.id.iBtnSearch)
    void doSearch(){
        onClickSearch();

    }

    //region Search
    public void onClickSearch() {
        if (msCity.getSpinner().getSelectedIndex()==0){
            Snackbar.make(findViewById(android.R.id.content),cityHint,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (msType.getSpinner().getSelectedIndex()==0){
            Snackbar.make(findViewById(android.R.id.content),providerTypeHint,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (msArea.getSpinner().getSelectedIndex()==0&&msArea.getSpinner().isEnabled()){
            Snackbar.make(findViewById(android.R.id.content),areaHint,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (msSpicific.getSpinner().getSelectedIndex()==0
                &&msSpicific.getSpinner().isEnabled()
                &&msSpicific.getVisibility()==View.VISIBLE){
            Snackbar.make(findViewById(android.R.id.content),pharmSpeificHint,Snackbar.LENGTH_SHORT).show();
            return;
        }

         updateProviders("0");
    }
    //endregion


    //region function get providers it take city id and area id and user degree and search type if Doctor (specific id)
    private void updateProviders(final String index) {
            stateView.setListener(new StateView.OnRetrListener() {
                @Override
                public void onRetry() {
                    updateProviders("0");
                }
            });



        if (index.equals("0")) {
            stateView.setVisibility(View.VISIBLE);
            stateView.showState(StateView.LOADING);
            rvSearchResult.setVisibility(View.GONE);
        }


        call= App.getInstance().getService().getProviders(
                Constants.providerCities.get(msCity.getSpinner().getSelectedIndex()).getId(),
                App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_DMS)?
                        "-1":App.getInstance().getPrefManager().getCurrentUser().getDegree(),
                Constants.providerTypesList.get(msType.getSpinner().getSelectedIndex()).getId(),
                getAppLanguage(),
                index,
                qeuryString.isEmpty() ? "-1" : qeuryString,
                (msArea.isEnabled()) ? Constants.providerAreas.get(msArea.getSpinner().getSelectedIndex()).getId() : "-1",
                Constants.KEY_SEARCH_ID
                ,(msSpicific.isEnabled()) ? Constants.doctorSpicificList.get(msSpicific.getSpinner().getSelectedIndex()).getId() : "-1");
        call.enqueue(new Callback<ResponseProviders>() {
            @Override
            public void onResponse(Call<ResponseProviders> call, Response<ResponseProviders> response) {
                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (response.body().getList() == null || response.body().getList().size() == 0) {
                        if (index.equals("0")) {
                            if(message.getCode()!=1 ) {
                                if(message.getDetails().equals("Can't connect to server")){
                                   updateProviders("0");
                                }else {
                                    stateView.showState(StateView.EMPTY);
                                    rvSearchResult.setVisibility(View.GONE);
                                }


                            }
                        } else if ((!index.equals("0"))) {
                            if((message.getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || message.getDetails().toString().trim().equals(R.string.error_no_data_branch_available))){
                                providerAdapter.setEnableLoadMore(false);
                            }

                        }else if(!index.equals("0") && (message.getDetails().equals("Can't connect to server"))){
                            updateProviders(index);
                        }
                    } else {
                        if (index.equals("0")) {
                            stateView.setVisibility(View.GONE);
                            //iconsearch.setVisibility(View.GONE);
                            rvSearchResult.setVisibility(View.VISIBLE);
                            Constants.providers.clear();
                            Constants.providers.addAll(response.body().getList());

                            providerAdapter.notifyDataSetChanged();
                            providerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    switch (view.getId()) {
                                        case R.id.ivNavigate:
                                            IntentManager.makeNavigate(NetworkActivity.this, Constants.providers.get(position).getAddress());
                                            break;
                                    }
                                }
                            });
                            providerAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    updateProviders(String.valueOf(providerAdapter.getItemCount() - 1));
                                }
                            }, rvSearchResult);

                            providerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    LinearLayout agentCardContainer = (LinearLayout) view.findViewById(R.id.agentCardContainer);
                                    ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                                    Intent intent = new Intent(NetworkActivity.this, ProviderActivity.class);
                                    intent.putExtra(ProviderActivity.KEY_PROVIDER_ACTIVITY_PROVIDER, position);

                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                        ActivityOptionsCompat options = ActivityOptionsCompat
                                                .makeScaleUpAnimation(
                                                        agentCardContainer,
                                                        (int) agentCardContainer.getX(), (int) agentCardContainer.getY(),
                                                        agentCardContainer.getMeasuredWidth(), agentCardContainer.getMeasuredHeight());
                                        ActivityCompat.startActivity(NetworkActivity.this, intent, options.toBundle());
                                    } else {
                                        ActivityOptionsCompat options = ActivityOptionsCompat
                                                .makeSceneTransitionAnimation(
                                                        NetworkActivity.this,
                                                        Pair.create((View) thumbnail, getString(R.string.transition_photo_image)),
                                                        Pair.create((View) agentCardContainer, getString(R.string.transition_photo_background)));
                                        ActivityCompat.startActivity(NetworkActivity.this, intent, options.toBundle());
                                    }
                                }
                            });
                        } else {
                            if ((response.body().getList().size() % 20) != 0)
                                providerAdapter.setEnableLoadMore(false);
                            Constants.providers.addAll(response.body().getList());
                        }
                        //    providerAdapter = new_image ProviderAdapter(R.layout.layout_provider_card, Constants.providers);
                        //   rvSearchResult.setAdapter(providerAdapter);
                        providerAdapter.notifyDataSetChanged();
                    }
                }

                if (response.body().getList().size() != 0){
                    providerAdapter.loadMoreComplete();
                }
            }

            @Override
            public void onFailure(Call<ResponseProviders> call, Throwable t) {
                if (index.equals("0"))
                    stateView.showState(StateView.NO_CONNECTION);


                Log.e(TAG , t.toString());
            }
        });
    }
//endregion



    //region getDoctor Specific
    private void getDoctorSpecific(){
        msSpicific.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
            @Override
            public void onRetry() {
                getDoctorSpecific();

            }
        });
        msSpicific.showLoading();
                call=App.getInstance().getService().getDoctorSpecific(getAppLanguage(),Constants.KEY_SEARCH_ID);
        call.enqueue(new Callback<ResponseDoctorSpicific>() {
            @Override
            public void onResponse(Call<ResponseDoctorSpicific> call, Response<ResponseDoctorSpicific> responseSpicifci) {
                if (responseSpicifci.body() != null) {
                    Message message = responseSpicifci.body().getMessage();
                    if (message.getCode() != 1)
                    {
                        msSpicific.showFailure();

                    }else {
                        msSpicific.showSucess();
                        Constants.doctorSpicificList.add(new DoctorSpicific("",pharmSpeificHint));

                        Constants.doctorSpicificList .addAll(responseSpicifci.body().getList());


                        msSpicific.getSpinner().setItems(Constants.doctorSpicificList);
                        DialogUtils.showDialog(NetworkActivity.this,false);
                    }
                }else {

                    msSpicific.showFailure();

                }

            }

            @Override
            public void onFailure(Call<ResponseDoctorSpicific> call, Throwable throwable) {
                msSpicific.showFailure();

            }
        });

    }
    //endregion

    //region get provider type like hospital and doctor
    private void getProvider() {
        msType.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
            @Override
            public void onRetry() {
                getProvider();

            }
        });
        msType.showLoading();
        retriveDataCall=App.getInstance().getService().getProviderTypes(getAppLanguage(), Constants.KEY_SEARCH_ID);
        retriveDataCall.enqueue(new Callback<ResponseProviderTypes>() {
            @Override
            public void onResponse(Call<ResponseProviderTypes> call, Response<ResponseProviderTypes> response) {
                if (response.body() != null) {
                    Message message = response.body().getMessage();

                    if (message.getCode() != 1) {

                        msType.showFailure();
                    }
                    else {
                        msType.showSucess();
                        Constants.providerTypesList.add(new ProviderType("",providerTypeHint));

                        Constants.providerTypesList.addAll(response.body().getList());

                        msType.getSpinner().setItems(Constants.providerTypesList);
                        DialogUtils.showDialog(NetworkActivity.this,false);
                        getCities();

                    }
                }else {
                    msType.showFailure();


                }
            }

            @Override
            public void onFailure(Call<ResponseProviderTypes> call, Throwable t) {
                msType.showFailure();

            }
        });

    }

    //endregion



    //region  get Provider Cities
    public void getCities(){
    msCity.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
        @Override
        public void onRetry() {
            getProvider();

        }
    });
        msCity.showLoading();

        nestedCall=App.getInstance().getService().getCities(getAppLanguage(), Constants.KEY_SEARCH_ID);
        nestedCall.enqueue(new Callback<ResponseCities>() {
            @Override
            public void onResponse(Call<ResponseCities> call, Response<ResponseCities> response) {
                DialogUtils.showDialog(NetworkActivity.this,false);
                if (response.body() != null) {
                    Message message = response.body().getMessage();
                    if (message.getCode() != 1) {
                        msCity.showFailure();

                    }else {
                        msCity.showSucess();
                        Constants.providerCities.add(new City("",cityHint));
                        Constants.providerCities .addAll(response.body().getList());
                        msCity.getSpinner().setItems(Constants.providerCities);

                    }
                }else {
                    msCity.showFailure();

                }
            }

            @Override
            public void onFailure(Call<ResponseCities> call, Throwable t) {
                msCity.showFailure();

            }
        });
    }
//endregion


    //Destroy Call to avoid leak Memory
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call!=null){call.cancel();}
        if (nestedCall!=null){nestedCall.cancel();}
        if (retriveDataCall!=null)retriveDataCall.cancel();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
       // searchView.clearFocus();
        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.color_white));
        searchAutoComplete.setHint(R.string.hint_search_netwrok);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        searchView.setLayoutParams(params);

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
        this.qeuryString=query;
        updateProviders("0");
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }








}
