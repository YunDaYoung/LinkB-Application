package com.example.linkb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class main extends AppCompatActivity {

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private mainHomeFrag menu1Fragment = new mainHomeFrag();
    private mainSearchFrag menu2Fragment = new mainSearchFrag();
    private mainAlertFrag menu3Fragment = new mainAlertFrag();
    private mainTempFrag menu4Fragment = new mainTempFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconSize(100);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.logined_main_frame_layout, menu1Fragment).commitAllowingStateLoss();

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
    public void onBackPressed() {
        // Default
        backPressHandler.onBackPressed();
    }
}

