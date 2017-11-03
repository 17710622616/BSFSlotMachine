package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 2/11/2017.
 */

public class CollapsingAdapter extends PagerAdapter {
    // 图片集合
    private List<ImageView> adImgList = new ArrayList<ImageView>();

    public CollapsingAdapter(List<ImageView> adImgList) {
        this.adImgList = adImgList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(adImgList.get(position % adImgList.size()));
        return adImgList.get(position % adImgList.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(adImgList.get(position % adImgList.size()));
    }
}
