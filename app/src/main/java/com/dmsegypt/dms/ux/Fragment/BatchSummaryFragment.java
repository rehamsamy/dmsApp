package com.dmsegypt.dms.ux.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.BatchPharmacy;
import com.dmsegypt.dms.rest.model.BatchSummary;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseBatchPharmacy;
import com.dmsegypt.dms.rest.model.Response.ResponseBatchSummary;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.activity.AddMemberActivity;
import com.dmsegypt.dms.ux.adapter.SummaryBatchAdpater;
import com.dmsegypt.dms.ux.dialogs.PharmcySearchDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 27/11/2017.
 */

public class BatchSummaryFragment extends Fragment {
    // region Constants
    private static final int STATE_LOADING=0;
    private static final int STATE_SUCESS=1;
    private static final int STATE_EMPTY=2;
    private static final int STATE_FAIL=3;
    //endregion
    //region UI Views

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.batch_sp)
    EditText batchEdit;
    @BindView(R.id.check_sp)
    MaterialSpinner checkSp;
    @BindArray(R.array.check_status)
    String[]state_names;
    @BindView(R.id.horizontalView)
    HorizontalScrollView horizontalScrollView;
    SummaryBatchAdpater batchAdapter;
    List<BatchSummary>summaryList;
    @BindView(R.id.progress_bar)
    ProgressBar loadingBar;
    @BindView(R.id.emtpy_tv)
    TextView emptyView;

    @BindView(R.id.batch_num)
    TextView tvbatch_num;

    @BindView(R.id.filter_batch_num)
    EditText filter_batch_num;


    @BindView(R.id.mpharmacy)
    MaterialSpinner mpharmacy;

    @BindView(R.id.lineardmsspinner)
    LinearLayout lineardmsspinner;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.cancel_filter_batchno)
    ImageButton cancel_filter_batchno;

    @BindView(R.id.btn_filter_batchno)
    ImageButton btn_filter_batchno;


    @BindView(R.id.header_linear_batchno)
    LinearLayout header_linear_batchno;

    @BindView(R.id.header_linear_etbatchno)
    LinearLayout header_linear_etbatchno;




    private Call call;
    private int Index=0;
    String language="En";
    boolean isDMSUser;
    boolean isProviderUser;

    //endregion


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language= LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        summaryList=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_batch_summary,container,false);
        ButterKnife.bind(this,view);
        iniView();

        return view;
    }

    void iniView()
    {


        isProviderUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER);
        isDMSUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);
        if(isProviderUser){
            lineardmsspinner.setVisibility(View.VISIBLE);
            et_search.setVisibility(View.GONE);
            getAllbatchpharmacy(App.getInstance().getPrefManager().getCurrentUser().getPrvId());
        }else if(isDMSUser){
            lineardmsspinner.setVisibility(View.VISIBLE);
        }
        checkSp.setItems(state_names);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        batchAdapter=new SummaryBatchAdpater(R.layout.list_summary_row,summaryList);
        recyclerView.setAdapter(batchAdapter);



        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT=2;
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(motionEvent.getRawX() >= (et_search.getRight() -    et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        try {
                            if(et_search.getText().toString().trim().equals("")){
                                et_search.setError("Empty Field");
                            }else {
                                getAllbatchpharmacy(et_search.getText().toString().trim());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        return true;
                    }
                }

                return false;
            }
        });
        //////////////////////////////////////////////////////////////////////

        filter_batch_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    batchAdapter=new SummaryBatchAdpater(R.layout.list_summary_row,summaryList);
                    recyclerView.setAdapter(batchAdapter);
                }else {
                    filterSearch(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        cancel_filter_batchno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_batchno.setVisibility(View.VISIBLE);
                header_linear_etbatchno.setVisibility(View.GONE);
                filter_batch_num.setText("");
            }
        });


        btn_filter_batchno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_batchno.setVisibility(View.GONE);
                header_linear_etbatchno.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filterSearch(CharSequence charSequence) {
        if(summaryList != null && summaryList.size() != 0){
            List<BatchSummary>filterResult = new ArrayList<>();
            for(int i=0;i<summaryList.size();i++){
                if(summaryList.get(i).getBatch_no().equals(charSequence.toString())){
                    filterResult.add(summaryList.get(i));
                }
            }
            if(filterResult != null) {
                batchAdapter=new SummaryBatchAdpater(R.layout.list_summary_row,filterResult);
                recyclerView.setAdapter(batchAdapter);
            }
        }
    }

    @OnClick(R.id.search_button)
    void doSearch(){
        Index=0;
        summaryList.clear();
        showState(STATE_LOADING);
        getBatchDetails("0");
    }


/*
    @OnTouch({R.id.batch_num,R.id.filter_batch_num})
    public boolean dofilter(MotionEvent event){
        final int DRAWABLE_RIGHT=2;
        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            if(event.getRawX() >= (tvbatch_num.getRight() -    tvbatch_num.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                try {
                    tvbatch_num.setVisibility(View.GONE);
                    filter_batch_num.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();

                }

                return true;
            }

            if(event.getRawX() >= (filter_batch_num.getRight() -    filter_batch_num.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                try {
                    tvbatch_num.setVisibility(View.VISIBLE);
                    filter_batch_num.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }
        }
        return false;
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        if(Constants.PharmacyBatchList != null && Constants.PharmacyBatchList.size() !=0){
            mpharmacy.setItems(Constants.PharmacyBatchList);

        }
    }

    private void getAllbatchpharmacy(String stringsearch) {
        DialogUtils.showDialog(getActivity(),true);
        App.getInstance().getService().getBatchPharmcy(LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN,stringsearch,"0").enqueue(new Callback<ResponseBatchPharmacy>() {
            @Override
            public void onResponse(Call<ResponseBatchPharmacy> call, Response<ResponseBatchPharmacy> response) {
                DialogUtils.showDialog(getActivity(),false);
                if(response != null && response.body().getMessage().getCode() != 0){
                    if(response.body().getList().size() != 0){
                        BatchPharmacy nothingoption=new BatchPharmacy("","Select no Thing");
                        Constants.PharmacyBatchList=new ArrayList<>();
                        Constants.PharmacyBatchList.add(nothingoption);
                        Constants.PharmacyBatchList.addAll(response.body().getList());
                        mpharmacy.setEnabled(true);
                        mpharmacy.setItems(Constants.PharmacyBatchList);
                        Snackbar.make(getActivity().findViewById(android.R.id.content),response.body().getMessage().getDetails(),Snackbar.LENGTH_LONG).show();

                    }
                }else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),response.body().getMessage().getDetails(),Snackbar.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBatchPharmacy> call, Throwable throwable) {
                DialogUtils.showDialog(getActivity(),false);
                Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();

            }
        });





    }

    void showState(int state){
        loadingBar.setVisibility(state==0?View.VISIBLE:View.GONE);
        horizontalScrollView.setVisibility(state==1?View.VISIBLE:View.GONE);
        emptyView.setVisibility(state==2?View.VISIBLE:View.GONE);
    }
    void getBatchDetails(final String rowIndex){
        //card Id at provider User is his pharmacy code
        String pharmCode="";
        if(isDMSUser){
            if(Constants.PharmacyBatchList != null && Constants.PharmacyBatchList.size() != 0){
                if(mpharmacy.getSelectedIndex() == 0){
                    pharmCode="-1";
                }else {
                    pharmCode = Constants.PharmacyBatchList.get(mpharmacy.getSelectedIndex()).getId();
                }
            }else {
                pharmCode="-1";
            }

        }else if(isProviderUser){
            pharmCode =App.getInstance().getPrefManager().getCurrentUser().getPrvId();
        }

        String batchState=checkSp.getSelectedIndex()==0?"-1":state_names[checkSp.getSelectedIndex()];
        String batchNum=batchEdit.getText().toString().isEmpty()?"-1":batchEdit.getText().toString();

        call=  App.getInstance().getService().getBatchSummary(language,pharmCode,batchState,batchNum,rowIndex);
        call.enqueue(new Callback<ResponseBatchSummary>() {
            @Override
            public void onResponse(Call<ResponseBatchSummary> call, Response<ResponseBatchSummary> response) {

                Message message=response.body().getMessage();
                List<BatchSummary> list=response.body().getList();
                if (message.getCode()==1) {
                    if(list.isEmpty()){
                        if (rowIndex.equals("0"))
                            showState(STATE_EMPTY);
                    }
                    else {

                        showState(STATE_SUCESS);
                        summaryList.addAll(list);
                        batchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {
                                getBatchDetails(summaryList.size()+"");

                            }
                        },recyclerView);
                        batchAdapter.notifyDataSetChanged();

                    }

                }else {
                    if (rowIndex.equals("0")){
                        showState(STATE_FAIL);
                    }
                    batchAdapter.setEnableLoadMore(false);
                    Snackbar.make(getActivity().findViewById(android.R.id.content),message.getDetails(),Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBatchSummary> call, Throwable t) {
                if (rowIndex.equals("0")){
                    showState(STATE_FAIL);
                }
                Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }

}
