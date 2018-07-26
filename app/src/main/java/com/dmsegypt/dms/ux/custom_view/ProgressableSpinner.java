package com.dmsegypt.dms.ux.custom_view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dmsegypt.dms.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amr on 18/04/2018.
 */

public class ProgressableSpinner extends FrameLayout implements View.OnClickListener {
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.progress_dots)
    DotLoadingView progress;
    @BindView(R.id.retry_imgv)
    ImageView retryImgv;
    OnRetryListener onRetryListener;

    public ProgressableSpinner(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProgressableSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ProgressableSpinner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progressable_spinner_layout, this);
        ButterKnife.bind(this, view);
        retryImgv.setVisibility(GONE);
        progress.setVisibility(GONE);
        retryImgv.setOnClickListener(this);

    }
    public void showLoading(){
        progress.setVisibility(VISIBLE);
        retryImgv.setVisibility(GONE);
    }
    public void showSucess(){
        progress.setVisibility(GONE);
        retryImgv.setVisibility(GONE);
    }
    public void showFailure(){
        progress.setVisibility(GONE);
        retryImgv.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (onRetryListener!=null){
            onRetryListener.onRetry();
        }
    }

    public interface OnRetryListener{
        public void onRetry();
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }
 public MaterialSpinner getSpinner(){
     return spinner;
 }




}
