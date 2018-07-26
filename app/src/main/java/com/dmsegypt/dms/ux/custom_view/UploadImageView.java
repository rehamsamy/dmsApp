package com.dmsegypt.dms.ux.custom_view;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dmsegypt.dms.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amr on 17/05/2017.
 */

public class UploadImageView extends FrameLayout implements View.OnClickListener {
    public static final int DEFAULT_STATE = 0;
    public static final int SUCESS_STATE = 3;
    public static final int LOADING_STATE = 1;
    public static final int FAILED_STATE = 2;
    @BindView(R.id.imgv_profile)
    ImageView profileImgv;
    @BindView(R.id.imgv_close)
    ImageView closeImgv;
    @BindView(R.id.imgv_retry)
    ImageView retryImgv;
    @BindView(R.id.loading)
    ProgressBar loading;
    OnUploadImageClickListener uploadImageClickListener;

    public UploadImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public UploadImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public UploadImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_upload_image, this);
        ButterKnife.bind(this, view);
        showState(DEFAULT_STATE);
    }

    public void showState(int state) {
        loading.setVisibility(state == LOADING_STATE ? VISIBLE : GONE);
        closeImgv.setVisibility(state == SUCESS_STATE ? VISIBLE : GONE);
        retryImgv.setVisibility(state == FAILED_STATE ? VISIBLE : GONE);
        closeImgv.setOnClickListener(state == SUCESS_STATE ? this : null);
        retryImgv.setOnClickListener(state == FAILED_STATE ? this : null);
        profileImgv.setOnClickListener(state == DEFAULT_STATE ? this : null);
    }

    public void setUploadImageClickListener(OnUploadImageClickListener uploadImageClickListener) {
        this.uploadImageClickListener = uploadImageClickListener;
    }

    @Override
    public void onClick(View view) {
        if (uploadImageClickListener == null) return;
        switch (view.getId()) {
            case R.id.imgv_profile:
                uploadImageClickListener.onAddClicked(view);
                break;
            case R.id.imgv_retry:
                showState(LOADING_STATE);
                uploadImageClickListener.onRetryClicked(view);
                break;
            case R.id.imgv_close:
                showState(DEFAULT_STATE);
                profileImgv.setImageResource(R.drawable.ic_add_image_default);
                uploadImageClickListener.onCloseClicked(view);
                break;
        }

    }

    public interface OnUploadImageClickListener {
        void onCloseClicked(View view);

        void onRetryClicked(View view);

        void onAddClicked(View view);
    }
    public void PreviewImage(@NonNull Uri url){

        Glide.with(this.getContext())
                .load(url.toString())
                .error(R.drawable.ic_add_image_default)
                .override(150, 150)
                .centerCrop()
                .placeholder(R.color.grey_bg)
                .dontAnimate()
                .into(profileImgv);
    }
  public void setDefaultImage(){
      profileImgv.setImageResource(R.drawable.ic_add_image_default);
  }

}
