package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Rpsheta;
import com.dmsegypt.dms.rest.model.User;

import java.util.List;

/**
 * Created by amr on 25/12/2017.
 */

public class RoshetaAdapter extends BaseQuickAdapter<Rpsheta, BaseViewHolder> {
    public RoshetaAdapter(List<Rpsheta> data) {
        super(R.layout.phamacies_orders_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Rpsheta rosheta) {

        helper.setText(R.id.pharmacy_name,rosheta.getName());
        helper.setText(R.id.rder_date,rosheta.getOrderDate());

        if (rosheta.getState().equals("1"))
        {
            helper.setText(R.id.state,"Accepted");
            helper.setBackgroundRes(R.id.state,R.drawable.accept_chip);

        }
       else if (rosheta.getState().equals("2"))
        {
            helper.setText(R.id.state,"Pending");
            helper.setBackgroundRes(R.id.state,R.drawable.wait_chip);

        }

       else if (rosheta.getState().equals("3"))
        {
            helper.setText(R.id.state,"Refused");
            helper.setBackgroundRes(R.id.state,R.drawable.refuse_chipse);

        }

               }


}
