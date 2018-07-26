package com.dmsegypt.dms.ux.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class UsersAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public UsersAdapter(Context context, int layoutResId, List<User> data) {
        super(layoutResId, data);
        mContext = context;
        setLoadMoreView(new LoadMoreView());

    }

    /**
     * Implement this method and use the helper to adapt the view to the given Item.
     *
     * @param helper A fully initialized helper.
     * @param item   The Item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, User item) {

        if(item != null) {

            helper.setText(R.id.tv_user_name, String.format("%s %s %s", item.getFirstName(), item.getSecondName(), item.getLastName()));

            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                Picasso.with(mContext)
                        .load(item.getImageUrl())
                        .priority(Picasso.Priority.HIGH)
                        .into((ImageView) helper.getView(R.id.civ_user_image));
            }
            if (item.getSwitch_user_type().equalsIgnoreCase(Constants.USER_TYPE_NORMAL)){
                helper.setText(R.id.tv_user_type,R.string.label_type_normal_user);
            }else if (item.getSwitch_user_type().equalsIgnoreCase(Constants.USER_TYPE_HR)){
                helper.setText(R.id.tv_user_type,R.string.label_type_hr_user);

            }else if (item.getSwitch_user_type().equalsIgnoreCase(Constants.USER_TYPE_DMS)){
                helper.setText(R.id.tv_user_type,R.string.label_type_dms_user);
            }
            else if(item.getSwitch_user_type().equalsIgnoreCase(Constants.USER_TYPE_FAMILY_MEMBER)){
                helper.setText(R.id.tv_user_type,R.string.label_type_family_member);
            }

        }

    }
}