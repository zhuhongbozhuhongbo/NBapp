package com.example.map3dtest.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.nbapp.R;
import com.example.map3dtest.recyclerviewinterface.MyOnItemClickListener;
import com.example.map3dtest.recyclerviewinterface.MyOnItemLongClickListener;

import java.util.List;

/**
 * Created by 朱宏博 on 2018/5/19.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder>{
    private List<String> mDatas0;
    private List<String> mDatas1;

    private Context mContext;
    private LayoutInflater inflater;

    private MyOnItemClickListener itemClickListener;
    private MyOnItemLongClickListener itemLongClickListener;

    public ProjectAdapter(Context context, List<String> datas0, List<String> datas1){
        this.mContext = context;
        this.mDatas0 = datas0;
        this.mDatas1 = datas1;

        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount(){
        return mDatas0.size();
    }

    /**
     * 列表点击事件
     * @param itemClickListener
     */
    public void setOnItemClickListener(MyOnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    /**
     * 列表长按事件
     * @param itemLongClickListener
     */
    public void setOnItemLongClickListener(MyOnItemLongClickListener itemLongClickListener){
        this.itemLongClickListener = itemLongClickListener;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position){
        holder.tv0.setText(mDatas0.get(position));
        holder.tv1.setText(mDatas1.get(position));

        if(itemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    itemClickListener.OnItemClickListener(holder.itemView, holder.getLayoutPosition());
                }
            });
        }

        if(itemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    itemLongClickListener.OnItemLongClickListener(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.item_project, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv0;
        TextView tv1;

        public MyViewHolder(View view){
            super(view);
            tv0 = (TextView)view.findViewById(R.id.item_project_0);
            tv1 = (TextView)view.findViewById(R.id.item_project_1);
        }
    }

}
