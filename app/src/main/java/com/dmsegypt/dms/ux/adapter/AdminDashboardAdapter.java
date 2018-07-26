package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.DashBoard;

import java.util.List;

/**
 * Created by amr on 15/02/2018.
 */

public class AdminDashboardAdapter extends BaseQuickAdapter<DashBoard,BaseViewHolder> {
    public AdminDashboardAdapter(List<DashBoard> data) {
        super(R.layout.dashboard_row,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DashBoard dashBoard) {
        baseViewHolder.setText(R.id.title_dashboard,dashBoard.getTitle());
        baseViewHolder.setImageResource(R.id.icon_dashboard,dashBoard.getDrawable());
    }
}
