package com.cocacola.john_li.bsfmerchantsversionapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 各種訂單fragment的適配器
 * Created by John_Li on 5/1/2018.
 */

public class OrderFgAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments ;
    private List<String> mTitles ;

    public OrderFgAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
