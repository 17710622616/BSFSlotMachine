package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CarListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.ExpandSwipeRefreshLayout;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的車輛列表
 * Created by John_Li on 9/8/2017.
 */

public class CarListActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView carListHead;
    private ListView carLv;
    private ExpandSwipeRefreshLayout mExpandSwipeRefreshLayout;
    private LinearLayout noCarLL;

    private List<String> carList;
    private List<CarModel.CarCountAndListModel.CarInsideModel> carModelList;
    private CarListAdapter mCarListAdapter;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        carListHead = findViewById(R.id.carlist_head);
        carLv = findViewById(R.id.carlist_lv);
        mExpandSwipeRefreshLayout = findViewById(R.id.car_list_expand_swipe);
        noCarLL = findViewById(R.id.no_car_ll);
    }

    @Override
    public void setListener() {
        mExpandSwipeRefreshLayout.setOnLoadListener(new ExpandSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mExpandSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNo ++;
                        callNetGetCarList();
                        carList.add("私家車");
                        carList.add("電單車");
                        carList.add("私家車");
                        carList.add("電單車");
                        carList.add("私家車");
                        mCarListAdapter.notifyDataSetChanged();
                        // 加载完后调用该方法
                        mExpandSwipeRefreshLayout.setLoading(false);
                    }
                }, 2000);
            }
        });

        mExpandSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carModelList.clear();
                pageNo = 1;
                callNetGetCarList();
                carList.clear();
                carList.add("私家車");
                carList.add("電單車");
                carList.add("私家車");
                carList.add("電單車");
                carList.add("私家車");
                mCarListAdapter.notifyDataSetChanged();
                // 更新完后调用该方法结束刷新
                mExpandSwipeRefreshLayout.setRefreshing(false);
            }
        });
        carLv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public void initData() {
        carListHead.setTitle("我的車輛");
        carListHead.setLeft(this);
        carListHead.setRight(R.mipmap.push_invitation, this);

        carModelList = new ArrayList<>();
        mExpandSwipeRefreshLayout.setRefreshing(true);
        callNetGetCarList();
        carList = new ArrayList<String>();
        carList.add("私家車");
        carList.add("電單車");
        carList.add("私家車");
        carList.add("電單車");
        carList.add("私家車");

        mCarListAdapter = new CarListAdapter(this, carList);
        carLv.setAdapter(mCarListAdapter);

        mExpandSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorMineYellow),
                getResources().getColor(R.color.colorMineOringe),
                getResources().getColor(R.color.colorMineGreen));
    }

    /**
     * 獲取車輛列表
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CarModel model = new Gson().fromJson(result.toString(), CarModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    carModelList = model.getData().getData();
                    Log.d("car_list_count", "長度：" + carModelList.size());
                } else if (model.getCode() == 10001){
                    Toast.makeText(CarListActivity.this, model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarListActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CarListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                refreshView();
            }
        });
    }

    public void refreshView(){
        if (carModelList.size() > 0) {
            noCarLL.setVisibility(View.GONE);
            mExpandSwipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            noCarLL.setVisibility(View.VISIBLE);
            mExpandSwipeRefreshLayout.setVisibility(View.GONE);
        }

        if (mExpandSwipeRefreshLayout.isRefreshing()) {
            mExpandSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                startActivityForResult(new Intent(this, AddCarActivity.class), BSSMConfigtor.ADD_CAR_RQUEST);
                break;
        }
    }
}
