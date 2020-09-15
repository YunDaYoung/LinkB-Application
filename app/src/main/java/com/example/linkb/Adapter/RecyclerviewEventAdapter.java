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
import com.example.linkb.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerviewEventAdapter extends RecyclerView.Adapter<RecyclerviewEventAdapter. ViewHolder> {

    public ArrayList<RecommendEvent> eventList;
    public Context context;

    public RecyclerviewEventAdapter(Context context, ArrayList<RecommendEvent> eventList){
        Log.e("e1", "pass");
        this.context = context;
        this.eventList = eventList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recommendEventDDay;
        TextView recommendEventTitle;
        TextView recommendEventDate;
        ImageView recommendEventImage;

        public ViewHolder(View view){
            super(view);
            recommendEventDDay = (TextView)view.findViewById(R.id.recommend_event_d_day);
            recommendEventTitle = (TextView)view.findViewById(R.id.recommend_event_title);
            recommendEventDate = (TextView)view.findViewById(R.id.recommend_event_date);
            recommendEventImage = (ImageView)view.findViewById(R.id.recommend_event_image);
        }

        public void bind(RecommendEvent events) {
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

                        //D-day 계산
                        String[] array = date1.split("-");
                        int dYear = Integer.parseInt(array[0]);
                        int dMonth = Integer.parseInt(array[1]);
                        int dDay = Integer.parseInt(array[2]);


                        //UI는 메인쓰레드에서 설정
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recommendEventDDay.setText("D" + caldate(dYear, dMonth, dDay));
                                recommendEventTitle.setText(events.name);
                                recommendEventDate.setText(result1 + "~" + result2);
                                recommendEventImage.setImageBitmap(bmp);
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
        View view = LayoutInflater.from(context).inflate(R.layout.recommended_event_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

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

    public String caldate(int myear, int mmonth, int mday) {
        try {
            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            Calendar dday = Calendar.getInstance();

            mmonth -= 1; // 받아온날자에서 -1을 해줘야함.
            dday.set(myear,mmonth,mday);// D-day의 날짜를 입력합니다.

            long day = dday.getTimeInMillis()/86400000; // 각각 날의 시간 값을 얻어온 다음 //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )
            long tday = today.getTimeInMillis()/86400000;

            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.

            if((int)count >= 0){
                return "-Day";   //당일이면 Day 출력
            }
            else {
                return String.valueOf(count); // 날짜는 하루 + 시켜줘야합니다.
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}