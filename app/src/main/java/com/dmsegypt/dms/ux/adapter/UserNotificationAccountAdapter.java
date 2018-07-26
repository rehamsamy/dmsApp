package com.dmsegypt.dms.ux.adapter;


import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Account;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.dmsegypt.dms.ux.custom_view.CropCircleTransform;

import java.util.List;


/**
 * Created by mahmoud on 12/02/17.
 */

public class UserNotificationAccountAdapter extends BaseQuickAdapter<Account,BaseViewHolder> {
    public UserNotificationAccountAdapter(int layoutResId, List<Account> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Account mobileUser) {

if (mobileUser!=null)
{
    helper.addOnClickListener(R.id.checkbox);

    helper.setText(R.id.name,mobileUser.getName());
    helper.setText(R.id.cardId,mobileUser.getCardId());
    helper.setText(R.id.email,mobileUser.getEmail());
    helper.setText(R.id.phone,mobileUser.getMobile());
    if (mobileUser.getType().equals("0"))
    {
        helper.setText(R.id.type,"User");
    }
    else  if (mobileUser.getType().equals("1"))
    {
        helper.setText(R.id.type,"HR");
    }
    else  if (mobileUser.getType().equals("2"))
    {
        helper.setText(R.id.type,"Provider");
    }
    else if (mobileUser.getType().equals("3"))
    {
        helper.setText(R.id.type,"DMS Member");
    }else {
        helper.setText(R.id.type,"Un Known");

    }

    helper.setChecked(R.id.checkbox,mobileUser.isChecked());



    helper.setChecked(R.id.checkbox,mobileUser.isChecked());



/*    helper.getView(R.id.checkbox).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mobileUser.isChecked())
            {
                   mobileUser.setChecked(false);
            }
            else {
                   mobileUser.setChecked(true);
                 }
            helper.setChecked(R.id.checkbox,mobileUser.isChecked());
        }
    });*/




}

    }

}
