package com.dmsegypt.dms.ux.adapter;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.NotificationItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 10/03/2017.
 **/

public class NotificationAdapter extends BaseQuickAdapter<NotificationItem, BaseViewHolder> {
    public NotificationAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());

    }

    @Override
    protected void convert(BaseViewHolder helper, NotificationItem item) {
        helper.setText(R.id.tvDetails, item.getNotificationDetails())
                .setText(R.id.tvDate, item.getNotificationDate().toString());
    }
}