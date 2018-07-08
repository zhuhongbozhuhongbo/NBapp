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

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyViewHolder>{
    private List<String> mDatas0;
    private List<String> mDatas1;

    private Context mContext;
    private LayoutInflater inflater;

    public DayAdapter(Context context, List<String> datas0, List<String> datas1){
        this.mContext = context;
        this.mDatas0 = datas0;
        this.mDatas1 = datas1;

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

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.item_day, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv0;
        TextView tv1;

        public MyViewHolder(View view){
            super(view);
            tv0 = (TextView)view.findViewById(R.id.item_day_0);
            tv1 = (TextView)view.findViewById(R.id.item_day_1);
        }
    }

}
