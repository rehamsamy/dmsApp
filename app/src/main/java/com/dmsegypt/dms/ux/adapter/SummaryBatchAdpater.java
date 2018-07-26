package com.dmsegypt.dms.ux.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.BatchSummary;
import com.dmsegypt.dms.ux.dialogs.TextViewDialog;

import java.util.List;

/**
 * Created by amr on 03/12/2017.
 */

public class SummaryBatchAdpater extends BaseQuickAdapter<BatchSummary,BaseViewHolder> {
    public SummaryBatchAdpater(int layoutResId, List<BatchSummary> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
    }

    @Override

    protected void convert(BaseViewHolder helper, final BatchSummary item) {

        helper.getView(R.id.batch_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getBatch_no());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });


        helper.getView(R.id.prv_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getPrv_name());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });

        helper.getView(R.id.System_amt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getSystem_amt());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });


        if (helper.getAdapterPosition() % 2 == 0) {
            helper.itemView.setBackgroundColor(helper.itemView.getResources().getColor(R.color.colorPrimaryTrams));
            helper.setText(R.id.row_no_tv, helper.getAdapterPosition() + 1 + "");
            helper.setText(R.id.prv_code, item.getPrv_no());
            helper.setTextColor(R.id.prv_code, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.batch_num, item.getBatch_no());
            helper.setTextColor(R.id.batch_num, helper.itemView.getResources().getColor(R.color.colormain));

            helper.setText(R.id.prv_name, item.getPrv_name());
            helper.setTextColor(R.id.prv_name, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.System_amt, item.getSystem_amt());
            helper.setTextColor(R.id.System_amt, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.check_amount, item.getCheck_amt());
            helper.setTextColor(R.id.check_amount, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.check_status, item.getCheck_status());
            helper.setTextColor(R.id.check_status, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.system_no, item.getSystem_no());
            helper.setTextColor(R.id.system_no, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.data_statement, item.getDate_statment());
            helper.setTextColor(R.id.data_statement, helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.manual_no, item.getManual_no());
            helper.setTextColor(R.id.manual_no, helper.itemView.getResources().getColor(R.color.colormain));
            } else {
            helper.itemView.setBackgroundColor(helper.itemView.getResources().getColor(R.color.color_batch));
            helper.setText(R.id.row_no_tv, helper.getAdapterPosition() + 1 + "");
            helper.setText(R.id.prv_code, item.getPrv_no());
            helper.setTextColor(R.id.prv_code, helper.itemView.getResources().getColor(R.color.color_white));

            helper.setText(R.id.batch_num, item.getBatch_no());
            helper.setTextColor(R.id.batch_num, helper.itemView.getResources().getColor(R.color.colormain));

            helper.setText(R.id.prv_name, item.getPrv_name());
            helper.setTextColor(R.id.prv_name, helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.System_amt, item.getSystem_amt());
            helper.setTextColor(R.id.System_amt, helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.check_amount, item.getCheck_amt());
            helper.setTextColor(R.id.check_amount, helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.check_status, item.getCheck_status());
            helper.setTextColor(R.id.check_status, helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.system_no, item.getSystem_no());
            helper.setTextColor(R.id.system_no, helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.data_statement, item.getDate_statment());
            helper.setTextColor(R.id.data_statement, helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.manual_no, item.getManual_no());
            helper.setTextColor(R.id.manual_no, helper.itemView.getResources().getColor(R.color.color_white));


        }


    }
}