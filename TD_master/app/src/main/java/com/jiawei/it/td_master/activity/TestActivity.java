package com.jiawei.it.td_master.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiawei.it.td_master.R;
import com.jiawei.it.td_master.utils.DownloadUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.list)
    ListView list;

    ArrayAdapter<String> myAdapterInstance;
    int layoutID = android.R.layout.simple_list_item_1;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    @BindView(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        list.setOnItemClickListener(this);

        registerMessageReceiver();  // used for receive msg

    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
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
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    //showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    //                    if (!ExampleUtil.isEmpty(extras)) {
                    //                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    //                    }
                    showMsg.append(messge);
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
        ArrayList<String> myStringArray = new ArrayList<String>();
        String[] split = msg.split(",");
        for (int i = 0;i<split.length;i++){
            myStringArray.add(split[i]);
            Log.i("JIGUANG-Example",split[i]);
        }
        myAdapterInstance = new ArrayAdapter<String>(this, layoutID, myStringArray);
        list.setAdapter(myAdapterInstance);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        DownloadUtil.getInstance().download(String.valueOf(parent.getItemAtPosition(position)), "/sdcard/Movies", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                //btn_download.setClickable(true);
                //dialog.hide();
                //ToastMgr.showLong(NetVideoPlayerActivity.this,"视频已保存："+path);
                tvShow.setText("");
            }

            @Override
            public void onDownloading(int progress) {
                //dialog.setMessage("已下载" + progress + "%");
                tvShow.setText("已下载" + progress + "%");
            }

            @Override
            public void onDownloadFailed() {
                //dialog.hide();
                //btn_download.setClickable(true);
            }
        });
    }
}
