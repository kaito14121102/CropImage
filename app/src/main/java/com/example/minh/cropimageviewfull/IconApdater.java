package com.example.minh.cropimageviewfull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class IconApdater extends BaseAdapter {
    private Context mCntext;
    private int mLayout;
    private List<Integer> mIcons;

    public IconApdater(Context mCntext, int mLayout, List<Integer> mIcons) {
        this.mCntext = mCntext;
        this.mLayout = mLayout;
        this.mIcons = mIcons;
    }

    @Override
    public int getCount() {
        return mIcons.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        ImageView imageIcon;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mCntext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_icon, null);
            holder = new ViewHolder();
            holder.imageIcon = (ImageView) convertView.findViewById(R.id.image_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int icon = mIcons.get(position);
        holder.imageIcon.setImageResource(icon);
        return convertView;
    }
}
