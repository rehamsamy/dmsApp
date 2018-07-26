package com.dmsegypt.dms.ux.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Base64;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.Manifest;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.ux.activity.ApprovalReplyActivity;
import com.dmsegypt.dms.ux.dialogs.ImageViewerDialog;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.R.attr.bitmap;


/**
 * Created by mahmoud on 24-Sep-17.
 */

public class RequestAdapter extends BaseQuickAdapter<Request,BaseViewHolder> {


    public RequestAdapter(Context context,int layoutResId, List<Request> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());

    }


    @Override
    protected void convert(final BaseViewHolder helper, final Request request) {
        if(request != null){
            if(request.getTitle() != null){
                helper.setVisible(R.id.req_title,true);
                helper.setText(R.id.req_title,request.getTitle());
            }else {
            }

            if(request.getImage() != null) {

                byte[] decodedString = Base64.decode(request.getImage(), Base64.DEFAULT);
                if (decodedString!=null){
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ((ImageView) helper.getView(R.id.req_image_list)).setImageBitmap(decodedByte);



                }







            }
            if(request.getReq_type() != null){
                if(request.getReq_type().equals("c")){
                    helper.setVisible(R.id.req_type,true);
                    helper.setText(R.id.req_type,R.string.action_chronic);
                    helper.setBackgroundRes(R.id.req_type,R.drawable.refuse_chipse);
                }else if(request.getReq_type().equals("m")){
                    helper.setVisible(R.id.req_type,true);
                    helper.setText(R.id.req_type,R.string.medical);
                    helper.setBackgroundRes(R.id.req_type,R.drawable.accept_chip);

                }
            }else {
            }

            if(request.getDescription() != null) {
                helper.setVisible(R.id.req_details,true);
                helper.setText(R.id.req_details, request.getDescription());
            }else {

            }
            if(request.getCardID() != null) {
                helper.setVisible(R.id.req_cardid,true);

                helper.setText(R.id.req_cardid, request.getCardID());
            }else {
            }
            if(request.getReq_date() != null) {
                helper.setVisible(R.id.req_date,true);
                helper.setText(R.id.req_date, request.getReq_date());
            }else {

            }
            if(request.getEmp_ename() != null){
                helper.setText(R.id.user_name,request.getEmp_ename());
            }

            helper.getView(R.id.req_image_list).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

/*
                    helper.getView(R.id.req_image_list).buildDrawingCache();
                    Bitmap bitmap = helper.getView(R.id.req_image_list).getDrawingCache();
                   String real = request.getImage().replace("D:\\BackEnd", "http:" + "\\" + Constants.BASE_SERVER_IP);
                    final String finalb = real.replace("\\", "//");
                    Palette palette = Palette.from(bitmap).generate();
*/






                 /*   ByteArrayOutputStream byteArrayOutputStream = new_image ByteArrayOutputStream();
                    bmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/


                    ImageViewerDialog dialog= ImageViewerDialog.newInstance(request.getImage(),false);
                    dialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(),"image_dialog");
                }
            });

            helper.getView(R.id.ivReplay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    IntentManager.startReplyActivity((Activity) helper.getConvertView().getContext(),ApprovalReplyActivity.class,request.getId(),view);

                    //  helper.setVisible(R.id.req_image_list,false);
                 /*   helper.getView(R.id.req_image_list).requestLayout();
                    helper.getView(R.id.req_image_list).getLayoutParams().height=200;
                    helper.getView(R.id.req_image_list).getLayoutParams().width=200;
                    helper.setVisible(R.id.reply_container,true);
                    helper.getView(R.id.reply_container).setAlpha(0.0f);

                    helper.getView(R.id.reply_container).animate().translationY(view.getHeight()).alpha(1.0f).setDuration(500).setListener(null);
                    helper.setVisible(R.id.minimize_button,true);*/


                }
            });

    /*        helper.getView(R.id.minimize_button).setOnClickListener(new_image View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   helper.setVisible(R.id.req_image_list,true);
                    helper.getView(R.id.req_image_list).requestLayout();
                    helper.getView(R.id.req_image_list).getLayoutParams().height=300;
                    helper.getView(R.id.req_image_list).getLayoutParams().width=ViewGroup.LayoutParams.MATCH_PARENT;
                    helper.getView(R.id.reply_container).animate().translationY(0).alpha(0.0f).setDuration(500).setListener(new_image Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            helper.setVisible(R.id.reply_container,false);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    helper.setVisible(R.id.minimize_button,false);
                }
            });*/
            //   helper.setImageBitmap(R.id.req_image,request.getImage());



        }

    }



}
