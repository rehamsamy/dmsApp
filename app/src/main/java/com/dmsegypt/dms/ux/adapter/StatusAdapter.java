package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Response.Status;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class StatusAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public StatusAdapter(int layoutResId, List<Status> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());

    }

    /**
     * Implement this method and use the helper to adapt the view to the given Item.
     *
     * @param helper A fully initialized helper.
     * @param item   The Item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tv_number, item.getNumber());
        helper.setText(R.id.tv_date, item.getDate());
        helper.setText(R.id.tv_status, item.getStatus());

    }
}