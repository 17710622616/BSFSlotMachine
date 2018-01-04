package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 5/1/2018.
 */

public class AllOrderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_history_order, null);
        return view;
    }
}
