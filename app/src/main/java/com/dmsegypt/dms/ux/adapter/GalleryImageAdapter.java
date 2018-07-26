package com.dmsegypt.dms.ux.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dmsegypt.dms.R;

import java.util.List;

/**
 * Created by mahmoud on 12/12/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud on 12/12/17.
 */

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.GalleryViewHolder>
{
    public List<String> Images;
    private int selected_position=-1;

    public GalleryImageAdapter(List<String> images) {
        Images = images;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gallery_item,null,false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        holder.bindView(Images.get(position));
    }

    @Override
    public int getItemCount() {
        return Images.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {    @BindView(R.id.gallery_imgv)
        ImageView imageView;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        void bindView(String url){
            String real=url.replace("D:\\BackEnd","http:"+"\\"+ Constants.BASE_SERVER_IP);
            final String finalb=real.replace("\\","//");
            Glide.with(imageView.getContext()).load(finalb).fitCenter().error(R.drawable.no_image).diskCacheStrategy(DiskCacheStrategy.RESULT ).placeholder(R.drawable.no_image).into(imageView);
        /*    if (selected_position==getAdapterPosition()){
                itemView.setBackgroundResource(R.drawable.select_border_image);
            }else {
                itemView.setBackgroundResource(R.drawable.border_image);
            }*/
        }

        @Override
        public void onClick(View v) {

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            if (listener!=null){
               imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                       View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
               imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
               imageView.buildDrawingCache();

               Bitmap bmap = imageView.getDrawingCache();

               listener.OnClick(getAdapterPosition(),Images.get(getAdapterPosition()));
           }
        }
    }

    public OnClickListener getListener() {
        return listener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    OnClickListener listener;
interface OnClickListener{
void OnClick(int pos,String url);
}

}


