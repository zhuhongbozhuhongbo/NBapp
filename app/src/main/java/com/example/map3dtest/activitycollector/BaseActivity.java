package com.example.map3dtest.activitycollector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2017/10/27.
 * <p>
 * 基类
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());

        //将当前Activity添加到Activity管理器中
        ActivityCollector.getActivityCollector().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //把一个将要销毁的Activity从Activity管理器中移除
        ActivityCollector.getActivityCollector().finishActivity(this);
    }

}
