package com.dmsegypt.dms.ux.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.rest.model.Response.ResponseRelpy;
import com.dmsegypt.dms.rest.model.Response.ResponseRequests;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.dmsegypt.dms.ux.custom_view.CropCircleTransform;
import com.dmsegypt.dms.ux.dialogs.ImageViewerDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mahmoud on 10/19/17.
 */

public class UserRequstsAdaper extends BaseQuickAdapter<Request,BaseViewHolder> {
    public UserRequstsAdaper(Context context, int layoutReqId, List<Request> data) {
        super(layoutReqId,data);
        setLoadMoreView(new LoadMoreView());

    }

    @Override
    protected void convert(final BaseViewHolder helper, final Request request) {
        if(request != null){/*
            String real = request.getImage().replace("D:\\BackEnd", "http:" + "\\" + Constants.BASE_SERVER_IP);
            final String finalb = real.replace("\\", "//");*/
            if (request.getImage()!=null){
            byte[] decodedString = Base64.decode(request.getImage(), Base64.DEFAULT);
            if (decodedString!=null){
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ((CircleImageView) helper.getView(R.id.req_img))
                .setImageBitmap(decodedByte);



            }
            }
            if (request.getReply_image()!=null) {
                byte[] replydecodedString = Base64.decode(request.getReply_image(), Base64.DEFAULT);
                if (replydecodedString != null) {
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(replydecodedString, 0, replydecodedString.length);
                    ((CircleImageView) helper.getView(R.id.rep_img))
                            .setImageBitmap(decodedByte);

                }
            }
            if (request.getReply_image()!=null){
            helper.getView(R.id.rep_img).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      ImageViewerDialog dialog=ImageViewerDialog.newInstance(request.getReply_image(),false);
                      dialog.show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), "image_dialog");

                  }
              });

            }
            if (request.getImage()!=null){

                helper.getView(R.id.req_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageViewerDialog dialog=ImageViewerDialog.newInstance(request.getImage(),false);
                    dialog.show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), "image_dialog");
                }
            });
            }



           helper.getView(R.id.show_reply).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (helper.getView(R.id.reply_layout).getVisibility()!=View.VISIBLE){
                       helper.setVisible(R.id.reply_layout,true);
                       helper.setText(R.id.show_reply,R.string.hide_reply);
                       ((TextView)helper.getView(R.id.show_reply)).setCompoundDrawablesWithIntrinsicBounds
                               (0, 0,R.drawable.ic_keyboard_arrow_down_white_24dp, 0);
                   }
                   else{
                       helper.setVisible(R.id.reply_layout,false);
                       helper.setText(R.id.show_reply,R.string.show_reply);
                       ((TextView)helper.getView(R.id.show_reply)).setCompoundDrawablesWithIntrinsicBounds
                               (0, 0,R.drawable.ic_keyboard_arrow_up_white_24dp, 0);

                   }

               }
           });
            if(request.getId() != null) {

            }
            if(request.getTitle() != null){
                helper.setText(R.id.req_title,request.getTitle());
            }

            if(request.getReq_type() != null){
                if(request.getReq_type().equals("C")){
                    helper.setText(R.id.req_type,R.string.action_chronic);
                    helper.setTextColor(R.id.req_type,R.color.colorAccentRed);
                }else if(request.getReq_type().equals("M")){
                    helper.setText(R.id.req_type,R.string.medical);
                    helper.setTextColor(R.id.req_type,R.color.colorAccentyellow);

                }
            }

            if(request.getDescription() != null) {

               helper.setText(R.id.req_details, request.getDescription());
            }
            if(request.getReq_date() != null) {

                helper.setText(R.id.req_date, request.getReq_date());
            }
            if(request.getReply_flag() != null){
               TextView showReplyTv=helper.getView(R.id.show_reply);
                if(request.getReply_flag().equals("1")) {
                    helper.setBackgroundRes(R.id.req_state, R.drawable.accept_chip);
                    helper.setText(R.id.req_state,R.string.msg_accept);
                    helper.setVisible(R.id.show_reply,true);
                    if (showReplyTv.getText().toString().equals(showReplyTv.getContext().getString(R.string.hide_reply))){
                        helper.setVisible(R.id.reply_layout,true);
                    }


                }else if(request.getReply_flag().equals("0")){
                    helper.setBackgroundRes(R.id.req_state,R.drawable.refuse_chipse);
                    helper.setText(R.id.req_state,R.string.msg_refuse);
                    helper.setVisible(R.id.show_reply,true);
                    if (showReplyTv.getText().toString().equals(showReplyTv.getContext().getString(R.string.hide_reply))){
                        helper.setVisible(R.id.reply_layout,true);
                    }

                }else{
                    helper.setBackgroundRes(R.id.req_state,R.drawable.wait_chip);
                    helper.setText(R.id.req_state,R.string.msg_waiting);
                    helper.setVisible(R.id.show_reply,false);
                    helper.setVisible(R.id.reply_layout,false);

                }
            }
            else {
                helper.setBackgroundRes(R.id.req_state,R.drawable.wait_chip);
                helper.setText(R.id.req_state,R.string.msg_waiting);
                helper.setVisible(R.id.show_reply,false);
                helper.setVisible(R.id.reply_layout,false);

            }

            if(request.getReply_note() != null){
                helper.setText(R.id.tvDetails,request.getReply_note());
                helper.setVisible(R.id.tvDetails,true);
            }

        }

    }
}
