package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 10/8/2017.
 */

public class OpinionPhotoAdapter extends BaseAdapter {
    private List<Integer> photoList;
    private LayoutInflater inflater;
    private Context mContext;
    public OpinionPhotoAdapter(Context context, List<Integer> photoList) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_opinion, null);
            holder.opinionPhoto = convertView.findViewById(R.id.item_opinion_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.opinionPhoto.setImageResource(photoList.get(i));
        return convertView;
    }

    class ViewHolder {
        private ImageView opinionPhoto;
    }
}
