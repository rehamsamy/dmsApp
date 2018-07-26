package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.BatchSummary;
import com.dmsegypt.dms.rest.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amr on 26/12/2017.
 */

public class SeletcedUserAdapter extends BaseQuickAdapter<User,BaseViewHolder> {

    public SeletcedUserAdapter(int layoutResId, List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        baseViewHolder.setText(R.id.name_tv,user.getFirstName()+" "+user.getSecondName());
        baseViewHolder.addOnClickListener(R.id.imgv_delete);
    }
}
