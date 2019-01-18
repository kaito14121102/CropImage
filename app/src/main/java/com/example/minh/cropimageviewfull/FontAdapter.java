package com.example.minh.cropimageviewfull;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FontAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<String> fonts;

    public FontAdapter(Context context, int layout, List<String> fonts) {
        this.context = context;
        this.layout = layout;
        this.fonts = fonts;
    }

    @Override
    public int getCount() {
        return fonts.size();
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
        TextView txtFont;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_font, null);
            holder = new ViewHolder();
            holder.txtFont = (TextView) convertView.findViewById(R.id.text_font);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String font = fonts.get(position);
        holder.txtFont.setText("Hello World");
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                font);
        holder.txtFont.setTypeface(tf);
        return convertView;
    }
}
