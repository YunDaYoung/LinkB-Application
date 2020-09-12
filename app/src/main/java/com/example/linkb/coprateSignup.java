package com.example.linkb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class coprateSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coprate_signup);

        final Button btnSignUp = (Button)findViewById(R.id.btnSignUp);
        final LinearLayout page1 =(LinearLayout)findViewById(R.id.page1);
        final LinearLayout page2 =(LinearLayout)findViewById(R.id.page2);
        final LinearLayout page3 =(LinearLayout)findViewById(R.id.page3);
        final Button btnBack = (Button)findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page3.getVisibility()==View.VISIBLE){
                    page1.setVisibility(View.GONE);
                    page2.setVisibility(View.VISIBLE);
                    page3.setVisibility(View.GONE);
                    btnSignUp.setText("다음단계로");
                }else if(page2.getVisibility()==View.VISIBLE){
                    page1.setVisibility(View.VISIBLE);
                    page2.setVisibility(View.GONE);
                    page3.setVisibility(View.GONE);
                }else{
                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);

                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page1.getVisibility()==View.VISIBLE){
                    page1.setVisibility(View.GONE);
                    page2.setVisibility(View.VISIBLE);
                    page3.setVisibility(View.GONE);
                }else if(page2.getVisibility()==View.VISIBLE){
                    page1.setVisibility(View.GONE);
                    page2.setVisibility(View.GONE);
                    page3.setVisibility(View.VISIBLE);
                    btnSignUp.setText("가입완료!");
                }else{
                    //회원가입 코드 !

                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);
                }
            }
        });
    }
}