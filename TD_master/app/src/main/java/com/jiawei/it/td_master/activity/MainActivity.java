package com.jiawei.it.td_master.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jiawei.it.td_master.R;
import com.jiawei.it.td_master.bean.User;
import com.jiawei.it.td_master.utils.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String mNameVerified = "用户名或密码错误";

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_regist)
    Button btnRegist;

    protected static final int ERROR = 1;
    protected static final int SUCCESS = 2;
    protected static final int FAILURE = 0;
    BufferedReader bufferReader;

    String uName = null;
    String uPass = null;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case SUCCESS:
                    try {
                        Gson gson = new Gson();
                        final User user = gson.fromJson((String)msg.obj, User.class);
                        Intent intent = new Intent(MainActivity.this,UserMsgActivity.class);
                        intent.putExtra("用户名",user.getName());
                        intent.putExtra("密码",user.getPswd());
                        intent.putExtra("IP地址",user.getIp());
                        intent.putExtra("性别",user.getSex());
                        startActivity(intent);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    showToast("发送失败");
                    break;
                case ERROR:
                    showToast((String)msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        String pswd = intent.getStringExtra("pswd");
        etName.setText(name);
        etPass.setText(pswd);
    }

    @OnClick({R.id.btn_login, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                userLogin();
                break;
            case R.id.btn_regist:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userLogin() {

        uName = etName.getText().toString().trim();

        uPass = etPass.getText().toString().trim();
        if(TextUtils.isEmpty(uName)){
            showToast("用户名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(uPass)){
            showToast("密码不能为空！");
            return;
        }

        getAsynHttp();
        //new LoginThread().start();
    }

    private void getAsynHttp() {
        Map<String,Object> mItem = new HashMap<>();
        List<Map<String,Object>> listItems = new ArrayList<>();
        //提交后台地址   http://wwww.baidu.com
        final String path = "http://192.168.11.156:8080/LoginModel/LoginServlet?username="+uName+"&password="+uPass;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("size", "10")
                .build();
        final Request request = new Request.Builder()
                .url(path)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG,result);
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i(TAG, "cache---" + str);
                } else {
                    String str = response.networkResponse().toString();
                    Log.i(TAG, "network---" + str);
                }
                if(result.equals(mNameVerified)){
                    Message msg = Message.obtain();
                    msg.what = ERROR;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }else {
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            }
        });
    }
    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
