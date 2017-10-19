package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 我的車輛列表
 * Created by John_Li on 9/8/2017.
 */

public class CarListActivity extends BaseActivity implements View.OnClickListener, CarListAdapter.CarRechargeCallBack {
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
    // 修改的位置
    private int updatePosition = 0;

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
                    public void run() { //和最大的数据比较
                        if (pageSize * (pageNo + 1) > totolCarCount){
                            Toast.makeText(CarListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                            mExpandSwipeRefreshLayout.setLoading(false);
                        } else {
                            pageNo ++;
                            callNetGetCarList();
                        }
                    }
                }, 500);
            }
        });

        mExpandSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carModelList.clear();
                pageNo = 1;
                callNetGetCarList();
            }
        });
        carLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showCarDeleteDialog(i);
                return false;
            }
        });

        carLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CarListActivity.this, AddCarActivity.class);
                intent.putExtra("startWay", "update");
                intent.putExtra("updateModel", new Gson().toJson(carModelList.get(i)));
                updatePosition = i;
                startActivityForResult(intent, BSSMConfigtor.UPDATE_CAR_RQUEST);
            }
        });

        noCarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandSwipeRefreshLayout.setRefreshing(true);
                noCarLL.setVisibility(View.GONE);
                carModelList.clear();
                pageNo = 1;
                callNetGetCarList();
            }
        });
    }

    @Override
    public void initData() {
        carListHead.setTitle("我的車輛");
        carListHead.setLeft(this);
        carListHead.setRight(R.mipmap.push_invitation, this);

        carModelList = new ArrayList<>();
        mCarListAdapter = new CarListAdapter(this, carModelList, this);
        carLv.setAdapter(mCarListAdapter);
        mExpandSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorMineYellow),
                getResources().getColor(R.color.colorMineOringe),
                getResources().getColor(R.color.colorMineGreen));
        mExpandSwipeRefreshLayout.setRefreshing(true);
        callNetGetCarList();
    }

    /**
     * 刪除車輛的dialog
     * @param position
     */
    private void showCarDeleteDialog(final int position) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_normal)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        TextView msgTv = viewHolder.getView(R.id.dialog_normal_msg);
                        msgTv.setText("是否要刪除該車輛，刪除后車輛的會員費不會退還的哦！");
                        viewHolder.setOnClickListener(R.id.dialog_normal_no, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.dialog_normal_yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                callNetDeleteCar(position);
                                baseNiceDialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(210)
                .show(getSupportFragmentManager());
    }

    /**
     * 請求刪除車輛
     * @param position
     */
    private void callNetDeleteCar(final int position) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.DELETE_CAR + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id",carModelList.get(position).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    carModelList.remove(position);
                    mCarListAdapter.refreshListView(carModelList);
                    Toast.makeText(CarListActivity.this, "刪除成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarListActivity.this, "刪除失敗！", Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CarListActivity.this, "刪除失敗！", Toast.LENGTH_SHORT).show();
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
                    List<CarModel.CarCountAndListModel.CarInsideModel> carInsideModelsFromNet = model.getData().getData();
                    carModelList.addAll(carInsideModelsFromNet);
                    Log.d("car_list_count", "長度：" + carModelList.size());
                    // List去重
                    deWeightListById();
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

    /**
     * 請求完成，刷新界面
     */
    public void refreshView(){
        if (carModelList.size() > 0) {
            noCarLL.setVisibility(View.GONE);
        } else {
            noCarLL.setVisibility(View.VISIBLE);
        }
        mCarListAdapter.refreshListView(carModelList);
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
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra("startWay", "add");
                startActivityForResult(intent, BSSMConfigtor.ADD_CAR_RQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "添加車輛失敗！", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case 3: // 添加車輛的返回
                carModelList.add(0, new Gson().fromJson(data.getStringExtra("NEW_CAR_FROM_ADD"), CarModel.CarCountAndListModel.CarInsideModel.class));
                mCarListAdapter.refreshListView(carModelList);
                break;
            case 4: // 修改車輛的返回
                carModelList.set(updatePosition,new Gson().fromJson(data.getStringExtra("NEW_CAR_FROM_ADD"), CarModel.CarCountAndListModel.CarInsideModel.class));
                mCarListAdapter.refreshListView(carModelList);
                break;
        }
    }

    /**
     * List去重
     */
    private void deWeightListById() {
        /*Set<CarModel.CarCountAndListModel.CarInsideModel> carSet = new TreeSet<>((o1, o2) -> Integer.toString(o1.getId()).compareTo(Integer.toString(o2.getId())));
        carSet.addAll(carModelList);
        carModelList = new ArrayList<>(carSet);*/
        Set<CarModel.CarCountAndListModel.CarInsideModel> s= new TreeSet<CarModel.CarCountAndListModel.CarInsideModel>(new Comparator<CarModel.CarCountAndListModel.CarInsideModel>(){

            @Override
            public int compare(CarModel.CarCountAndListModel.CarInsideModel o1, CarModel.CarCountAndListModel.CarInsideModel o2) {
                return Integer.toString(o1.getId()).compareTo(Integer.toString(o2.getId()));
            }

        });

        s.addAll(carModelList);
        carModelList = new ArrayList<CarModel.CarCountAndListModel.CarInsideModel>(s);
    }

    @Override
    public void carRechargeClick(View view) {
        Toast.makeText(CarListActivity.this, "充值按鈕" + view.getId(), Toast.LENGTH_SHORT).show();
    }
}
