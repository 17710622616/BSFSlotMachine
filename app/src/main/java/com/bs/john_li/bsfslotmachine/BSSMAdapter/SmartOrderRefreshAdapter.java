package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.InputStream;
import java.util.List;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartOrderRefreshAdapter extends RecyclerView.Adapter<SmartOrderRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> list;
    private final Context mContext;
    private OSSClient oss;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private OnItemClickListener mOnitemClickListener = null;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartOrderRefreshAdapter(Context context, List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> list, OSSClient oss) {
        this.list = list;
        this.oss = oss;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        //计算内存，并且给Lrucache 设置缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/6;
        mMemoryCache = new LruCache<String ,BitmapDrawable>(cacheSize){
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return  value.getBitmap().getByteCount();
            }
        };
    }
    @Override
    public SmartOrderRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_user_order, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_order_iv = (ImageView) view.findViewById(R.id.item_order_iv);
        vh.item_order_type = (TextView) view.findViewById(R.id.item_order_type);
        vh.item_order_no = (TextView) view.findViewById(R.id.item_order_no);
        vh.item_order_status = (TextView) view.findViewById(R.id.item_order_status);
        vh.item_order_money = (TextView) view.findViewById(R.id.item_order_money);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartOrderRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        switch (list.get(position).getOrderType()) {
            case 1:
                holder.item_order_type.setText("訂單類型：充值訂單");
                break;
            case 2:
                holder.item_order_type.setText("訂單類型： 會員續費訂單");
                break;
            case 3:
                holder.item_order_type.setText("訂單類型：投幣訂單 ");
                break;
            case 4:
                holder.item_order_type.setText("訂單類型：拍照停車訂單");
                break;
        }
        holder.item_order_no.setText("訂單編號：" +list.get(position).getOrderNo());
        holder.item_order_money.setText("訂單金額：" + String.valueOf(list.get(position).getTotalAmount()));
        switch (list.get(position).getOrderStatus()) {
            case 1:
                holder.item_order_status.setText("訂單狀態：待支付");
                break;
            case 3:
                holder.item_order_status.setText("訂單狀態：已支付");
                break;
            case 4:
                holder.item_order_status.setText("訂單狀態：已投幣");
                break;
            case 9:
                holder.item_order_status.setText("訂單狀態：已取消");
                break;
        }

        switch (list.get(position).getOrderType()) {
            case 1://充值訂單
                holder.item_order_iv.setImageResource(R.mipmap.top_up);
                holder.item_order_iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case 2://會員續費訂單
                holder.item_order_iv.setImageResource(R.mipmap.car_recharge);
                holder.item_order_iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case 3://確定投幣機訂單
                //AliyunOSSUtils.downloadImg(list.get(position).getImg1(), oss, holder.item_order_iv, mContext, R.mipmap.load_img_fail_list);
                x.image().bind(holder.item_order_iv, list.get(position).getImg1(), options);
                break;
            case 4: //未知投幣機訂單
                //AliyunOSSUtils.downloadImg(list.get(position).getImg1(), oss, holder.item_order_iv, mContext, R.mipmap.load_img_fail_list);
                x.image().bind(holder.item_order_iv, list.get(position).getImg1(), options);
                break;
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnitemClickListener != null) {
            mOnitemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_order_iv;
        public TextView item_order_type;
        public TextView item_order_no;
        public TextView item_order_money;
        public TextView item_order_status;
        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void refreshListView(List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
