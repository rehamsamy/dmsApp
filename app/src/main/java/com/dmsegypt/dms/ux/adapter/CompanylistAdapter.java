package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Company;
import com.dmsegypt.dms.ux.activity.AddOrderActivity;

import java.util.List;

/**
 * Created by mahmoud on 31/12/17.
 */

public class CompanylistAdapter extends BaseQuickAdapter<Company,BaseViewHolder> {

    public CompanylistAdapter(AddOrderActivity addOrderActivity, int layoutResId, List<Company> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Company company) {
        if(company != null){
            if(company.getComp_aname() != null){
                helper.setText(R.id.tv_company_a_name,company.getComp_aname());

            }
            if(company.getComp_ename() != null){
                helper.setText(R.id.tv_company_e_name,company.getComp_ename());

            }
            if(company.getComp_address1() != null){
                helper.setText(R.id.tv_company_address,company.getComp_address1());

            }
            if(company.getComp_id() != null){
                helper.setText(R.id.tv_company_id,company.getComp_id());

            }
        }

    }
}
