package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.EditText;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.utils.SlideViewPagerTransformer;
import com.dmsegypt.dms.ux.adapter.BatchViewPagerAdapter;
import com.dmsegypt.dms.ux.dialogs.PharmcySearchDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * Created by amr on 27/11/2017.
 */

public class BatchActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        iniView();
    }
    void iniView() {
        viewPager.setPageTransformer(true,new SlideViewPagerTransformer(SlideViewPagerTransformer.TransformType.ZOOM));
        viewPager.setAdapter(new BatchViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }





    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_batch;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_batch;
    }
}
