package com.dmsegypt.dms.ux.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
   // @BindView(R.id.civAvatar)
   // CircleImageView civAvatar;


    @BindView(R.id.tvProfileTitle)
    TextView tvProfileTitle;
    @BindView(R.id.tvNationalID)
    TextView tvNationalID;
    @BindView(R.id.tvCardId)
    TextView tvCardId;
    @BindView(R.id.tvCardIdLabel)
    TextView tvCardIdLabel;
    @BindView(R.id.tvMobileNumber)
    TextView tvMobileNumber;
    @BindView(R.id.tvEmail)
    TextView tvEmail;







    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().findViewById(R.id.fragment_home_appBar).setElevation(0);
        }
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }



    private void init() {
        if (App.getInstance().getPrefManager().isLoggedIn()) {
            User user = App.getInstance().getPrefManager().getCurrentUser();
            tvProfileTitle.setText(user.getFirstName());
            tvNationalID.setText(user.getNationalId());
            tvCardId.setText(user.getCardId());
            tvMobileNumber.setText(user.getMobile());
            tvEmail.setText(user.getEmail());




          /*
            List<Animator> animations = new_image ArrayList<Animator>();

            animations.add(ObjectAnimator.ofInt(tv_number, "left", 100, 1000).setDuration(10000));
            animations.add(ObjectAnimator.ofFloat(tv_number, "textSize", 10, 20).setDuration(10000));

            final AnimatorSet animatorSet = new_image AnimatorSet();
            animatorSet.playTogether(animations);
            animatorSet.start();

            Handler handler = new_image Handler();
            handler.postDelayed(new_image Runnable() {
                @Override
                public void run() {
                    animatorSet.cancel();
                }
            }, 5000);
*/




            if (App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER)) {
                tvCardId.setVisibility(View.GONE);
                tvCardIdLabel.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.btnEditProfile)
    public void onEditClicked(View view) {
          IntentManager.startBaseBackActivity(getActivity(), IntentManager.ACTIVITY_EDIT_PROFILE,view);


    }


}
