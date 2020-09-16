package com.example.linkb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkb.Adapter.RecyclerviewEventAdapter;
import com.example.linkb.Class.DetailEvent;
import com.example.linkb.Class.RecommendEvent;
import com.example.linkb.mainFrag.MainHomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EventDetail extends AppCompatActivity {

    Context context;

    Intent intent;
    String eventId;

    ScrollView scrollView;

    ImageButton beforeBtn;
    TextView detailTitle;
    ImageView detailImage;

    TextView detailDate;
    TextView detailTime;
    TextView detailMember;
    TextView detailPlace;
    Button mapBtn;

    TextView detailIntroduce;
    TextView detailCompany;

    RecyclerView recommendEventRecycler;
    Button joinBtn;

    FloatingActionButton upScrollFab;

    ArrayList<DetailEvent> detailList;
    ArrayList<RecommendEvent> recommendedEventList;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        context = EventDetail.this;
        scrollView = (ScrollView) findViewById(R.id.scollView);

        intent = getIntent();
        eventId = intent.getExtras().getString("event_id");

        beforeBtn = (ImageButton) findViewById(R.id.before_btn);
        recommendEventRecycler = (RecyclerView) findViewById(R.id.recommend_event_list);
        upScrollFab = (FloatingActionButton) findViewById(R.id.up_scroll_fab);

        new RestAPITaskRecommend("http://101.101.161.189/api/index.php/linkb_event/select_event_detail", context, detailList, eventId).execute();
        new RestAPITaskRecommend1("http://101.101.161.189/api/index.php/linkb_event/select_recommend_event_list", recommendEventRecycler, context).execute();

        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upScrollFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public class RestAPITaskRecommend extends AsyncTask<Integer, Void, ArrayList<DetailEvent>> {

        protected String mURL;
        protected Context context;
        protected ArrayList<DetailEvent> detailList;
        protected String id;

        public RestAPITaskRecommend(String mURL, Context context, ArrayList<DetailEvent> detailList, String id){
            this.mURL = mURL;
            this.context = context;
            this.detailList = detailList;
            this.id = id;
        }


        @Override
        protected ArrayList<DetailEvent> doInBackground(Integer... params) {
            try{
                mURL = mURL + "?event_idx=" + id;
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/json");
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

                JSONArray jsonArray1 = (JSONArray)getKey.get("event");
                JSONArray jsonArray2 = (JSONArray)getKey.get("company");
                JSONArray jsonArray3 = (JSONArray)getKey.get("member");
                detailList = new ArrayList<>();
                for(int i =0; i< jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject)jsonArray1.get(i);
                    JSONObject jsonObject2 = (JSONObject)jsonArray2.get(i);
                    JSONObject jsonObject3 = (JSONObject)jsonArray3.get(i);
                    detailList.add(new DetailEvent(
                            jsonObject1.get("event_idx").toString(),
                            jsonObject1.get("event_name").toString(),
                            jsonObject1.get("event_host").toString(),
                            jsonObject1.get("event_start_date").toString(),
                            jsonObject1.get("event_end_date").toString(),
                            jsonObject1.get("event_introduce").toString(),
                            jsonObject1.get("event_map").toString(),
                            jsonObject1.get("event_image").toString(),
                            jsonObject2.get("company_idx").toString(),
                            jsonObject2.get("company_name").toString(),
                            jsonObject3.get( "cnt").toString()
                            ));
                }
                return detailList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<DetailEvent> detailList) {
            detailTitle = (TextView) findViewById(R.id.detail_title);
            detailImage = (ImageView) findViewById(R.id.detail_image);
            detailDate = (TextView) findViewById(R.id.detail_date);
            detailTime = (TextView) findViewById(R.id.detail_time);
            detailMember = (TextView) findViewById(R.id.detail_member);
            detailPlace = (TextView) findViewById(R.id.detail_place);
            mapBtn = (Button) findViewById(R.id.map_btn);

            detailIntroduce = (TextView) findViewById(R.id.detail_introduce);
            detailCompany = (TextView) findViewById(R.id.detail_company);

            joinBtn = (Button) findViewById(R.id.join_btn);

            //event_start_date와 event_end_date에서 날짜, 시간 쪼개기
            String startDate = detailList.get(0).getEventStartDate();
            String endDate = detailList.get(0).getEventEndDate();
            //날짜 형식 20XX.XX.XX 로 맞추기
            String date1 = startDate.substring(0, startDate.lastIndexOf(' '));
            String date2 = endDate.substring(0, endDate.lastIndexOf(' '));
            String result1 = date1.replace('-', '.');
            String result2 = date2.replace('-', '.');
            //날짜 쪼개기 결과
            String date = result1 + " ~ " + result2;
            //시간 형식 XX:XX 로 맞추기
            date1 = startDate.substring(startDate.lastIndexOf(' ') + 1, 16);
            date2 = endDate.substring(startDate.lastIndexOf(' ') + 1, 16);
            //시간 쪼개기 결과
            String time = date1 + " ~ " + date2;

            //detail 설명 <div></div> 없애기
            String introduce = detailList.get(0).getEventIntroduce();
            introduce = introduce.replace("<div>", "");
            introduce = introduce.replace("</div>", "\n");

            //이미지 Bitmap 으로 띄우기
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(detailList.get(0).getEventImage());
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detailImage.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("Detail view Error", e.toString());
                    }
                }
            };
            Thread tr = new Thread(r);
            tr.start();

            //데이터 화면에 띄우기
            detailTitle.setText(detailList.get(0).getEventName());
            detailDate.setText(date);
            detailTime.setText(time);
            detailMember.setText(detailList.get(0).getMemberCount() + "명");
            detailPlace.setText(detailList.get(0).getEventHost());
            detailIntroduce.setText(introduce);
            detailCompany.setText(detailList.get(0).getCompanyName());

            //버튼 클릭 이벤트
            mapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(detailList.get(0).getEventMap()));
                    startActivity(intent);
                }
            });

            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), detailList.get(0).getEventName() + "신청", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //RecommendEvent 데이터 불러오기
    public class RestAPITaskRecommend1 extends AsyncTask<Integer, Void, ArrayList<RecommendEvent>> {

        protected String mURL;
        RecyclerView recyclerView;
        protected Context context;

        public RestAPITaskRecommend1(String mURL, RecyclerView recyclerView, Context context){
            this.mURL = mURL;
            this.recyclerView = recyclerView;
            this.context = context;
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
}