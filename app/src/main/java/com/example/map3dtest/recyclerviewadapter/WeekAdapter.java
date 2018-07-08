package com.example.map3dtest.recyclerviewadapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.nbapp.R;

import java.util.List;

/**
 * Created by 朱宏博 on 2018/3/29.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder>{
    private List<String> mDatas0;
    private List<String> mDatas1;
    private List<String> mDatas2;
    private List<String> mDatas3;
    private Context mContext;
    private LayoutInflater inflater;

    public WeekAdapter(Context context, List<String> datas0, List<String> datas1, List<String> datas2, List<String> datas3){
        this.mContext = context;
        this.mDatas0 = datas0;
        this.mDatas1 = datas1;
        this.mDatas2 = datas2;
        this.mDatas3 = datas3;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount(){
        return mDatas0.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        holder.tv0.setText(mDatas0.get(position));
        holder.tv1.setText(mDatas1.get(position));
        holder.tv2.setText(mDatas2.get(position));
        holder.tv3.setText(mDatas3.get(position));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.item_week, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv0;
        TextView tv1;
        TextView tv2;
        TextView tv3;

        public MyViewHolder(View view){
            super(view);
            tv0 = (TextView)view.findViewById(R.id.item_week_0);
            tv1 = (TextView)view.findViewById(R.id.item_week_1);
            tv2 = (TextView)view.findViewById(R.id.item_week_2);
            tv3 = (TextView)view.findViewById(R.id.item_week_3);
        }
    }

}
