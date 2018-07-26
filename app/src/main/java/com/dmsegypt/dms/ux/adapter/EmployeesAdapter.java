package com.dmsegypt.dms.ux.adapter;

import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.User;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class EmployeesAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    boolean isApprovalActivity;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public EmployeesAdapter(int layoutResId, List<User> data,boolean isApprovalActivity) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
        this.isApprovalActivity=isApprovalActivity;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given Item.
     *
     * @param helper A fully initialized helper.
     * @param item   The Item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, User item) {

        if (item != null) {
            helper.setText(R.id.tv_member_name, String.format("%s %s %s", item.getFirstName(), item.getSecondName(), item.getLastName()));
            helper.setText(R.id.tv_card_id, item.getCardId());
             if (!isApprovalActivity) {
                 helper.addOnClickListener(R.id.switch_emp_enable);
                 helper.addOnClickListener(R.id.tv_editcard);

                 if (item.getStatus().equals("1") && (item.getHr_request_type().equals("0") || item.getHr_request_type().equals("")))
                     ((Switch) helper.getView(R.id.switch_emp_enable)).setChecked(true);
                 else ((Switch) helper.getView(R.id.switch_emp_enable)).setChecked(false);
             }else {
                 helper.setVisible(R.id.switch_emp_enable,false);
                 helper.setVisible(R.id.tv_editcard,false);
             }

        }

    }
}