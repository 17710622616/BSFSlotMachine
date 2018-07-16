package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by John_Li on 13/10/2017.
 */

public class OrderPhotoAdapter extends BaseAdapter {
    private List<String> photoList;
    private LayoutInflater inflater;
    private Context mContext;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.load_img_fail).build();

    public OrderPhotoAdapter(Context context, List<String> photoList) {
        this.photoList = photoList;
        inflater = LayoutInflater.from(context);
        mContext = context;
    }
    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int i) {
        return photoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        OrderPhotoAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new OrderPhotoAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_opinion, null);
            holder.imgPhoto = convertView.findViewById(R.id.item_opinion_iv);
            convertView.setTag(holder);
        } else {
            holder = (OrderPhotoAdapter.ViewHolder) convertView.getTag();
        }

        x.image().bind(holder.imgPhoto, photoList.get(i), options);
        return convertView;
    }

    class ViewHolder {
        private ImageView imgPhoto;
    }

    public void refreshData(List<String> newList) {
        this.photoList = newList;
        notifyDataSetChanged();
    }
}
