package com.dmsegypt.dms.ux.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmsegypt.dms.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amr on 27/11/2017.
 */

public class PharmcySearchDialog extends DialogFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pharmacy_et)
    EditText pharmcyEdit;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.emtpy_tv)
    TextView emptyTv;
    public static final int STATE_LOADING=0;
    public static final int STATE_LOADED=1;
    public static final int STATE_EMPTY=2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_pharmacy_search,container,false);
        ButterKnife.bind(this,view);
          iniView();
        return  view;
    }
    void iniView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        showState(STATE_LOADING);

    }
    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
    public void searchPharmacy(){

    }


    void showState(int isShowing){
        progressBar.setVisibility(isShowing==0?View.VISIBLE:View.GONE);
        recyclerView.setVisibility(isShowing==1?View.VISIBLE:View.GONE);
        emptyTv.setVisibility(isShowing==2?View.VISIBLE:View.GONE);
    }



}
