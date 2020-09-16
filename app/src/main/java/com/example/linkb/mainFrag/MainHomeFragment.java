package com.example.linkb.mainFrag;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.linkb.Adapter.RecyclerviewEventAdapter;
import com.example.linkb.Adapter.SelectEventListAdapter;
import com.example.linkb.Class.MainCover;
import com.example.linkb.Class.RecommendEvent;
import com.example.linkb.Class.SelectEvent;
import com.example.linkb.R;
import com.example.linkb.main;
import com.example.linkb.roundImageView.RoundImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainHomeFragment extends Fragment {

    Context context;
    Activity activity;

    ImageButton menuBtn;
    ImageButton refreshBtn;

    ViewFlipper imageSlide;
    TextView indicatorLine1;
    TextView indicatorLine2;
    TextView indicatorLine3;
    TextView indicatorLine4;
    TextView indicatorLine5;

    RecyclerView selectEventRecyclerView;
    RecyclerView recommendedEventRecyclerView;

    ArrayList<MainCover> coverList;
    ArrayList<SelectEvent> selectEventList;
    ArrayList<RecommendEvent> recommendedEventList;
    ArrayList<TextView> lineList;

    int num;
    Bitmap bmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_main_home, container, false);

        menuBtn = (ImageButton) view.findViewById(R.id.main_menu_btn);
        refreshBtn = (ImageButton) view.findViewById(R.id.main_refresh_btn);


        imageSlide = (ViewFlipper) view.findViewById(R.id.image_slide);
        indicatorLine1 = (TextView) view.findViewById(R.id.indicator_line1);
        indicatorLine2 = (TextView) view.findViewById(R.id.indicator_line2);
        indicatorLine3 = (TextView) view.findViewById(R.id.indicator_line3);
        indicatorLine4 = (TextView) view.findViewById(R.id.indicator_line4);
        indicatorLine5 = (TextView) view.findViewById(R.id.indicator_line5);

        lineList = new ArrayList<>();
        lineList.add(indicatorLine1);
        lineList.add(indicatorLine2);
        lineList.add(indicatorLine3);
        lineList.add(indicatorLine4);
        lineList.add(indicatorLine5);

        context = view.getContext();
        activity = getActivity();

        selectEventRecyclerView = (RecyclerView) view.findViewById(R.id.select_event_list);
        recommendedEventRecyclerView = (RecyclerView) view.findViewById(R.id.recommend_event_list);

        new RestAPITaskRecommend("http://101.101.161.189/api/index.php/linkb_cover/select_cover_list", imageSlide, context, activity).execute();
        new RestAPITaskRecommend1("http://101.101.161.189/api/index.php/linkb_event/select_recommend_event_list", recommendedEventRecyclerView, context, activity).execute();
        new RestAPITaskRecommend2("http://101.101.161.189/api/index.php/linkb_event/select_event_list", selectEventRecyclerView, context, activity).execute();



        //Menu 열기
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((main)getContext()).openMenu();
            }
        });

//        refreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((main)getContext()).refreshing();
//            }
//        });
        return view;
    }

    //Main cover 데이터 불러오기
    public class RestAPITaskRecommend extends AsyncTask<Integer, Void, ArrayList<MainCover>> {

        protected String mURL;
        protected ViewFlipper imageSlide;
        protected Context context;
        protected Activity activity;

        public RestAPITaskRecommend(String mURL, ViewFlipper imageSlide, Context context, Activity activity){
            this.mURL = mURL;
            this.imageSlide = imageSlide;
            this.context = context;
            this.activity = activity;
        }


        @Override
        protected ArrayList<MainCover> doInBackground(Integer... params) {
            try{
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("apikey", "starthub");

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();

                String str;
                String startLine;
                boolean start = false;
                while((str = reader.readLine()) != null){
                    if(!start){
                        if(str.indexOf("{")!=-1) {
                            startLine = str.substring(str.indexOf("{"));
                            builder.append(startLine + "\n");
                            start = true;
                        }
                    } else{
                        builder.append(str+ "\n");
                    }
                }
                String result = builder.toString();


                JSONObject getKey = new JSONObject(result);

                JSONArray jsonArray = (JSONArray)getKey.get("cover");

                coverList = new ArrayList<>();
                for(int i =0; i< jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    coverList.add(new MainCover(
                            jsonObject.get("cover_idx").toString(),
                            jsonObject.get("cover_mobile").toString(),
                            jsonObject.get("cover_link").toString()
                    ));
                }
                return coverList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MainCover> coverList) {

            //뷰플리퍼(슬라이드 기능)에 이미지 갯수에 맞게 추가
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    for(num=0; num<coverList.size(); num++){
                        try{
                            URL url = new URL(coverList.get(num).getMobileImage());
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //이미지 세팅
                                    RoundImageView img = new RoundImageView(context);
                                    final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, context.getResources().getDisplayMetrics());
                                    img.setLayoutParams(new ViewFlipper.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                    ViewFlipper.LayoutParams ip = (ViewFlipper.LayoutParams)img.getLayoutParams();
                                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    ip.gravity = Gravity.CENTER;
                                    img.setImageBitmap(bmp);
                                    imageSlide.addView(img);
                                }
                            });
                        } catch (Exception e){
                            Log.e("Flipper Image Error", e.toString());
                        }
                    }
                }
            };
            Thread thread1 = new Thread(r);
            thread1.start();

            //이미지 슬라이드 설정 - 왼쪽에서 등장 애니메이션
            Animation showIn = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
            imageSlide.setInAnimation(showIn);
            //이미지 슬라이드 설정 - 오른쪽으로 퇴장 애니메이션
            imageSlide.setOutAnimation(context, R.anim.slide_in_right);

            //이미지 슬라이드 설정 - 자동 슬라이딩
            imageSlide.setFlipInterval(4000);//플리핑 간격(1000ms)
            imageSlide.startFlipping();

        }
    }

    //RecommendEvent 데이터 불러오기
    public class RestAPITaskRecommend1 extends AsyncTask<Integer, Void, ArrayList<RecommendEvent>> {

        protected String mURL;
        RecyclerView recyclerView;
        protected Context context;
        protected Activity activity;

        public RestAPITaskRecommend1(String mURL, RecyclerView recyclerView, Context context, Activity activity){
            this.mURL = mURL;
            this.recyclerView = recyclerView;
            this.context = context;
            this.activity = activity;
        }


        @Override
        protected ArrayList<RecommendEvent> doInBackground(Integer... params) {
            try{
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("apikey", "starthub");

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while((str = reader.readLine()) != null){
                    builder.append(str+ "\n");
                }
                String result = builder.toString();


                JSONObject getKey = new JSONObject(result);

                JSONArray jsonArray = (JSONArray)getKey.get("event_list");


                recommendedEventList = new ArrayList<>();
                for(int i =0; i< jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    recommendedEventList.add(new RecommendEvent(
                            jsonObject.get("event_idx").toString(),
                            jsonObject.get("event_name").toString(),
                            jsonObject.get("event_host").toString(),
                            jsonObject.get("event_start_date").toString(),
                            jsonObject.get("event_end_date").toString(),
                            jsonObject.get("event_location").toString(),
                            jsonObject.get("event_image").toString()
                    ));
                }

                return recommendedEventList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecommendEvent> eventList) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            RecyclerviewEventAdapter adapter = new RecyclerviewEventAdapter(context, eventList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

        }
    }

    //SelectEvent 데이터 불러오기
    public class RestAPITaskRecommend2 extends AsyncTask<Integer, Void, ArrayList<SelectEvent>> {

        protected String mURL;
        RecyclerView recyclerView;
        protected Context context;
        protected Activity activity;

        public RestAPITaskRecommend2(String mURL, RecyclerView recyclerView, Context context, Activity activity){
            this.mURL = mURL;
            this.recyclerView = recyclerView;
            this.context = context;
            this.activity = activity;
        }


        @Override
        protected ArrayList<SelectEvent> doInBackground(Integer... params) {
            try{
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("apikey", "starthub");

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();

                String str;
                while((str = reader.readLine()) != null){
                    builder.append(str+ "\n");
                }
                String result = builder.toString();

                JSONObject getKey = new JSONObject(result);

                JSONArray jsonArray = (JSONArray)getKey.get("event_list");

                selectEventList = new ArrayList<>();
                for(int i =0; i< jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    selectEventList.add(new SelectEvent(
                            jsonObject.get("event_idx").toString(),
                            jsonObject.get("event_name").toString(),
                            jsonObject.get("event_host").toString(),
                            jsonObject.get("event_start_date").toString(),
                            jsonObject.get("event_end_date").toString(),
                            jsonObject.get("event_location").toString(),
                            jsonObject.get("event_image").toString()
                    ));
                }

                return selectEventList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SelectEvent> eventList) {

            GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
            SelectEventListAdapter adapter = new SelectEventListAdapter(context, eventList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

        }
    }
}