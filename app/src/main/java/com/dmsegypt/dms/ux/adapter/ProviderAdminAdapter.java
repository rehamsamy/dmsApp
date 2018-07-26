package com.dmsegypt.dms.ux.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Pharmacy;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.Response.GetPharmaciesReponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProviderAdminAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Pharmacy> providerItems;
    private ProviderAdminAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ProviderAdminAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ProviderAdminAdapter(Context context, ArrayList<Pharmacy>countries, LoadMoreListener loadMoreListener) {
        super(context,loadMoreListener);
        this.providerItems =countries;
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pharmcy_row,parent,false);
        return new ProviderViewHolder(view);
    }

    @Override
    protected void doBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProviderViewHolder){
            ((ProviderViewHolder)holder).bindView(providerItems.get(position));
        }
    }
    class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.nameLabel)
        TextView nameLabel;
        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.addressLabel)
        TextView addressLabel;
        ProviderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        void bindView(Pharmacy areaItem) {
            nameLabel.setText(areaItem.getName());
            imageView.setImageResource(R.drawable.ic_company_36dp);
            addressLabel.setText(areaItem.getCode());
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            if (onItemClickListener!=null){
                onItemClickListener.OnItemClicked(pos);
            }

        }
    }
    @Override
    protected int getCount() {
        return providerItems.size();
    }

    public interface OnItemClickListener{
        public void OnItemClicked(int pos);
    }

}

