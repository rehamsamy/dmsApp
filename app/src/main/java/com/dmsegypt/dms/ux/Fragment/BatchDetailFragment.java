package com.dmsegypt.dms.ux.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.BatchDetail;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseBatchDetail;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.adapter.DetailBatchAdpater;
import com.dmsegypt.dms.ux.dialogs.FilterDetailDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 27/11/2017.
 */

public class BatchDetailFragment extends Fragment implements FilterDetailDialog.OnFilterApplyListener {

    private static final int STATE_LOADING=0;
    private static final int STATE_SUCESS=1;
    private static final int STATE_EMPTY=2;
    private static final int STATE_FAIL=3;

    @BindView(R.id.design_bottom_sheet)
    View bottomSheet;
    @BindView(R.id.show_btn)
    ImageView showBtn;
    private BottomSheetBehavior<View> behavior;
    @BindView(R.id.batch_sp)
    EditText batchEdit;
    @BindView(R.id.check_sp)
    MaterialSpinner checkSp;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String language="En";
    @BindArray(R.array.state_name)
    String[]state_names;
    @BindArray(R.array.state_code)
    String[]state_code;
    @BindView(R.id.progress_bar)
    ProgressBar loadingBar;
    @BindView(R.id.emtpy_tv)
    TextView emptyView;
    @BindView(R.id.claimamnt_tv)
    TextView claimTv;
    @BindView(R.id.total_row_tv)
    TextView totalrowTv;
    @BindView(R.id.diffamnt_tv)
    TextView differentTv;
    @BindView(R.id.sysamount_tv)
    TextView systemTv;
    @BindView(R.id.agreemnet_tv)
    TextView agreemnetTv;
    @BindView(R.id.medicine_count_tv)
            TextView medicineCountTv;
    @BindView(R.id.horizontalView)
    HorizontalScrollView horizontalScrollView;
    DetailBatchAdpater batchAdapter;
    List<BatchDetail>detailList;
    private Call call;
     String batch_code="-1";
     String pharm_code="-1";
     String reason="-1";
     String card_id="-1";
    String claim_no="-1";
    String medicine_no="-1";
    @BindView(R.id.batch_no_tv)
    TextView tvbatch_num;

    @BindView(R.id.filter_batch_num)
    EditText filter_batch_num;

    @BindView(R.id.cancel_filter_claim)
    ImageButton cancel_filter_claim;

    @BindView(R.id.btn_filter_claim)
    ImageButton btn_filter_claim;


    @BindView(R.id.header_linear_claim)
    LinearLayout header_linear_claim;

    @BindView(R.id.header_linear_etclaim)
    LinearLayout header_linear_etclaim;



    @BindView(R.id.cancel_filter_disc)
    ImageButton cancel_filter_disc;

    @BindView(R.id.btn_filter_disc)
    ImageButton btn_filter_disc;


    @BindView(R.id.header_linear_disc)
    LinearLayout header_linear_disc;

    @BindView(R.id.header_linear_etdisc)
    LinearLayout header_linear_etdisc;




    @BindView(R.id.cancel_filter_cardid)
    ImageButton cancel_filter_cardid;

    @BindView(R.id.btn_filter_cardid)
    ImageButton btn_filter_cardid;


    @BindView(R.id.header_linear_cardid)
    LinearLayout header_linear_cardid;

    @BindView(R.id.header_linear_etcardid)
    LinearLayout header_linear_etcardid;


    @BindView(R.id.cancel_filter_batchno)
    ImageButton cancel_filter_batchno;

    @BindView(R.id.btn_filter_batchno)
    ImageButton btn_filter_batchno;


    @BindView(R.id.header_linear_batchno)
    LinearLayout header_linear_batchno;

    @BindView(R.id.header_linear_etbatchno)
    LinearLayout header_linear_etbatchno;


    @BindView(R.id.Card_id_tv)
    TextView tvCardId;

    @BindView(R.id.filter_card_id)
    EditText filter_card_id;

    @BindView(R.id.Claim_no)
    TextView tvclaim_no;

    @BindView(R.id.filter_claim_no)
    EditText filter_claim_no;

    @BindView(R.id.Disc_code)
    TextView tvdisc_code;

    @BindView(R.id.filter_disc_code)
    EditText filter_disc_code;

    @BindView(R.id.tv_number)
    TextView tv_number;


    @BindView(R.id.search_button)
    ImageButton search_button;


    ArrayList<BatchDetail>reasonList;
    @BindView(R.id.reason_container)
    LinearLayout reasonContainer;
    private int mTotalDy=0;
    boolean isDMSUser;
    boolean isProviderUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        language= LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        detailList=new ArrayList<>();
        reasonList=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_batch_detail,container,false);
        ButterKnife.bind(this,view);
        iniView();
        return view;
    }
    private void iniView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,detailList);
        recyclerView.setAdapter(batchAdapter);
        batchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getBatchDetails(pharm_code,reason,batch_code,card_id,claim_no,medicine_no,detailList.size()+"");
            }
        },recyclerView);
        checkSp.setItems(state_names);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(Integer.MAX_VALUE)) {
                    bottomSheet.setVisibility(View.GONE);

                } else if(recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING  && bottomSheet.getVisibility() == View.GONE) {
                    bottomSheet.setVisibility(View.VISIBLE);
                }

            }
        });



        batchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_button.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    search_button.setVisibility(View.GONE);
                }else {
                    search_button.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


       filter_batch_num.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().equals("")){
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,detailList);
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





        ///////////////////////////////////////////////////////////// Card ID
        filter_card_id.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().equals("")){
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,detailList);
                recyclerView.setAdapter(batchAdapter);
            }else {
                filterSearchcardId(charSequence);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });

        cancel_filter_cardid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_cardid.setVisibility(View.VISIBLE);
                header_linear_etcardid.setVisibility(View.GONE);
                filter_card_id.setText("");
            }
        });


        btn_filter_cardid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_cardid.setVisibility(View.GONE);
                header_linear_etcardid.setVisibility(View.VISIBLE);
            }
        });





        /////////////////////////////////////////////////////////////claim num
        filter_claim_no.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().equals("")){
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,detailList);
                recyclerView.setAdapter(batchAdapter);
            }else {
                filterSearchclaimno(charSequence);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });


        cancel_filter_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_claim.setVisibility(View.VISIBLE);
                header_linear_etclaim.setVisibility(View.GONE);
                filter_claim_no.setText("");
            }
        });


        btn_filter_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_claim.setVisibility(View.GONE);
                header_linear_etclaim.setVisibility(View.VISIBLE);
            }
        });




    /////////////////////////////////////////////////////////// disk code
        filter_disc_code.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().equals("")){
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,detailList);
                recyclerView.setAdapter(batchAdapter);
            }else {
                filterSearchdisc_code(charSequence);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });

        cancel_filter_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_disc.setVisibility(View.VISIBLE);
                header_linear_etdisc.setVisibility(View.GONE);
                filter_disc_code.setText("");
            }
        });


        btn_filter_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header_linear_disc.setVisibility(View.GONE);
                header_linear_etdisc.setVisibility(View.VISIBLE);
            }
        });




}

    private void filterSearchdisc_code(CharSequence charSequence) {
        if(detailList != null && detailList.size() != 0){
            List<BatchDetail>filterResult = new ArrayList<>();
            for(int i=0;i<detailList.size();i++){
                if(detailList.get(i).getDisc_code().equals(charSequence.toString())){
                    filterResult.add(detailList.get(i));
                }
            }
            if(filterResult != null) {
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,filterResult);
                recyclerView.setAdapter(batchAdapter);
            }
        }
    }

    private void filterSearchclaimno(CharSequence charSequence) {
        if(detailList != null && detailList.size() != 0){
            List<BatchDetail>filterResult = new ArrayList<>();
            for(int i=0;i<detailList.size();i++){
                if(detailList.get(i).getClaim_no().equals(charSequence.toString())){
                    filterResult.add(detailList.get(i));
                }
            }
            if(filterResult != null) {
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,filterResult);
                recyclerView.setAdapter(batchAdapter);
            }
        }
    }


    private void filterSearch(CharSequence charSequence) {
        if(detailList != null && detailList.size() != 0){
            List<BatchDetail>filterResult = new ArrayList<>();
            for(int i=0;i<detailList.size();i++){
                if(detailList.get(i).getBatch_no().equals(charSequence.toString())){
                    filterResult.add(detailList.get(i));
                }
            }
            if(filterResult != null) {
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,filterResult);
                recyclerView.setAdapter(batchAdapter);
            }
        }
    }

    private void filterSearchcardId(CharSequence charSequence) {
        if(detailList != null && detailList.size() != 0){
            List<BatchDetail>filterResult = new ArrayList<>();
            for(int i=0;i<detailList.size();i++){
                if(detailList.get(i).getCard_id().equals(charSequence.toString())){
                    filterResult.add(detailList.get(i));
                }
            }
            if(filterResult != null) {
                batchAdapter=new DetailBatchAdpater(R.layout.list_batch_row_item,filterResult);
                recyclerView.setAdapter(batchAdapter);
            }
        }
    }

    @OnClick(R.id.search_button)
      void doSearch(){
        isProviderUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER);
        isDMSUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);

        if(isDMSUser){
            pharm_code ="-1";
        }else if(isProviderUser){
            pharm_code =App.getInstance().getPrefManager().getUser().getCardId() ;
        }

        reason=checkSp.getSelectedIndex()==0?"-1":state_code[checkSp.getSelectedIndex()]+"";
        batch_code=batchEdit.getText().toString().isEmpty()?"-1":batchEdit.getText()+"";
        card_id="-1";
        claim_no="-1";
        medicine_no="-1";
        detailList.clear();
        showState(STATE_LOADING);
        getBatchDetails(pharm_code,reason,batch_code,card_id,claim_no,medicine_no,"0");
     }

    // show sum of claim and differnet amount
    @OnClick(R.id.show_btn)
    void showDetail(){
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            showBtn.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);

        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showBtn.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);

        }
    }
    void showState(int state){
        loadingBar.setVisibility(state==0?View.VISIBLE:View.GONE);
        horizontalScrollView.setVisibility(state==1?View.VISIBLE:View.GONE);
        emptyView.setVisibility(state==2?View.VISIBLE:View.GONE);
    }
    void getBatchDetails(String pharmCode,String batchState,String batchNum,String card_id,String claim,String medicine,final String rowIndex){
        //bottomSheet.setVisibility(View.GONE);
      call=App.getInstance().getService()
              .getBatchDetails(language,pharmCode,batchState,batchNum,card_id,claim,medicine,rowIndex);
                call.enqueue(new Callback<ResponseBatchDetail>() {
            @Override
            public void onResponse(Call<ResponseBatchDetail> call, Response<ResponseBatchDetail> response) {
                //bottomSheet.setVisibility(View.VISIBLE);
                Message message=response.body().getMessage();
                List<BatchDetail> list=response.body().getList();
                if (message.getCode()==1) {
                        if(list.isEmpty()){
                            if (rowIndex.equals("0"))
                                showState(STATE_EMPTY);
                        }
                        else {

                            if (rowIndex.equals("0"))
                            {
                                claimTv.setText(response.body().getSumclaimamt());
                                differentTv.setText(response.body().getSumdifferenceamt());
                                systemTv.setText(response.body().getSumsystemamt());
                                agreemnetTv.setText(response.body().getCountdiscclaimno());
                                medicineCountTv.setText(response.body().getCountdiscmedcode());
                                totalrowTv.setText(response.body().getCountbatchno());

                            }else {

                                if ((response.body().getList().size() % 20) != 0){
                                    batchAdapter.setEnableLoadMore(false);
                                }else {
                                    batchAdapter.loadMoreComplete();
                                }


                            }
                            addReasonToDetail(list);
                            showState(STATE_SUCESS);
                            detailList.addAll(list);
                            batchAdapter.notifyDataSetChanged();


                        }

                }else {
                    if (rowIndex.equals("0")){
                  //      bottomSheet.setVisibility(View.GONE);
                        showState(STATE_FAIL);
                    }
                    batchAdapter.loadMoreComplete();

                    Snackbar.make(getActivity().findViewById(android.R.id.content),message.getDetails(),Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBatchDetail> call, Throwable t) {
                if (rowIndex.equals("0")){
                    //bottomSheet.setVisibility(View.GONE);
                    showState(STATE_FAIL);
                }else {
                    //bottomSheet.setVisibility(View.VISIBLE);
                    batchAdapter.loadMoreComplete();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_filter_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId()==R.id.action_filter){
           FilterDetailDialog detailDialog= FilterDetailDialog.newInstance(card_id,claim_no,medicine_no);
           detailDialog.setListener(this);
           detailDialog.show(getActivity().getSupportFragmentManager(),"ddddd");
          return  true;
       }
        return false;
    }

    @Override
    public void onFilterApply(String card_id, String claim_no, String medicine_no) {
        this.medicine_no=medicine_no;
        this.card_id=card_id;
        this.claim_no=claim_no;
        detailList.clear();
        showState(STATE_LOADING);
        getBatchDetails(pharm_code,reason,batch_code,card_id,claim_no,medicine_no,"0");


    }

 void addReasonToDetail(List<BatchDetail> batchDetails){
    for (BatchDetail detail:batchDetails){
        if (!reasonList.contains(detail)){
            reasonList.add(detail);
        }
    }
    reasonContainer.removeAllViews();
    for (BatchDetail batch:reasonList){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.row_reason,null);
        TextView titleTv=(TextView)view.findViewById(R.id.title_tv);
        TextView descTv=(TextView)view.findViewById(R.id.desc_tv);
        titleTv.setText(batch.getGroup_name()+"");
        descTv.setText(batch.getSum_differenceamt_reason()+"");
        reasonContainer.addView(view);
    }


}
}
