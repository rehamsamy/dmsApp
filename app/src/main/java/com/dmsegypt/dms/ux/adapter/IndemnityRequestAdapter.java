package com.dmsegypt.dms.ux.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.SingleRequestImage;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.activity.SingleApproval;
import com.dmsegypt.dms.ux.custom_view.UploadImageView;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 10/12/2017.
 */

public class IndemnityRequestAdapter extends RecyclerView.Adapter<IndemnityRequestAdapter.ImageViewHolder>{
    ArrayList<SingleRequestImage>singleRequestImages;
    Activity activity;
    String subFolder="";

    public IndemnityRequestAdapter(ArrayList<SingleRequestImage> singleRequestImages, String subFolder, Activity activity) {
        this.singleRequestImages = singleRequestImages;
        this.activity = activity;
        this.subFolder=subFolder;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_images,null,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.BindView(singleRequestImages.get(position));

    }

    @Override
    public int getItemCount() {
        return singleRequestImages.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements UploadImageView.OnUploadImageClickListener {

        @BindView(R.id.single_imgv)
        UploadImageView uploadImageView;


        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            uploadImageView.setUploadImageClickListener(this);
        }

        void BindView(SingleRequestImage singleRequestImage){
            if (getAdapterPosition()==singleRequestImages.size()-1){
                uploadImageView.showState(UploadImageView.DEFAULT_STATE);
                uploadImageView.setDefaultImage();
            }
            else {
            uploadImageView.PreviewImage(singleRequestImage.getLocal_path());
            uploadImageView.showState(singleRequestImage.getState());
            if (singleRequestImage.getState()==UploadImageView.LOADING_STATE){
                uploadImageRequest(getAdapterPosition());
            }
            }
        }


        @Override
        public void onCloseClicked(View view) {
           /* singleRequestImages.remove(getAdapterPosition());
            notifyDataSetChanged();*/
        }

        @Override
        public void onRetryClicked(View view) {
            int pos=getAdapterPosition();
            SingleRequestImage requestImage=singleRequestImages.get(pos);
            requestImage.setState(UploadImageView.LOADING_STATE);
            uploadImageView.showState(requestImage.getState());
            uploadImageRequest(pos);
        }

        @Override
        public void onAddClicked(View view) {

            Intent intent  = new Intent(activity, ImagePickerActivity.class);
            activity.startActivityForResult(intent, SingleApproval.INTENT_REQUEST_GET_IMAGES);

        }

        void uploadImageRequest(final int pos){


             Uploader uploader=new Uploader();

            uploader.UploadImage(itemView.getContext(),singleRequestImages.get(pos).getLocal_path(),subFolder
                    , Uploader.SINGLE_FOLDER,
                    new Callback<ResponseUpdateProfileImage>() {
                        @Override
                        public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                            if (response.body().getCode()==1){
                                singleRequestImages.get(pos).setState(UploadImageView.SUCESS_STATE);
                                Toast.makeText(activity, pos+":"+singleRequestImages.get(pos).getLocal_path().toString(), Toast.LENGTH_SHORT).show();
                                notifyItemChanged(pos);
                            }else {
                                singleRequestImages.get(pos).setState(UploadImageView.FAILED_STATE);
                                notifyItemChanged(pos);

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable t) {
                            singleRequestImages.get(pos).setState(UploadImageView.FAILED_STATE);
                            notifyItemChanged(pos);
                        }
                    });

        }





    }
}
