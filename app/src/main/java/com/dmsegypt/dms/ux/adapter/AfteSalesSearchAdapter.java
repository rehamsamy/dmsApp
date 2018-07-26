package com.dmsegypt.dms.ux.adapter;

import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.AfterSalesItem;
import com.dmsegypt.dms.rest.model.User;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class AfteSalesSearchAdapter extends BaseQuickAdapter<AfterSalesItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public AfteSalesSearchAdapter(int layoutResId, List<AfterSalesItem> data) {
        super(layoutResId,data);
        setLoadMoreView(new LoadMoreView());
    }

    /**
     * Implement this method and use the helper to adapt the view to the given Item.
     *
     * @param helper A fully initialized helper.
     * @param item   The Item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, AfterSalesItem item) {
        if (item != null) {
            helper.setText(R.id.tv_comp_name,item.getCompanyName());
            helper.setText(R.id.tv_visit_id,"#"+item.getId());
            helper.setText(R.id.tv_visit_date,item.getVisitDate());
            if (item.getDeletedFlag().equals("Y"))
            {   if (item.isLoading()){

                helper.setVisible(R.id.deleted_label,false);
                helper.setVisible(R.id.delete_after_sales_item,false);
            }else {
                helper.setVisible(R.id.deleted_label, true);
                helper.setVisible(R.id.delete_after_sales_item, false);
            }
            }else {
                helper.setVisible(R.id.deleted_label,false);
                helper.setVisible(R.id.delete_after_sales_item,true);
            }
            helper.addOnClickListener(R.id.delete_after_sales_item);
             if (item.isLoading){
                 helper.setVisible(R.id.progress,true);
             }else {
                 helper.setVisible(R.id.progress,false);
             }
        }
    }
}