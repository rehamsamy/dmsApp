package com.dmsegypt.dms.ux.adapter;

import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Item;
import com.dmsegypt.dms.rest.model.ReasonsCheck;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by mahmoud on 1/01/18.
 */

public class ReasonsAdapter extends BaseQuickAdapter<Item,BaseViewHolder>{
    ArrayList<String> selectedReasons = new ArrayList<String>();

    public ReasonsAdapter(int layoutResId, List<Item> data) {
        super(layoutResId, data);
    }


    public ArrayList<String> getSelectedReasons(){
        return selectedReasons;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Item item) {
        if(item != null){
            if(item.getName() != null) {
                helper.setText(R.id.textView1, item.getName());
                helper.setChecked(R.id.checkBox1,false);
            }
            helper.setOnCheckedChangeListener(R.id.checkBox1, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        selectedReasons.add(item.getId());
                    }else {
                        selectedReasons.remove(item.getId());
                    }
                }
            });

        }


    }
}
