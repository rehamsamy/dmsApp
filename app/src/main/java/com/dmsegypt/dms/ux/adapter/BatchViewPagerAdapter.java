package com.dmsegypt.dms.ux.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dmsegypt.dms.ux.Fragment.BatchDetailFragment;
import com.dmsegypt.dms.ux.Fragment.BatchSummaryFragment;

/**
 * Created by amr on 27/11/2017.
 */

public class BatchViewPagerAdapter extends FragmentStatePagerAdapter {
    private String []title={"summary","Detail"};
    public BatchViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:fragment=new BatchSummaryFragment();break;
            case 1:fragment=new BatchDetailFragment();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
