package com.dmsegypt.dms.ux.adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.BatchDetail;
import com.dmsegypt.dms.ux.dialogs.TextViewDialog;

import java.util.List;

/**
 * Created by amr on 29/11/2017.
 */

public class DetailBatchAdpater  extends BaseQuickAdapter<BatchDetail,BaseViewHolder> {
    public DetailBatchAdpater(int layoutResId, List<BatchDetail> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
    }

    @Override

    protected void convert(BaseViewHolder helper, final BatchDetail item) {

        helper.getView(R.id.Prv_branch_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getPrv_branch_name());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });

        helper.getView(R.id.Card_id_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getCard_id());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });

        helper.getView(R.id.Claim_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getClaim_no());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });

        helper.getView(R.id.Med_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getMed_name());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });

        helper.getView(R.id.Med_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getMed_code());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });

        helper.getView(R.id.Prv_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getPrv_name());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });



      /*  helper.getView(R.id.Prv_name).setOnClickListener(new_image View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewDialog textViewDialog=TextViewDialog.newInstance(item.getPrv_name());
                textViewDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "text_dialog");
            }
        });*/


        if (helper.getAdapterPosition()%2==0){
            helper.itemView.setBackgroundColor(helper.itemView.getResources().getColor(R.color.colorPrimaryTrams));
            helper.setText(R.id.row_no_tv,helper.getAdapterPosition()+1+"");
            helper.setText(R.id.Batch_no_tv,item.getBatch_no());
            helper.setTextColor(R.id.Batch_no_tv,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Card_id_tv,item.getCard_id());
            helper.setTextColor(R.id.Card_id_tv,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Claim_no,item.getClaim_no());
            helper.setTextColor(R.id.Claim_no,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Difference_amt,item.getDifference_amt());
            helper.setTextColor(R.id.Difference_amt,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Disc_code,item.getDisc_code());
            helper.setTextColor(R.id.Disc_code,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Group_name,item.getGroup_name());
            helper.setTextColor(R.id.Group_name,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Med_code,item.getMed_code());
            helper.setTextColor(R.id.Med_code,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Med_name,item.getMed_name());
            helper.setTextColor(R.id.Med_name,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Prv_branch_name,item.getPrv_branch_name());
            helper.setTextColor(R.id.Prv_branch_name,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Prv_branch_num,item.getPrv_branch_num());
            helper.setTextColor(R.id.Prv_branch_num,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Prv_no,item.getPrv_no());
            helper.setTextColor(R.id.Prv_no,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.System_amt,item.getSystem_amt());
            helper.setTextColor(R.id.System_amt,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Unit,item.getUnit());
            helper.setTextColor(R.id.Unit,helper.itemView.getResources().getColor(R.color.colormain));
            helper.setText(R.id.Prv_name,item.getPrv_name());
            helper.setTextColor(R.id.Prv_name,helper.itemView.getResources().getColor(R.color.colormain));
        }
        else {
            helper.itemView.setBackgroundColor(helper.itemView.getResources().getColor(R.color.color_batch));
            helper.setText(R.id.row_no_tv,helper.getAdapterPosition()+1+"");
            helper.setText(R.id.Batch_no_tv,item.getBatch_no());
            helper.setTextColor(R.id.Batch_no_tv,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Card_id_tv,item.getCard_id());
            helper.setTextColor(R.id.Card_id_tv,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Claim_no,item.getClaim_no());
            helper.setTextColor(R.id.Claim_no,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Difference_amt,item.getDifference_amt());
            helper.setTextColor(R.id.Difference_amt,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Disc_code,item.getDisc_code());
            helper.setTextColor(R.id.Disc_code,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Group_name,item.getGroup_name());
            helper.setTextColor(R.id.Group_name,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Med_code,item.getMed_code());
            helper.setTextColor(R.id.Med_code,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Med_name,item.getMed_name());
            helper.setTextColor(R.id.Med_name,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Prv_branch_name,item.getPrv_branch_name());
            helper.setTextColor(R.id.Prv_branch_name,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Prv_branch_num,item.getPrv_branch_num());
            helper.setTextColor(R.id.Prv_branch_num,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Prv_no,item.getPrv_no());
            helper.setTextColor(R.id.Prv_no,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.System_amt,item.getSystem_amt());
            helper.setTextColor(R.id.System_amt,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Unit,item.getUnit());
            helper.setTextColor(R.id.Unit,helper.itemView.getResources().getColor(R.color.color_white));
            helper.setText(R.id.Prv_name,item.getPrv_name());
            helper.setTextColor(R.id.Prv_name,helper.itemView.getResources().getColor(R.color.color_white));


        }


    }




}