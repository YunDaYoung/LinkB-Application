package com.example.linkb.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkb.Class.RecommendEvent;
import com.example.linkb.Class.SelectEvent;
import com.example.linkb.R;

import java.net.URL;
import java.util.ArrayList;

public class SelectEventListAdapter extends RecyclerView.Adapter<SelectEventListAdapter. ViewHolder>{

    ArrayList<SelectEvent> eventList;
    Context context;

    public SelectEventListAdapter(Context context, ArrayList<SelectEvent> eventList){
        this.context = context;
        this.eventList = eventList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView selectEventTitle;
        TextView selectEventDate;
        ImageView selectEventImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            selectEventTitle = (TextView) view.findViewById(R.id.select_event_title);
            selectEventDate = (TextView) view.findViewById(R.id.select_event_date);
            selectEventImage = (ImageView) view.findViewById(R.id.select_event_image);
        }

        public void bind(SelectEvent events) {

            String startDate = events.startDate;
            String endDate = events.endDate;


            //InputStream을 쓰기 위해 다른 쓰레드에서 설정
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL(events.eventImage);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        //날짜 형식 20XX.XX.XX 로 맞추기
                        String date1 = startDate.substring(0, startDate.lastIndexOf(' '));
                        String date2 = endDate.substring(0, endDate.lastIndexOf(' '));
                        String result1 = date1.replace('-', '.');
                        String result2 = date2.replace('-', '.');

                        //UI는 메인쓰레드에서 설정
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                selectEventTitle.setText(events.name);
                                selectEventDate.setText(result1 + "~" + result2);
                                selectEventImage.setImageBitmap(bmp);
                            }
                        });
                    }catch (Exception e){
                        Log.e("bitmap error", e.toString());
                    }
                }
            };

            Thread thread1 = new Thread(r);
            thread1.start();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_event_item, viewGroup, false);
        SelectEventListAdapter.ViewHolder viewHolder = new SelectEventListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(eventList.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
