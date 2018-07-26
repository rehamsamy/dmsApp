package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.Salesitem;

import java.util.List;

/**
 * Created by Mahmoud on 3/30/2018.
 */

public class SalesAdapter extends BaseQuickAdapter<Salesitem, BaseViewHolder> {

    public SalesAdapter(int layoutResId, List<Salesitem> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
    }

    @Override
    protected void convert(BaseViewHolder helper, Salesitem item) {


        helper.setText(R.id.sales_order_id,"# "+item.getId());
        helper.setText(R.id.agent_name,item.getAgentEname());
        helper.setText(R.id.agent_phone,item.getMobile());
       // helper.setText(R.id.agent_type,item.getCustomerType());
        helper.setText(R.id.faxNumber,item.getFaxNo());

        helper.addOnClickListener(R.id.deactive_imgv);
        helper.addOnClickListener(R.id.finish_imgv);

        if (item.getActive().equals(Constants.STATE_YES)&&item.getFinish().equals(Constants.STATE_NO)){
            helper.setBackgroundRes(R.id.state_tv,R.drawable.wait_chip);
            helper.setText(R.id.state_tv,R.string.avialable);
            if (item.isLoading()){
                helper.setVisible(R.id.loading_progress,true);
                helper.setVisible(R.id.action_layout,false);
            }else {
                helper.setVisible(R.id.loading_progress,false);
                helper.setVisible(R.id.action_layout,true);
            }

        }else if(item.getActive().equals(Constants.STATE_NO)) {
            helper.setBackgroundRes(R.id.state_tv,R.drawable.refuse_chipse);
            helper.setText(R.id.state_tv,R.string.label_cancel);
            helper.setVisible(R.id.action_layout,false);
            helper.setVisible(R.id.loading_progress,false);

        }
        else if(item.getActive().equals(Constants.STATE_YES)) {
            helper.setBackgroundRes(R.id.state_tv,R.drawable.accept_chip);
            helper.setText(R.id.state_tv,R.string.label_finish);
            helper.setVisible(R.id.action_layout,false);
            helper.setVisible(R.id.loading_progress,false);

        }



    }


}
