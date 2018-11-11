package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.GuoJiangLongActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CarServiceAdapter;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollListView;
import com.bs.john_li.bsfslotmachine.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by John_Li on 9/11/2018.
 */

public class CarServiceFragment extends BaseFragment {
    public static String TAG = CarServiceFragment.class.getName();
    private View carServiceView;
    private BSSMHeadView headView;
    private RefreshLayout mRefreshLayout;
    private NoScrollGridView mGv;
    private NoScrollListView mLv;

    //
    private CarServiceAdapter mCarServiceAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        carServiceView = inflater.inflate(R.layout.fragment_car_service, null);
        initView();
        setListenter();
        initData();
        return carServiceView;
    }

    @Override
    public void initView() {
        headView = carServiceView.findViewById(R.id.car_service_head);
        mRefreshLayout = (RefreshLayout) carServiceView.findViewById(R.id.car_service_srl);
        mGv = carServiceView.findViewById(R.id.car_service_gv);
        mLv = carServiceView.findViewById(R.id.car_service_lv);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(getActivity()) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListenter() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        getActivity().startActivity(new Intent(getActivity(), GuoJiangLongActivity.class));
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("汽車服務");

        // 初始化橫幅

        // 初始化車輛服務的菜單GridView
        mCarServiceAdapter = new CarServiceAdapter(getActivity());
        mGv.setAdapter(mCarServiceAdapter);
    }
}
