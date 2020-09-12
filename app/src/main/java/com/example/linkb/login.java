package com.example.linkb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {

    EditText editEmail,editPwd;
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
        final RadioButton rp = (RadioButton) findViewById(R.id.radioPrivate);
        final RadioButton rc = (RadioButton) findViewById(R.id.radiCcorporate);
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final ImageButton btnNaverLogin = (ImageButton) findViewById(R.id.btn_naver_login);
        final ImageButton btnKakaoLogin = (ImageButton) findViewById(R.id.btn_kakao_login);
        editEmail = (EditText) findViewById(R.id.edit_login_email);
        editPwd = (EditText) findViewById(R.id.edit_login_pwd);
        editEmail.setPadding(50, 0, 0, 0);
        editPwd.setPadding(50,0, 0, 0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idEdit = editEmail.getText().toString();
                String pwdEdit = editPwd.getText().toString();
                new RestAPITaskLogin("http://101.101.161.189/api/index.php/linkb_member/login_member", idEdit, pwdEdit).execute();

            }
        });

        btnNaverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"네이버 로그인 모듈 추가 예정",Toast.LENGTH_SHORT).show();
            }
        });

        btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"카카오 로그인 모듈 추가 예정",Toast.LENGTH_SHORT).show();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int privCorp = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(privCorp);
                if (rb.getText().toString() == rp.getText().toString()) {
                    Intent intent = new Intent(getApplicationContext(), privateSignUp.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), coprateSignup.class);
                    startActivity(intent);
                }
            }
        });
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
                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // Default
        backPressHandler.onBackPressed();
    }

}