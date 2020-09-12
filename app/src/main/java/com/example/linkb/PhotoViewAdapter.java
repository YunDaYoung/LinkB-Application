package com.example.linkb;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewAdapter.MyViewHolder> {
    private Context context;
    private List<String> list;


    Handler handler1 = new Handler();
    Handler handler2 = new Handler();
    Handler handler3 = new Handler();
    Handler handler4 = new Handler();
    public PhotoViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final mainHomeFrag a=new mainHomeFrag();
        int index = position % list.size();
        String item = list.get(index);

        /* 보류
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    URL url1 = new URL(" https://kr.object.ncloudstorage.com/starthub-statics/linkb/cover/cover_20200818.jpg");
                    InputStream is1 = url1.openStream();
                    final Bitmap bm1 = BitmapFactory.decodeStream(is1);
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            holder.imgBanner.setImageBitmap(bm1);
                        }
                    });
                    holder.imgBanner.setImageBitmap(bm1); //비트맵 객체로 보여주기
                } catch(Exception e){

                }

            }
        });

         */

        switch(index){
            case 0:
                holder.imgBanner.setImageResource(R.drawable.cover_20200818);
                break;
            case 1:
                holder.imgBanner.setImageResource(R.drawable.cover_20200818);
                break;
            case 2:
                holder.imgBanner.setImageResource(R.drawable.cover_20200818);
                break;
            case 3:
                holder.imgBanner.setImageResource(R.drawable.cover_20200818);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBanner;
        GradientDrawable drawable=
                (GradientDrawable) context.getDrawable(R.drawable.photoview_rounding);
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            imgBanner.setBackground(drawable);
            imgBanner.setClipToOutline(true);
        }
    }






}


