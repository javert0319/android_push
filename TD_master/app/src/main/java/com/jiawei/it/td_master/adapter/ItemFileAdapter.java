package com.jiawei.it.td_master.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiawei.it.td_master.R;
import com.jiawei.it.td_master.bean.ItemFile;
import com.jiawei.it.td_master.utils.ButtonInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JAIWEI
 * @company Thredim
 * @date on 2018/5/31.
 * @org www.thredim.com (宁波视睿迪光电有限公司)
 * @email thredim@thredim.com
 * @describe ItemFileAdapter
 */
public class ItemFileAdapter extends RecyclerView.Adapter<ItemFileAdapter.ImageItemFileViewHolder> {


    private List<ItemFile> articleList;

    //context
    private Context context;

    private LayoutInflater mLayoutInflater;

    private ButtonInterface buttonInterface;

    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        void onclick( int id,View view,int position);
    }

    public ItemFileAdapter(Context context, List<ItemFile> articleList) {
        this.context = context;
        this.articleList = articleList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ImageItemFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(
                R.layout.activity_file_item, parent, false);
        return new ImageItemFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageItemFileViewHolder holder, final int position) {
        ItemFile file = articleList.get(position);
        holder.tvFileName.setText(file.getFileName());
        holder.tvDownPercentage.setText(file.getPercentage());
        holder.btnDownFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
                    buttonInterface.onclick(R.id.btn_down_file,v,position);
                }
            }
        });
        holder.btnDownCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
                    buttonInterface.onclick(R.id.btn_down_cancel,v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ImageItemFileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_file_name)
        TextView tvFileName;
        @BindView(R.id.btn_down_file)
        Button btnDownFile;
        @BindView(R.id.btn_down_cancel)
        Button btnDownCancel;
        @BindView(R.id.pb_down)
        ProgressBar pbDown;
        @BindView(R.id.tv_down_percentage)
        TextView tvDownPercentage;

        public ImageItemFileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
