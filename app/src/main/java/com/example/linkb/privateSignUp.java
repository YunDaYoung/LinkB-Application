package com.example.linkb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.widget.IndicatorSeekBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class privateSignUp extends AppCompatActivity {

    static int checkCode = 0;
    EditText editEmail;
    LinearLayout page1, page2, page3;
    Button btnSignUp;
    IndicatorSeekBar mSeekBar;
    TextView seekbar_first, seekbar_second, seekbar_third;
    LinearLayout signup_frag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_sign_up);


        editEmail = (EditText) findViewById(R.id.edit_sigup_email);
        seekbar_first = findViewById(R.id.seekbar_first);
        seekbar_second = findViewById(R.id.seekbar_second);
        seekbar_third = findViewById(R.id.seekbar_third);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        page1 = (LinearLayout) findViewById(R.id.page1);
        page2 = (LinearLayout) findViewById(R.id.page2);
        page3 = (LinearLayout) findViewById(R.id.page3);
        mSeekBar = findViewById(R.id.seekbar);
        signup_frag=findViewById(R.id.signup_frag);
        final Button btnCheck = (Button) findViewById(R.id.btn_dup_check);
        final EditText editPwd = (EditText) findViewById(R.id.edit_sigup_pwd);
        final EditText editChPwd = (EditText) findViewById(R.id.edit_sigup_chpwd);
        final EditText editName = (EditText) findViewById(R.id.edit_sigup_name);
        final EditText editPhone = (EditText) findViewById(R.id.edit_sigup_phone);
        final EditText editWorkPlace = (EditText) findViewById(R.id.edit_sigup_work_place);
        final EditText editWorkCompany = (EditText) findViewById(R.id.edit_sigup_work_company);
        final EditText editWorkTeam = (EditText) findViewById(R.id.edit_sigup_work_team);
        final EditText editWorkPosition = (EditText) findViewById(R.id.edit_sigup_work_position);
        final ImageView setImage = findViewById(R.id.pwdChk);

        seekbar_second.setVisibility(View.INVISIBLE);
        seekbar_third.setVisibility(View.INVISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        signup_frag.setLayoutParams(new LinearLayout.LayoutParams
                (displayMetrics.widthPixels,(displayMetrics.heightPixels*80)/100));

        editEmail.setPadding(40, 0, 0, 50);
        editPwd.setPadding(40, 0, 0, 50);
        editChPwd.setPadding(40, 0, 0, 50);
        editName.setPadding(40, 0, 0, 50);
        editPhone.setPadding(40, 0, 0, 50);
        editWorkPlace.setPadding(40, 0, 0, 50);
        editWorkCompany.setPadding(40, 0, 0, 50);
        editWorkTeam.setPadding(40, 0, 0, 50);
        editWorkPosition.setPadding(40, 0, 0, 50);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //아이디 중복 체크를 실행했는지를 체크하는 코드
        editChPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPwd.getText().toString().equals(editChPwd.getText().toString())) {
                    setImage.setImageResource(R.drawable.ic_baseline_check_24);
                } else {
                    setImage.setImageResource(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
                                             @Override
                                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                 checkCode=0;
                                             }

                                             @Override
                                             public void onTextChanged(CharSequence s, int start, int before, int count) {
                                             }

                                             @Override
                                             public void afterTextChanged(Editable s) {

                                             }
                                         });

                btnCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailTemp = editEmail.getText().toString();
                        String emailAllow = "^[a-zA-Z0-9_-]+@[a-zA-z0-9.-]+\\.[a-zA-z]+$";
                        if (!emailTemp.matches(emailAllow)) {
                            Toast.makeText(getApplicationContext(), "허용가능한 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            new RestAPITaskCheckDuplicate("http://101.101.161.189/api/index.php/linkb_member/check_email_duplicate", emailTemp).execute();
                        }
                    }
                });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page1.getVisibility() == View.VISIBLE) {
                    if (checkCode == 0) {
                        Toast.makeText(getApplicationContext(), "중복확인을 하세요", Toast.LENGTH_SHORT).show();
                    } else {

                        if (!editPwd.getText().toString().equals(editChPwd.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                        } else if (editEmail.getText().toString().equals("") || editPwd.getText().toString().equals("") || editChPwd.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            page1.setVisibility(View.GONE);
                            page2.setVisibility(View.VISIBLE);
                            page3.setVisibility(View.GONE);
                            nextPhase();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    seekbar_first.setVisibility(View.INVISIBLE);
                                    seekbar_second.setVisibility(View.VISIBLE);
                                    seekbar_third.setVisibility(View.INVISIBLE);
                                }
                            }, 300);

                        }
                    }
                } else if (page2.getVisibility() == View.VISIBLE) {
                    if (editName.getText().toString().equals("") || editPhone.getText().toString().equals("") || editWorkPlace.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        page1.setVisibility(View.GONE);
                        page2.setVisibility(View.GONE);
                        page3.setVisibility(View.VISIBLE);
                        btnSignUp.setText("    " + "가입완료!" + "    ");
                        btnSignUp.setBackgroundResource(R.drawable.dup_check_button);
                        nextPhase();
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                seekbar_first.setVisibility(View.INVISIBLE);
                                seekbar_second.setVisibility(View.INVISIBLE);
                                seekbar_third.setVisibility(View.VISIBLE);
                            }
                        }, 300);
                    }
                } else {
                    //회원가입 코드 !
                    if (editWorkCompany.getText().toString().equals("") || editWorkPosition.getText().toString().equals("") || editWorkTeam.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        String emailTemp = editEmail.getText().toString();
                        String pwdTemp = editPwd.getText().toString();
                        String chPwdTemp = editChPwd.getText().toString();
                        String nameTemp = editName.getText().toString();
                        String phoneNumberTemp = editPhone.getText().toString();
                        String workPlaceTemp = editWorkPlace.getText().toString();
                        String workCompanyTemp = editWorkCompany.getText().toString();
                        String workTeamTemp = editWorkTeam.getText().toString();
                        String workPositionTemp = editWorkPosition.getText().toString();
                        new RestAPITaskSignUp("http://101.101.161.189/api/index.php/linkb_member/insert_member",
                                emailTemp, pwdTemp, nameTemp, phoneNumberTemp, workCompanyTemp, workPlaceTemp, workTeamTemp, workPositionTemp).execute();
                    }
                }


            }
        });


    }

    public class RestAPITaskCheckDuplicate extends AsyncTask<Integer, Void, String> {

        protected String mURL;
        protected String id;

        public RestAPITaskCheckDuplicate(String url, String id) {
            this.mURL = url;
            this.id = id;
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
                buffer.append("email").append("=").append(id);

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
                String result = builder.toString().trim();
                System.out.println("test0");
                System.out.println(result);


                System.out.println("test");
                JSONObject jsonObject = new JSONObject(result);

                JSONObject postObject = jsonObject.getJSONObject("code");
                System.out.println(postObject.toString());
                System.out.println();
                String code = postObject.getString("code");


                reader.close();
                return code;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String code) {

            if (code.equals("205")) {
                Toast.makeText(getApplicationContext(), "중복체크 실패", Toast.LENGTH_SHORT).show();
            } else if (code.equals("200")) {
                Toast.makeText(getApplicationContext(), "중복체크 성공", Toast.LENGTH_SHORT).show();
                checkCode = 1;
            } else if (code.equals("206")) {
                Toast.makeText(getApplicationContext(), "API Key가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            } else if (code.equals("500")) {
                Toast.makeText(getApplicationContext(), "API 실행 중 시스템에서 발생한 에러입니다.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public class RestAPITaskSignUp extends AsyncTask<Integer, Void, String> {

        protected String mURL, email, password, name, phone, work_company, work_place, work_team, work_position;

        public RestAPITaskSignUp(String url, String email, String password, String name, String phone, String work_company,
                                 String work_place, String work_team, String work_position) {
            this.mURL = url;
            this.email = email;
            this.password = password;
            this.name = name;
            this.phone = phone;
            this.work_company = work_company;
            this.work_place = work_place;
            this.work_team = work_team;
            this.work_position = work_position;
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
                buffer.append("email").append("=").append(email).append("&");
                buffer.append("password").append("=").append(password).append("&");
                buffer.append("name").append("=").append(name).append("&");
                buffer.append("phone").append("=").append(phone).append("&");
                buffer.append("work_company").append("=").append(work_company).append("&");
                buffer.append("work_place").append("=").append(work_place).append("&");
                buffer.append("work_team").append("=").append(work_team).append("&");
                buffer.append("work_position").append("=").append(work_position);

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
                String result = builder.toString().trim();
                System.out.println("test0");
                System.out.println(result);

                System.out.println("test");
                JSONObject jsonObject = new JSONObject(result);

                JSONObject postObject = jsonObject.getJSONObject("code");
                System.out.println(postObject.toString());
                System.out.println();
                String code = postObject.getString("code");
                reader.close();
                return code;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String code) {

            if (code.equals("200")) {
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            } else if (code.equals("205")) {
                Toast.makeText(getApplicationContext(), "이메일 중복으로 회원가입 실패", Toast.LENGTH_SHORT).show();
            } else if (code.equals("206")) {
                Toast.makeText(getApplicationContext(), "API Key가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            } else if (code.equals("500")) {
                Toast.makeText(getApplicationContext(), "API 실행 중 시스템에서 발생한 에러입니다.", Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                if (page3.getVisibility() == View.VISIBLE) {
                    page1.setVisibility(View.GONE);
                    page2.setVisibility(View.VISIBLE);
                    page3.setVisibility(View.GONE);
                    btnSignUp.setText("다음단계로");
                    btnSignUp.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    prePhase();
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            seekbar_first.setVisibility(View.INVISIBLE);
                            seekbar_second.setVisibility(View.VISIBLE);
                            seekbar_third.setVisibility(View.INVISIBLE);
                        }
                    }, 300);
                } else if (page2.getVisibility() == View.VISIBLE) {
                    page1.setVisibility(View.VISIBLE);
                    page2.setVisibility(View.GONE);
                    page3.setVisibility(View.GONE);
                    prePhase();
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            seekbar_first.setVisibility(View.VISIBLE);
                            seekbar_second.setVisibility(View.INVISIBLE);
                            seekbar_third.setVisibility(View.INVISIBLE);
                        }
                    }, 300);
                } else
                    finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void nextPhase() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mSeekBar, "progress", mSeekBar.getProgress(), mSeekBar.getProgress() + 100);
        anim.setDuration(500);
        anim.start();
    }

    public void prePhase() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mSeekBar, "progress", mSeekBar.getProgress(), mSeekBar.getProgress() - 100);
        anim.setDuration(500);
        anim.start();
    }


}