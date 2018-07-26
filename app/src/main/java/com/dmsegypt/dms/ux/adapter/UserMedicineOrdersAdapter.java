package com.dmsegypt.dms.ux.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.MedicineOrder;

import java.util.List;

/**
 * Created by amr on 25/12/2017.
 */

public class UserMedicineOrdersAdapter extends BaseQuickAdapter<MedicineOrder, BaseViewHolder> {
    public UserMedicineOrdersAdapter(List<MedicineOrder> data) {
        super(R.layout.phamacies_orders_item, data);
        setLoadMoreView(new LoadMoreView());

    }


    @Override
    protected void convert(final BaseViewHolder helper, MedicineOrder rosheta) {
        if (rosheta.getPresImgUrl()!=null) {
            byte[] decodedString = Base64.decode(rosheta.getPresImgUrl(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            helper.setImageBitmap(R.id.image_id, decodedByte);
            helper.addOnClickListener(R.id.image_id);
        }else {
            helper.setImageResource(R.id.image_id, R.drawable.no_image);
        }
        helper.setText(R.id.order_name,"# "+rosheta.getOrderName());
        helper.setText(R.id.pharmacy_name,rosheta.getPharmName());
        helper.setText(R.id.branch_name,rosheta.getBranchName());
        helper.setText(R.id.card_id,rosheta.getCardId());
        helper.setText(R.id.notes,rosheta.getNotes());


        helper.setOnClickListener(R.id.arrowDown, new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                helper.setVisible(R.id.arrowDown,false);
                helper.setVisible(R.id.arrowUp,true);
                helper.setVisible(R.id.notes,true);

            }
        });



        helper.setOnClickListener(R.id.arrowUp, new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                helper.setVisible(R.id.arrowDown,true);
                helper.setVisible(R.id.arrowUp,false);
                helper.setVisible(R.id.notes,false);
            }

        });
        //notes
        //edi
        helper.setText(R.id.rder_date, rosheta.getOrderDate());




        if (rosheta.getOrderState().equals("1")) {
            helper.setText(R.id.state, "Accepted");
            helper.setBackgroundRes(R.id.state, R.drawable.accept_chip);
        } else if (rosheta.getOrderState().equals("2")) {
            helper.setText(R.id.state, "Pending");
            helper.setBackgroundRes(R.id.state, R.drawable.wait_chip);
        } else if (rosheta.getOrderState().equals("0")) {
            helper.setText(R.id.state, "Refused");
            helper.setBackgroundRes(R.id.state, R.drawable.refuse_chipse);
        }


    }


}