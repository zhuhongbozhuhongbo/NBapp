package com.example.map3dtest.charts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;


import com.example.nbapp.R;
import com.example.map3dtest.recyclerviewadapter.DayAdapter;
import com.example.map3dtest.recyclerviewadapter.WeekAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestStudy extends AppCompatActivity {
    private RecyclerView recyclerDayView;
    private List<String> mDayDatas0;
    private List<String> mDayDatas1;
    private DayAdapter recycleDayAdapter;
    private LinearLayoutManager dayLayoutManager;

    private RecyclerView recyclerWeekView;
    private List<String> mWeekDatas0;
    private List<String> mWeekDatas1;
    private List<String> mWeekDatas2;
    private List<String> mWeekDatas3;
    private WeekAdapter recycleWeekAdapter;
    private LinearLayoutManager weekLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_chart);

        recyclerDayView = (RecyclerView)findViewById(R.id.today_recycler);
        initDayData();
        recycleDayAdapter = new DayAdapter(TestStudy.this, mDayDatas0, mDayDatas1);
        dayLayoutManager = new LinearLayoutManager(this);
        recyclerDayView.setLayoutManager(dayLayoutManager);
        dayLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerDayView.setAdapter(recycleDayAdapter);
        recyclerDayView.setItemAnimator( new DefaultItemAnimator());

        recyclerWeekView = (RecyclerView)findViewById(R.id.week_recycler);
        initWeekData();
        recycleWeekAdapter = new WeekAdapter(TestStudy.this, mWeekDatas0, mWeekDatas1, mWeekDatas2, mWeekDatas3);
        weekLayoutManager = new LinearLayoutManager(this);
        recyclerWeekView.setLayoutManager(weekLayoutManager);
        weekLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerWeekView.setAdapter(recycleWeekAdapter);
        recyclerWeekView.setItemAnimator( new DefaultItemAnimator());
    }

    private void initDayData(){
        mDayDatas0 = new ArrayList<String>();
        mDayDatas1 = new ArrayList<String>();


        mDayDatas0.add("今日开工时间");
        mDayDatas0.add("今日收工时间");
        mDayDatas0.add("实际工作时间");
        mDayDatas0.add("高效工作时间");
        mDayDatas0.add("中效工作时间");
        mDayDatas0.add("低效工作时间");
        mDayDatas0.add("利用率");

        mDayDatas1.add("07:30:00");
        mDayDatas1.add("18:00:00");
        mDayDatas1.add("08时32分");
        mDayDatas1.add("02时05分");
        mDayDatas1.add("03时15分");
        mDayDatas1.add("06时10分");
        mDayDatas1.add("50.60%");
    }

    private void initWeekData(){
        mWeekDatas0 = new ArrayList<String>();
        mWeekDatas1 = new ArrayList<String>();
        mWeekDatas2 = new ArrayList<String>();
        mWeekDatas3 = new ArrayList<String>();

        mWeekDatas0.add("本周一");
        mWeekDatas0.add("本周二");
        mWeekDatas0.add("本周三");
        mWeekDatas0.add("本周四");
        mWeekDatas0.add("本周五");
        mWeekDatas0.add("本周六");
        mWeekDatas0.add("本周日");

        for(int i = 0; i < 7; i++){
            mWeekDatas1.add("item" + i);
            mWeekDatas2.add("item" + i);
            mWeekDatas3.add("item" + i);
        }
    }



}
