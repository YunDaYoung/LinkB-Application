package com.example.linkb;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class mainHomeFrag extends Fragment {
    ;
    DrawerLayout maindrawer;
    View drawerView;
    ImageButton close_drawer;
    ListView main_list;
    ViewPager2 photoview;
    CircleIndicator3 indicator;
    ImageView user_img;
    boolean photo_isFirst = true;


    ArrayList<Main_SampleData> titleDataList;
    ArrayList<RecommendEventItem> mList = new ArrayList<RecommendEventItem>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_logined_main_frag1, container, false);

        //
        photoview = view.findViewById(R.id.photoview);
        maindrawer = view.findViewById(R.id.main_drawer_layout);
        //드러워블 뷰
        drawerView = view.findViewById(R.id.main_nav_view);
        close_drawer = view.findViewById(R.id.btn_CloseDrawer);
        //슬라이드 뷰 리스트
        main_list = view.findViewById(R.id.main_nav_list);
        //튤바 설정하기
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_nav_toolbar);
        indicator = view.findViewById(R.id.photoview_indicator);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        //사용자 사진
        user_img = view.findViewById(R.id.userRoundImage);

        GradientDrawable drawable=
                (GradientDrawable) getContext().getDrawable(R.drawable.main_nav_image_round);

        user_img.setBackground(drawable);
        user_img.setClipToOutline(true);


        //추천 이벤트
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        //프래그를 누를때마다 계속 아이템이 추가되어 구현
        mList.clear();
        new RestAPITaskRecommend("http://101.101.161.189/api/index.php/linkb_event/select_recommend_event_list", recyclerView).execute();






        //
        this.InitializeListData();
        final Main_list_Adapter myAdapter = new Main_list_Adapter(getActivity(), titleDataList);
        main_list.setAdapter(myAdapter);

        //드러워블 리스트뷰 클릭 리스너
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),
                        myAdapter.getItem(position).getContext(),
                        Toast.LENGTH_SHORT).show();
            }
        });


        close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maindrawer.closeDrawer(drawerView);
            }
        });


        // 보류 // new RestAPITaskCover("http://101.101.161.189/api/index.php/linkb_cover/select_cover_list").execute();
        List<String> items = new ArrayList<String>();
        items.add("items1");
        items.add("items2");
        items.add("items3");
        items.add("items4");
        final PhotoViewAdapter adapter = new PhotoViewAdapter(getActivity().getApplicationContext(), items);
        photoview.setAdapter(adapter);
        photoview.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        photoview.setOffscreenPageLimit(4);
        if (photo_isFirst == true) {
            photoview.setCurrentItem(adapter.getItemCount() / 2); // 아이템 개수의 중간지점에서 시작
            photo_isFirst = false;
        }
        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);
        indicator.setViewPager(photoview);
        indicator.createIndicators(5, 0);
        photoview.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffSet = position * -(2 * pageOffset + pageMargin);
                if (position < -1) {
                    page.setTranslationX(-myOffSet);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(myOffSet);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(myOffSet);
                }
            }
        });


        //포토뷰 끝


        photoview.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                indicator.animatePageSelected(position % 5);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    /* 2020-08-26 이재헌. 메인커버리스트 추가 (보류)
    class RestAPITaskCover extends AsyncTask<Integer, Void, String> {
        protected String mURL;

        public RestAPITaskCover(String url) {
            this.mURL = url;
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("apikey", "starthub");

                StringBuffer buffer = new StringBuffer();

                OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

                PrintWriter writer = new PrintWriter(outputStream);
                writer.write(buffer.toString());
                System.out.println(buffer.toString());
                writer.flush();
                writer.close();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }
                String result = builder.toString();
                System.out.println(result);

                String coverUrl="https://kr.object.ncloudstorage.com/starthub-statics/linkb/cover/cover_20200818.jpg";
                return coverUrl;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String coverUrl) {
            url1=coverUrl;
        }
    }

     */


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                maindrawer.openDrawer(drawerView);
                return true;
            }
            case R.id.action_refresh:
                Toast.makeText(getActivity().getApplicationContext(), "로그아웃 (비상 탈출)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), login.class);
                startActivity(intent);
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.icon_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    public void InitializeListData() {
        titleDataList = new ArrayList<Main_SampleData>();

        titleDataList.add(new Main_SampleData(R.drawable.ic_baseline_list_alt_24, "행사목록"));
        titleDataList.add(new Main_SampleData(R.drawable.ic_baseline_link_24, "행사참여하기"));

    }

    public void addItem(String img, String title, String day) {
        RecommendEventItem item = new RecommendEventItem();

        item.setDay(day);
        item.setTitle(title);
        item.setImageResource(img);
        mList.add(item);
    }


    public class RestAPITaskRecommend extends AsyncTask<Integer, Void, HashMap<String, String[]>> {

        protected String mURL;
        RecyclerView recyclerView;

        public RestAPITaskRecommend(String mURL, RecyclerView recyclerView){
            this.mURL = mURL;
            this.recyclerView = recyclerView;
        }


        @Override
        protected HashMap<String, String[]> doInBackground(Integer... params) {
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

                JSONObject jsonObject = new JSONObject(result);

                JSONArray parseArray = (JSONArray)jsonObject.get("event_list");

                String[] event_name = new String[parseArray.length()];
                String[] event_host = new String[parseArray.length()];
                String[] event_start_date = new String[parseArray.length()];
                String[] event_end_date = new String[parseArray.length()];
                String[] event_location = new String[parseArray.length()];
                String[] event_img = new String[parseArray.length()];


                for(int i =0; i < parseArray.length(); i++){
                    JSONObject contentObject = (JSONObject)parseArray.get(i);

                    System.out.println(contentObject.get("event_idx"));
                    System.out.println(contentObject.get("event_name"));
                    System.out.println(contentObject.get("event_host"));
                    System.out.println(contentObject.get("event_start_date"));
                    System.out.println(contentObject.get("event_end_date"));
                    System.out.println(contentObject.get("event_location"));
                    System.out.println(contentObject.get("event_use"));
                    System.out.println(contentObject.get("event_sort"));


                    event_img[i] = contentObject.get("event_image").toString();
                    event_name[i] = contentObject.get("event_name").toString();
                    event_host[i] = contentObject.get("event_host").toString();
                    event_start_date[i] = contentObject.get("event_start_date").toString();
                    event_end_date[i] = contentObject.get("event_end_date").toString();
                    event_location[i] = contentObject.get("event_location").toString();
                }
                HashMap<String, String[]> stringHashMap = new HashMap<>();
                stringHashMap.put("event_name", event_name);
                stringHashMap.put("event_host", event_host);
                stringHashMap.put("event_start_date", event_start_date);
                stringHashMap.put("event_end_date", event_end_date);
                stringHashMap.put("event_location", event_location);
                stringHashMap.put("event_img", event_img);

                return stringHashMap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, String[]> s) {

            String[] title = s.get("event_name");
            String[] startday = s.get("event_start_date");
            String[] endday = s.get("event_end_date");
            String[] img = s.get("event_img");

            System.out.println(title[title.length-1]);

            int tempsize =0;
            if(title.length < 10){
                tempsize = title.length;
            }else{
                tempsize = 10;
            }

            for(int i =0; i <tempsize; i++){
                addItem(img[i], title[i], startday[i].substring(0, startday[i].indexOf(" "))+"~"+endday[i].substring(0, endday[i].indexOf(" ")));
                System.out.println(img[i]);
            }

            EventAdapter eventAdapter = new EventAdapter(mList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            recyclerView.setAdapter(eventAdapter);

            super.onPostExecute(s);
        }
    }


}