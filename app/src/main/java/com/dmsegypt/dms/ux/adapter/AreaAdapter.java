package com.dmsegypt.dms.ux.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.AreaItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by amr on 16/02/2018.
 */

public class AreaAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<AreaItem> areaItems;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AreaAdapter(Context context, ArrayList<AreaItem>countries, LoadMoreListener loadMoreListener) {
        super(context,loadMoreListener);
        this.areaItems =countries;
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_area_row,parent,false);
        return new AreaViewHolder(view);
    }

    @Override
    protected void doBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AreaViewHolder){
            ((AreaViewHolder)holder).bindView(areaItems.get(position));
        }
    }
      class AreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
           @BindView(R.id.nameLabel)
           TextView nameLabel;
         AreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
             itemView.setOnClickListener(this);
        }

         void bindView(AreaItem areaItem) {
             nameLabel.setText(areaItem.getName());
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
        return areaItems.size();
    }

    public interface OnItemClickListener{
        public void OnItemClicked(int pos);
    }

}
