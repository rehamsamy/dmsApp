package com.dmsegypt.dms.ux.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.ux.custom_view.UploadImageView;

import java.util.List;

/**
 * Created by amr on 25/12/2017.
 */

public class MemberAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public MemberAdapter( List<User> data) {
        super(R.layout.member_list_row, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User user) {
        helper.setText(R.id.name_tv, String.format("%s %s %s", user.getFirstName(), user.getSecondName(), user.getLastName()));
        helper.setText(R.id.card_tv,user.getCardId());
        if (user.isSelected()){
           helper.setVisible(R.id.state_imageview,true);
        }else {
            helper.setVisible(R.id.state_imageview,false);        }
    }

}
