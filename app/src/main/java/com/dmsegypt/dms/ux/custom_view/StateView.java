package com.dmsegypt.dms.ux.custom_view;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dmsegypt.dms.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateView extends FrameLayout implements View.OnClickListener {
    public static final int LOADING=0;
    public static  final int NO_CONNECTION=1;
    public static  final int EMPTY=2;
    @BindView(R.id.progress)
    ImageView progressBar;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.connectionView)
    View connectionView;
    @BindView(R.id.retry_btn)
    Button retryButton;

    OnRetrListener listener;

    public StateView(@NonNull Context context) {
        super(context);
        initView(context);

    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }
    void initView(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.state_view_layout,this);
        ButterKnife.bind(this,view);
       // showState(LOADING);
        Drawable drawable=progressBar.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable){
            ((AnimatedVectorDrawable)drawable).start();
        } else  if(drawable instanceof AnimatedVectorDrawableCompat){
            ((AnimatedVectorDrawableCompat)drawable).start();


        }




    }

   public void showState(int state){
        progressBar.setVisibility(state == LOADING ? VISIBLE : GONE);
        emptyView.setVisibility(state == EMPTY ? VISIBLE : GONE);
        connectionView.setVisibility(state == NO_CONNECTION ? VISIBLE : GONE);
        retryButton.setOnClickListener(state==NO_CONNECTION ?this:null);
    }

    public void setListener(OnRetrListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onRetry();
        }
    }

    public interface  OnRetrListener{
        void onRetry();
    }









}
