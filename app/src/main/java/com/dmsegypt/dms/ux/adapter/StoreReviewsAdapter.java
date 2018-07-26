package com.dmsegypt.dms.ux.adapter;

import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Review;
import com.dmsegypt.dms.rest.model.User;

import java.util.List;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class StoreReviewsAdapter extends BaseQuickAdapter<Review, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each Item.
     * @param data        A new_image list is created out of this one to avoid mutable list
     */
    public StoreReviewsAdapter(int layoutResId, List<Review> data) {
        super(layoutResId, data);


    }

    /**
     * Implement this method and use the helper to adapt the view to the given Item.
     *
     * @param helper A fully initialized helper.
     * @param item   The Item that needs to be displayed.
     */
    @Override
    protected void convert(final BaseViewHolder helper, Review item) {

        if (item != null) {
            helper.setText(R.id.authorName, item.getAuthorName());
            helper.setText(R.id.comment, item.getComments().get(0).getUserComment().getText());
            helper.setRating(R.id.rateingBar,item.getComments().get(0).getUserComment().getStarRating());

            int commentsCounter=0;
            for (int i=0;i<item.getComments().size();i++)
            {
                if (item.getComments().get(i).getDeveloperComment()!=null) {
                    commentsCounter=1;
                    helper.setText(R.id.developerReply,item.getComments().get(i).getDeveloperComment().getText());

                }
            }


            if (commentsCounter==0)
            {

                helper.setVisible(R.id.arrowController,false);
                helper.setVisible(R.id.developerReplyContainer,false);

            }else{
                helper.setVisible(R.id.arrowController,true);
            }

            helper.setOnClickListener(R.id.arrowDown, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.setVisible(R.id.developerReplyContainer,true);
                    helper.setVisible(R.id.arrowUp,true);
                    helper.setVisible(R.id.arrowDown,false);

                }
            });

            helper.setOnClickListener(R.id.arrowUp, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.setVisible(R.id.developerReplyContainer,false);
                    helper.setVisible(R.id.arrowUp,false);
                    helper.setVisible(R.id.arrowDown,true);
                }
            });

            helper.addOnClickListener(R.id.replyImageBtn);

            }

            //developerReply

        }

    }
