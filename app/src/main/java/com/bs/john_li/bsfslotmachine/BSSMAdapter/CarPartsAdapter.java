package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarPartsOutModel;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * 選擇車輛
 * Created by John_Li on 9/8/2017.
 */

public class CarPartsAdapter extends BaseAdapter {
    private List<CarPartsOutModel.DataBeanX.CarPatsModel> list;
    private LayoutInflater inflater;
    private Context mContext;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail).build();
    public CarPartsAdapter(Context context, List<CarPartsOutModel.DataBeanX.CarPatsModel> list) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_car_parts, null);
            holder.item_car_parts_ivs = convertView.findViewById(R.id.item_car_parts_ivs);
            holder.item_car_parts_name_tv = convertView.findViewById(R.id.item_car_parts_name_tv);
            holder.item_car_parts_price_tv = convertView.findViewById(R.id.item_car_parts_price_tv);
            holder.item_car_parts_original_price_tv = convertView.findViewById(R.id.item_car_parts_original_price_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        x.image().bind(holder.item_car_parts_ivs, list.get(i).getHeadPic(), options);
        holder.item_car_parts_name_tv.setText(list.get(i).getName());
        holder.item_car_parts_price_tv.setText("MOP" + list.get(i).getCostPrice());
        holder.item_car_parts_original_price_tv.setText("原價：MOP" + list.get(i).getMarketPrice());
        holder.item_car_parts_original_price_tv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        return convertView;
    }

    class ViewHolder {
        public ImageView item_car_parts_ivs;
        public TextView item_car_parts_name_tv;
        public TextView item_car_parts_price_tv;
        public TextView item_car_parts_original_price_tv;
    }

    public void refreshListView(List<CarPartsOutModel.DataBeanX.CarPatsModel> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    /**
     * 未充值點擊接口
     */
    public interface CarUpdateCallBack {
        void carUpdateClick(View view);
    }
}
