package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.MedicineDetails;
import com.dmsegypt.dms.utils.DataUtils;

import java.util.List;

/**
 * Created by mahmoud on 10/06/17.
 */

public class ChronicAdapter extends BaseQuickAdapter<MedicineDetails,BaseViewHolder> {
    public ChronicAdapter(int layoutResId, List<MedicineDetails> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());

    }

    @Override
    protected void convert(BaseViewHolder helper, MedicineDetails item) {
        helper.setText(R.id.med_name,item.getMed_name());
        helper.setText(R.id.med_dosage,item.getDosage_form());
        helper.setText(R.id.med_pack_size,item.getPack_size());
        helper.setText(R.id.med_num_unit,item.getUnit_num());
        helper.setText(R.id.med_duration,item.getMed_duration());
        if(item.getActive().equals("Y")){
            helper.setImageResource(R.id.active_icon, DataUtils.getTypeIconById("9"));
            helper.setVisible(R.id.image_show,false);
        }else {
            helper.setImageResource(R.id.active_icon, DataUtils.getTypeIconById("10"));
            helper.setVisible(R.id.image_show,true);
        }
        if(!(item.getExcess().equals("0"))){
            helper.setVisible(R.id.tv_excess,true);
        }


    }
}
