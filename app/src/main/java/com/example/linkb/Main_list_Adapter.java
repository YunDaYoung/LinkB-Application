package com.example.linkb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Main_list_Adapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Main_SampleData> sample;

    public Main_list_Adapter(Context context, ArrayList<Main_SampleData> data){
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public Main_SampleData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.main_nav_row,null);
        TextView title = view.findViewById(R.id.textView1);
        ImageView image = view.findViewById(R.id.listImageView);

        title.setText(sample.get(position).getContext());
        image.setImageResource(sample.get(position).getTitle());
        return view;
    }
}
