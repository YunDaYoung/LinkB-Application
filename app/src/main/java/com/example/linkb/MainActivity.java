package com.example.linkb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    private ViewPager mViewPager;
    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        new RestAPITaskLogin("http://101.101.161.189/api/index.php/linkb_member/login_member",
                SharedPreference.getAttribute(getApplicationContext(), "userId"),
                SharedPreference.getAttribute(getApplicationContext(), "userPwd")).execute();
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);


        Button btnStartLinkB = (Button)findViewById(R.id.viewpager_button);

        btnStartLinkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
            }
        });


    }
    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new MainActivityFrag1(), "1");
        adapter.addFragment(new MainActivityFrag2(), "2");
        adapter.addFragment(new MainActivityFrag3(), "3");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        // Default
        backPressHandler.onBackPressed();
    }

    public class RestAPITaskLogin extends AsyncTask<Integer, Void, String> {
        protected String mURL;
        protected String id, pwd;

        public RestAPITaskLogin(String url, String id, String pwd) {
            this.mURL = url;
            this.id = id;
            this.pwd = pwd;
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("apikey", "starthub");

                StringBuffer buffer = new StringBuffer();
                buffer.append("email").append("=").append(id).append("&");
                buffer.append("password").append("=").append(pwd);

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

                JSONObject jsonObject = new JSONObject(result);

                JSONObject postObject = jsonObject.getJSONObject("code");
                System.out.println(postObject.toString());
                System.out.println();
                String code = postObject.getString("code");
                String message = postObject.getString("message");
                System.out.println("code:" + code);
                System.out.println("message:" + message);

                return code;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String code) {

            if (code.equals("207")) {
                
            } else if (code.equals("200")) {
                SharedPreference.setAttribute(getApplicationContext(), "userId", id);
                SharedPreference.setAttribute(getApplicationContext(), "userPwd", pwd);
                Intent intent = new Intent(getApplicationContext(), main.class);
                startActivity(intent);
                finish();

            } else if (code.equals("206")) {
                Toast.makeText(getApplicationContext(), "API Key가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            } else if (code.equals("500")) {
                Toast.makeText(getApplicationContext(), "API 실행 중 시스템에서 발생한 에러입니다.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}