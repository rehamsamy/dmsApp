package com.dmsegypt.dms.ux.adapter;

import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.rest.model.Runner;
import com.dmsegypt.dms.ux.dialogs.MoveMassengerDialog;

import java.util.List;

/**
 * Created by mahmoud on 28/12/17.
 */

public class RunnerListAdapter extends BaseQuickAdapter<Runner,BaseViewHolder> {


    public RunnerListAdapter(MoveMassengerDialog moveMassengerDialog, int layoutResId, List<Runner> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(final BaseViewHolder helper, Runner runner) {
        if(runner != null){
            if(runner.getRunner_ename() != null){
                helper.setText(R.id.tv_runner_name,runner.getRunner_ename());

            }
            if(runner.getRunner_rate() != null){
                helper.setText(R.id.tv_runner_rate,runner.getRunner_rate());

            }
            if(runner.getRunner_own() != null){
                helper.setText(R.id.tv_runner_own,runner.getRunner_own());
            }
        }


    }
}
