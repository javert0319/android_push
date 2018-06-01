package com.jiawei.it.td_master.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jiawei.it.td_master.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMsgActivity extends AppCompatActivity {

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_pass)
    TextView tvUserPass;
    @BindView(R.id.tv_user_ip)
    TextView tvUserIp;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);
        ButterKnife.bind(this);
        userData();
    }

    private void userData() {
        Intent intent = this.getIntent();
        final String uName = intent.getStringExtra("用户名");
        final String uIP = intent.getStringExtra("IP地址");
        final String uPass = intent.getStringExtra("密码");
        final String uSex = intent.getStringExtra("性别");
        tvUserName.setText(uName);
        tvUserIp.setText(uIP);
        tvUserSex.setText(uSex);
        tvUserPass.setText(uPass);
    }

}
