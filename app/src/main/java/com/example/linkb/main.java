package com.example.linkb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.linkb.mainFrag.MainHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class main extends AppCompatActivity {

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private MainHomeFragment menu1Fragment;
    private mainSearchFrag menu2Fragment = new mainSearchFrag();
    private mainAlertFrag menu3Fragment = new mainAlertFrag();
    private mainTempFrag menu4Fragment = new mainTempFrag();

    //드로워레이아웃
    View drawerView;
    DrawerLayout maindrawer;
    ImageButton close_drawer;
    ImageView user_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined_main);

        refreshing();

    }

    public void refreshing(){
        menu1Fragment = new MainHomeFragment();
        //Drawer 뷰
        maindrawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawerView = (View) findViewById(R.id.main_nav_view);
        close_drawer = (ImageButton) findViewById(R.id.btn_CloseDrawer);
        user_img = (ImageView) findViewById(R.id.userRoundImage);

        //Drawer 내부 이미지 세팅 = Menu 사용자 이미지 세팅
        GradientDrawable drawable=
                (GradientDrawable) main.this.getDrawable(R.drawable.main_nav_image_round);
        user_img.setBackground(drawable);
        user_img.setClipToOutline(true);

        //BottomView 세팅
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconSize(100);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.logined_main_frame_layout, menu1Fragment).commitAllowingStateLoss();

        //DrawerLayout창 닫기 이벤트 = Menu 닫기
        close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maindrawer.closeDrawer(drawerView);
            }
        });

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.homeItem: {
                        transaction.replace(R.id.logined_main_frame_layout, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.searchItem: {
                        transaction.replace(R.id.logined_main_frame_layout, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.alertItem: {
                        transaction.replace(R.id.logined_main_frame_layout, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.tempItem: {
                        transaction.replace(R.id.logined_main_frame_layout, menu4Fragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
        //포토뷰
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshing();
    }

    @Override
    public void onBackPressed() {
        // Default
        backPressHandler.onBackPressed();
    }

    public void openMenu(){
        maindrawer.openDrawer(drawerView);
    }
}

