package com.dmsegypt.dms.ux.adapter;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.IndemnityRequest;
import com.dmsegypt.dms.ux.activity.ListIndemnityRequests;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.ux.dialogs.ImageViewerDialog;


import java.util.List;
import android.widget.Gallery;

import butterknife.BindView;


/**
 * Created by mahmoud on 12/12/17.
 */

public class IndemnityListAdapter extends BaseQuickAdapter<IndemnityRequest,BaseViewHolder> {

    public IndemnityListAdapter(ListIndemnityRequests listIndemnityRequests, int layoutResId, List<IndemnityRequest> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final IndemnityRequest indemnityRequest) {
        RecyclerView recyclerView= (RecyclerView) helper.itemView.findViewById(R.id.gallery);
        if(indemnityRequest.getImages().size() != 0 && indemnityRequest.getImages() != null) {
            recyclerView.setVisibility(View.VISIBLE);
            final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(indemnityRequest.getImages());
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            recyclerView.setAdapter(galleryImageAdapter);
            galleryImageAdapter.setListener(new GalleryImageAdapter.OnClickListener() {
                @Override
                public void OnClick(int pos, final String url) {
                    if (url!=null){
                        String real = url.replace("D:\\BackEnd", "http:" + "\\" + Constants.BASE_SERVER_IP);
                        final String finalb = real.replace("\\", "//");
                        Glide.with(helper.convertView.getContext()).load(finalb).fitCenter().error(R.drawable.no_image).placeholder(R.drawable.no_image).into((ImageView) helper.getView(R.id.imageView));
                        helper.getView(R.id.imageView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              //  String real=url.replace("D:\\BackEnd","http:"+"\\"+"41.33.128.139");
                              //  final String finalb=real.replace("\\","//");
                                ImageViewerDialog dialog= ImageViewerDialog.newInstance(finalb,true);
                                dialog.show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(),"image_dialog");

                            }
                        });
                    }
                }
            });
        }else {
            recyclerView.setVisibility(View.GONE);

        }

            if(indemnityRequest.getDescreption() != null){
                helper.setText(R.id.req_description,indemnityRequest.getDescreption());
            }
            if(indemnityRequest.getTitle() != null){
                helper.setText(R.id.req_title,indemnityRequest.getTitle());
            }
            if(indemnityRequest.getRequest_date() != null){
                helper.setText(R.id.req_date,indemnityRequest.getRequest_date());
            }
            if(indemnityRequest.getCardId() != null){
                helper.setText(R.id.req_cardid,indemnityRequest.getCardId());
            }







    }
}