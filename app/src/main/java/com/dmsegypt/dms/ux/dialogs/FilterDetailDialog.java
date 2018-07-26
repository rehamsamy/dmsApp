package com.dmsegypt.dms.ux.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.dmsegypt.dms.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amr on 05/12/2017.
 */

public class FilterDetailDialog extends DialogFragment {
    public static final String EXTRA_CARD="card";
    public static final String EXTRA_CLAIM="claim";
    public static final String EXTRA_MED="med";


    @BindView(R.id.Card_id_et)
    EditText cardEdit;
    @BindView(R.id.Claim_no_et)
    EditText claimEdit;
    @BindView(R.id.no_medicine_et)
    EditText medicineEdit;
    OnFilterApplyListener listener;



    public static FilterDetailDialog newInstance(String card_id,String claim,String medicine_no) {

        Bundle args = new Bundle();
        args.putString(EXTRA_CARD,card_id);
        args.putString(EXTRA_CLAIM,claim);
        args.putString(EXTRA_MED,medicine_no);
        FilterDetailDialog fragment = new FilterDetailDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_filter,container,false);
        ButterKnife.bind(this,view);
        String card=getArguments().getString(EXTRA_CARD);
        String claim=getArguments().getString(EXTRA_CLAIM);
        String med=getArguments().getString(EXTRA_MED);
        cardEdit.setText(card.equals("-1")?"":card);
        claimEdit.setText(claim.equals("-1")?"":claim);
        medicineEdit.setText(med.equals("-1")?"":med);
        return view;

    }
    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    @OnClick(R.id.filter_btn)
    void onFilterDetail(){
        String card_id=cardEdit.getText().toString().isEmpty()?"-1":cardEdit.getText().toString();
        String claim_no=claimEdit.getText().toString().isEmpty()?"-1":claimEdit.getText().toString();
        String medicine=medicineEdit.getText().toString().isEmpty()?"-1":medicineEdit.getText().toString();
        if (listener!=null){
            listener.onFilterApply(card_id,claim_no,medicine);
        }

        dismiss();
    }

    @OnClick(R.id.cancel_btn)
    void onCancelDialog(){
        dismiss();
    }

    public interface OnFilterApplyListener{
        void onFilterApply(String card_id, String claim_no, String medicine_no);
    }


    public void setListener(OnFilterApplyListener listener) {
        this.listener = listener;
    }
}
