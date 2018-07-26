package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Company;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.ux.adapter.LoadMoreView;

import java.util.List;

/**
 * Created by Mahmoud on 4/1/2018.
 */

public class CompaniesFragmentAdapter extends BaseQuickAdapter<Company, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public CompaniesFragmentAdapter(int layoutResId, List<Company> data) {
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
    protected void convert(BaseViewHolder helper, Company item) {

        if (item != null) {
            helper.setText(R.id.tv_member_name,  item.getComp_ename());


        }

    }
}