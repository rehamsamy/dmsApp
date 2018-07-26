package com.dmsegypt.dms.ux.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.ContractDetail;
import com.dmsegypt.dms.rest.model.MedServiceContract;

import java.util.List;

/**
 * Created by amr on 20/03/2018.
 */

public class ContractServiceAdapter extends BaseQuickAdapter<MedServiceContract, BaseViewHolder> {
    public ContractServiceAdapter(List<MedServiceContract> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MedServiceContract medServiceContract) {
        baseViewHolder.setText(R.id.serv_code,medServiceContract.getServiceId());
        baseViewHolder.setText(R.id.serv_name,medServiceContract.getServiceName());
        baseViewHolder.setText(R.id.serv_notes,medServiceContract.getNotes());
        baseViewHolder.setText(R.id.celling_amt,medServiceContract.getCeilingAmt());
        baseViewHolder.setText(R.id.cielling_pec,medServiceContract.getCeilingPert());
        baseViewHolder.setText(R.id.carry_amt,medServiceContract.getCarrAmt());
        baseViewHolder.setText(R.id.indlist_price,medServiceContract.getIndListPrice());
        baseViewHolder.setText(R.id.refund_flag,medServiceContract.getRefundFlag());

    }
}
