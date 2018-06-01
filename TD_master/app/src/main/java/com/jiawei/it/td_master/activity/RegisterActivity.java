package com.jiawei.it.td_master.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jiawei.it.td_master.R;
import com.jiawei.it.td_master.utils.StreamTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.et_register_ip)
    EditText etRegisterIp;
    @BindView(R.id.et_register_pass)
    EditText etRegisterPass;
    @BindView(R.id.et_register_repass)
    EditText etRegisterRepass;
    @BindView(R.id.rg_register_man)
    RadioButton rgRegisterMan;
    @BindView(R.id.rg_register_woman)
    RadioButton rgRegisterWoman;
    @BindView(R.id.rg_register_sex)
    RadioGroup rgRegisterSex;
    @BindView(R.id.btn_register_regist)
    Button btnRegisterRegist;
    @BindView(R.id.btn_register_login)
    Button btnRegisterLogin;

    protected static final int ERROR = 1;
    protected static final int SUCCESS = 2;

    private String temp = "";
    private String image = "images/boy.png";
    private String email = "chia021@163.com";
    private String tem;
    private String uPass;
    private String uIP;
    private  String uName;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    showToast((String) msg.obj);
                    if (msg.obj.equals("注册成功")) {
                        //System.out.println("1111"+msg.obj);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.putExtra("name",etRegisterName.getText().toString().trim());
                        intent.putExtra("pswd",etRegisterPass.getText().toString().trim());
                        startActivity(intent);
                    }
                    break;
                case ERROR:
                    showToast("登录失败！");
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        rgRegisterSex.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.rg_register_man, R.id.rg_register_woman, R.id.rg_register_sex,
            R.id.btn_register_regist, R.id.btn_register_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register_regist:
                register();
                break;
            case R.id.btn_register_login:
                Intent loginActivity = new Intent(this, MainActivity.class);
                startActivity(loginActivity);
                break;
        }
    }

    private void register() {
        uName = etRegisterName.getText().toString().trim();
        uIP = etRegisterIp.getText().toString().trim();
        uPass = etRegisterPass.getText().toString().trim();
        final String uRePass = etRegisterRepass.getText().toString().trim();
        try {

            tem = URLEncoder.encode(URLEncoder.encode(temp,"UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(uName)){
            showToast("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(uIP)){
            showToast("IP地址不能为空");
            return;
        }
        if(TextUtils.isEmpty(uPass)){
            showToast("密码不能为空");
            return;
        }
        if(TextUtils.equals(uPass,uRePass) == false){
            showToast("两次输入密码不同");
            return;
        }
        if(uPass.length()<6){
            showToast("密码位数小于6，安全系数过低");
            return;
        }
        if(temp == ""){
            showToast("请选择性别");
            return;
        }
        new Thread(new SubRegister()).start();
    }

    private class SubRegister implements Runnable{
        @Override
        public void run() {

            try {
                String name = URLEncoder.encode(URLEncoder.encode(uName,"UTF-8"),"UTF-8");
                final String path="http://192.168.11.156:8080/LoginModel/RegServlet?username="+name+"&tel="+uIP+"&password="+uPass+"&sex="+tem+"&photo="+image+"&email="+email; //将注册内容拼接到url
                URL url= null;
                url = new URL(path);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; KB974487)");
                int code=conn.getResponseCode();
                if(code == 200){
                    InputStream is=conn.getInputStream();
                    String result= StreamTools.readInputStream(is);
                    Message msg= Message.obtain();
                    msg.what=SUCCESS;
                    msg.obj=result;
                    handler.sendMessage(msg);
                }else {
                    Message msg=Message.obtain();
                    msg.what=ERROR;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg=Message.obtain();
                msg.what=ERROR;
                handler.sendMessage(msg);
            }

        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rg_register_man:
                temp = "man";
                break;
            case R.id.rg_register_woman:
                temp = "woman";
                break;
            default:
                break;
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
