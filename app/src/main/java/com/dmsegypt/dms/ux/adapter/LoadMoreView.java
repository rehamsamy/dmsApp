package com.dmsegypt.dms.ux.adapter;

import com.dmsegypt.dms.R;

/**
 * Created by amr on 27/11/2017.
 */

public class LoadMoreView extends com.chad.library.adapter.base.loadmore.LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
