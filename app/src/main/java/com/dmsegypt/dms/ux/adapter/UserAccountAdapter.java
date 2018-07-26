package com.dmsegypt.dms.ux.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Account;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.dmsegypt.dms.ux.custom_view.CropCircleTransform;
import com.dmsegypt.dms.ux.dialogs.AdminEditUserActivity;

import java.io.Serializable;
import java.util.List;


/**
 * Created by mahmoud on 12/02/17.
 */

public class UserAccountAdapter extends BaseQuickAdapter<Account,BaseViewHolder> {
    boolean show=false;

    public UserAccountAdapter(int layoutResId, List<Account> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView());
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Account mobileUser) {
        final Context context=helper.getConvertView().getContext();


        if (mobileUser!=null)
        {    helper.addOnClickListener(R.id.switchBtn);
            String image_url="http://41.33.128.139/imageUploader/images/profile/"+mobileUser.getCardId()+".jpg";

            Glide.with(helper.convertView.getContext()).load(image_url).asBitmap().transform(new CropCircleTransform(helper.convertView.getContext())).error(R.drawable.no_image).placeholder(R.drawable.no_image).into((CircleImageView) helper.getView(R.id.profile_image));
            helper.addOnClickListener(R.id.ib_delete);
            helper.setText(R.id.name,mobileUser.getName());
            helper.setText(R.id.cardId,mobileUser.getCardId().equals("-1")?"None":mobileUser.getCardId());
            helper.setText(R.id.email,mobileUser.getEmail());
            helper.setText(R.id.phone,mobileUser.getMobile());
            helper.getView(R.id.tv_edituser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,AdminEditUserActivity.class);
                    intent.putExtra(AdminEditUserActivity.EXTRA_USER_ACCOUNT,(Serializable) mobileUser);
                    ((AppCompatActivity)context).startActivityForResult(intent,AdminEditUserActivity.REQUEST_CODE);
                }
            });



            if (mobileUser.getType().equals("0"))
            {
                helper.setText(R.id.type,"User");
            }
            else  if (mobileUser.getType().equals("1"))
            {
                helper.setText(R.id.type,"HR");
            }
            else  if (mobileUser.getType().equals("2"))
            {
                helper.setText(R.id.type,"Provider");
            }
            else if (mobileUser.getType().equals("3"))
            {
                helper.setText(R.id.type,"DMS Member");
            }
            else {
                helper.setText(R.id.type,"Un Known");

            }



            if (!show)
            {
                helper.setText(R.id.password,"**************");

            }



            helper.getView(R.id.showPasswordBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!show)
                    {
                        show=true;
                        helper.setText(R.id.password,"**************");

                    }else
                    {
                        helper.setText(R.id.password,mobileUser.getPassword());
                        show=false;

                    }
                }
            });

            if (mobileUser.getActive().trim().equals("1"))
            {
                helper.setVisible(R.id.activeState, true);
                helper.setVisible(R.id.inActivestate,false);
                helper.setChecked(R.id.switchBtn,true);
            }
            else {
                helper.setVisible(R.id.activeState, false);
                helper.setVisible(R.id.inActivestate,true);
                helper.setChecked(R.id.switchBtn,false);

            }

        }

    }

}
