package com.dmsegypt.dms.ux.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Chat;
import com.dmsegypt.dms.rest.model.MedicineDetails;
import com.dmsegypt.dms.utils.DataUtils;
import com.dmsegypt.dms.utils.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amr on 19/12/2017.
 */

public class ChatAdapter  extends BaseQuickAdapter<Chat,BaseViewHolder> {
    String userid;
    public ChatAdapter(int layoutResId, List<Chat> data,String userid) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
        this.userid=userid;

    }

    @Override
    protected void convert(BaseViewHolder helper, Chat item) {
        if (item.getIsGroup()==1){
        helper.setText(R.id.txtName,item.getTitle());
            if (item.getMembers()!=null){
                if(item.getMembers().containsKey(userid)) {
                    if (item.getMembers().get(userid).getMessage_count()>0){
                    helper.setVisible(R.id.count_tv, true);
                    helper.setText(R.id.count_tv, item.getMembers().get(userid).getMessage_count() + "");

                    }else {
                        helper.setVisible(R.id.count_tv, false);
                    }
                }else {
                    helper.setVisible(R.id.count_tv, false);
                }
            }else {
                helper.setVisible(R.id.count_tv,false);
            }

        }else {
            for (String key:item.getMembers().keySet()){
                if (!key.equals(userid)){
                    helper.setText(R.id.txtName,item.getMembers().get(key).getName());
                }else {
                    if (item.getMembers().get(key).getMessage_count()!=0){
                        helper.setVisible(R.id.count_tv,true);
                        helper.setText(R.id.count_tv,item.getMembers().get(key).getMessage_count()+"");
                    }else {
                        helper.setVisible(R.id.count_tv,false);

                    }

                }
            }


        }


        if (item.getLast_message()!=null){
            helper.setText(R.id.txtTime,  item.getLast_message().getDate());
        if (item.getLast_message().getSent_by().equals(userid)){
            helper.setVisible(R.id.state_imageview,true);
            helper.setImageResource(R.id.state_imageview,
                    item.getLast_message().getIs_seen()==1?R.drawable.ic_seen:R.drawable.ic_unseen);
        }
        else {
            helper.setVisible(R.id.state_imageview,false);
        }

        if (item.getLast_message().getMessage().isEmpty()&&!item.getLast_message().getImg_url().isEmpty()){
            helper.setText(R.id.txtMessage,"");
            ((TextView)helper.getView(R.id.txtMessage)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_image_default,0,0,0);

        }else if (!item.getLast_message().getMessage().isEmpty()){
            helper.setText(R.id.txtMessage,item.getLast_message().getMessage());
            ((TextView)helper.getView(R.id.txtMessage)).setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

        }

        }else {
            ((TextView)helper.getView(R.id.txtMessage)).setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            helper.setText(R.id.txtMessage,"");
            helper.setText(R.id.txtTime, "");
            helper.setVisible(R.id.state_imageview,false);

        }
        helper.setImageResource(R.id.icon_avata,item.getIsGroup()==1?R.drawable.ic_notify_group:R.drawable.default_avata);

    }
}