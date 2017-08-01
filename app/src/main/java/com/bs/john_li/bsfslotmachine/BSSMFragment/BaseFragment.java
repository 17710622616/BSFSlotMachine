package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.support.v4.app.Fragment;

/**
 * Created by John_Li on 28/7/2017.
 */

public abstract class BaseFragment extends Fragment {
    public abstract void initView();
    public abstract void setListenter();
    public abstract void initData();
}
