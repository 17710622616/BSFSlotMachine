package com.bs.john_li.googlemaptest.BSSMFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.john_li.googlemaptest.R;

/**
 * Created by John_Li on 18/8/2017.
 */

public class SearchFragment extends Fragment {
    public static String TAG = SearchFragment.class.getName();
    private View searchFg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        searchFg = inflater.inflate(R.layout.fragment_search, null);
        return searchFg;
    }
}
