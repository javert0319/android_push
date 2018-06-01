package com.jiawei.it.td_master.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.jiawei.it.td_master.R;
import com.jiawei.it.td_master.adapter.ItemFileAdapter;
import com.jiawei.it.td_master.bean.ItemFile;
import com.jiawei.it.td_master.utils.DownloadListner;
import com.jiawei.it.td_master.utils.DownloadManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDownActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 001;
    @BindView(R.id.rcv_article)
    RecyclerView rcvArticle;
    @BindView(R.id.btn_down_al)
    Button btnDownAl;
    @BindView(R.id.btn_cancel_all)
    Button btnCancelAll;

    private List<ItemFile> itemFileList;

    private ItemFile fileName;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.jiawei.it.td_master";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    DownloadManager mDownloadManager;

    private Map<Integer, ProgressBar> map;
    private Map<Integer, Integer> mProgress;
    private List<Integer> positionList;
    ItemFileAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_down);
        ButterKnife.bind(this);
        itemFileList = new ArrayList<ItemFile>();
        map = new HashMap<>();
        mProgress = new HashMap<> ();
        positionList = new ArrayList<> ();
        rcvArticle.setLayoutManager(new LinearLayoutManager(this));
        registerMessageReceiver();  // used for receive msg
    }

    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(messge);
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }
    private String[] split;

    private void setCostomMsg(String msg) {
        split = msg.split(",");
        for (int i = 0;i<split.length;i++){
            fileName = new ItemFile( split[i].substring(split[i].lastIndexOf("/")+1),"0%");
            itemFileList.add(fileName);
            Log.i("JIGUANG-Example","FileDownActivity--"+split[i]);
        }

        adapter = new ItemFileAdapter(FileDownActivity.this, itemFileList);
        rcvArticle.setAdapter(adapter);
        adapter.buttonSetOnclick(new ItemFileAdapter.ButtonInterface() {
            @Override
            public void onclick(int id, View view, int position) {
                switch (id){
                    case R.id.btn_down_file:
                        initDownloads(split[position]);
                        if (!mDownloadManager.isDownloading(split[position])) {
                            mDownloadManager.download(split[position]);
                            //btn_download1.setText("暂停");
                        showMessage("开始下载");
                        } else {
                            //btn_download1.setText("下载");
                            mDownloadManager.pause(split[position]);
                        }

                        break;
                    case R.id.btn_down_cancel:
                        mDownloadManager.cancel(split[position]);
                        break;
                }
            }
        });
    }

    private float pro;

    private void initDownloads(String fileUrl) {
        mDownloadManager = DownloadManager.getInstance();
        mDownloadManager.add(fileUrl, new DownloadListner() {
            @Override
            public void onFinished() {
                Toast.makeText(FileDownActivity.this, "下载完成!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(float progress) {
                //pb_progress1.setProgress((int) (progress * 100));
                //tv_progress1.setText(String.format("%.2f", progress * 100) + "%");
                pro = progress;
            }

            @Override
            public void onPause() {
                Toast.makeText(FileDownActivity.this, "暂停了!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                //tv_progress1.setText("0%");
                //pb_progress1.setProgress(0);
                //btn_download1.setText("下载");
                Toast.makeText(FileDownActivity.this, "下载已取消!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.btn_down_al, R.id.btn_cancel_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_down_al:
                if (!mDownloadManager.isDownloading(split)) {
                    btnDownAl.setText("全部暂停");
                    mDownloadManager.download(split);//最好传入个String[]数组进去
                } else {
                    mDownloadManager.pause(split);
                    btnDownAl.setText("全部下载");
                }
                break;
            case R.id.btn_cancel_all:
                mDownloadManager.cancel(split);
                btnCancelAll.setText("全部下载");
                break;
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        split = getSharedPreference("thredim");
        if(split != null){
            for (int i = 0;i<split.length;i++){
                fileName = new ItemFile( split[i].substring(split[i].lastIndexOf("/")+1),"0%");
                itemFileList.add(fileName);
                Log.i("JIGUANG-Example","FileDownActivity--"+split[i]);
            }

            adapter = new ItemFileAdapter(FileDownActivity.this, itemFileList);
            rcvArticle.setAdapter(adapter);
            adapter.buttonSetOnclick(new ItemFileAdapter.ButtonInterface() {
                @Override
                public void onclick(int id, View view, int position) {
                    switch (id){
                        case R.id.btn_down_file:
                            initDownloads(split[position]);
                            if (!mDownloadManager.isDownloading(split[position])) {
                                mDownloadManager.download(split[position]);
                                reInit(position);
                                //btn_download1.setText("暂停");
                                showMessage("开始下载");
                            } else {
                                //btn_download1.setText("下载");
                                mDownloadManager.pause(split[position]);
                            }

                            break;
                        case R.id.btn_down_cancel:
                            mDownloadManager.cancel(split[position]);
                            break;
                    }
                }
            });
        }
    }

    private void reInit(int position) {
        adapter.notifyItemChanged(position, "jw");//局部刷新
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (!checkPermission(permission)) {//针对android6.0动态检测申请权限
            if (shouldShowRationale(permission)) {
                showMessage("需要权限跑demo哦...");
            }
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 显示提示消息
     *
     * @param msg
     */
    private void showMessage(String msg) {
        Toast.makeText(FileDownActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检测用户权限
     *
     * @param permission
     * @return
     */
    protected boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否需要显示请求权限的理由
     *
     * @param permission
     * @return
     */
    protected boolean shouldShowRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setSharedPreference("thredim",split);
    }

    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);

        return str;
    }

    public void setSharedPreference(String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }
}
