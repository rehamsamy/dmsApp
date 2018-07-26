package com.dmsegypt.dms.ux.adapter;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.utils.DataUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class ProviderAdapter extends BaseQuickAdapter<Provider, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public ProviderAdapter(int layoutResId, List<Provider> data) {
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
    protected void convert(BaseViewHolder helper, Provider item) {
        helper.setText(R.id.title, item.getName());
        helper.setText(R.id.address, (!item.getAddress().isEmpty()) ?
                item.getAddress() :
                mContext.getString(R.string.not_available_address));
        helper.setText(R.id.phone, (!item.getTel().isEmpty()) ?
                item.getTel() :
                mContext.getString(R.string.not_available_phone));
        helper.setImageResource(R.id.ivType, DataUtils.getTypeIconById(item.getProviderType()));
        helper.addOnClickListener(R.id.ivNavigate);
    }
}