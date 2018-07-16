package com.example.map3dtest.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.MapView;
import com.example.nbapp.R;

import chihane.jdaddressselector.BottomDialog;
import mlxy.utils.Dev;

public class DeviceMaintainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView Ighome_page_0;
    private ImageView Ighome_page_1;
    private ImageView Ighome_page_2;
    private ImageView Ighome_page_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_maintain);
        initView();
    }

    private void initView() {

        Ighome_page_0 = (ImageView)findViewById(R.id.device_home_page_0);
        Ighome_page_1 = (ImageView)findViewById(R.id.device_home_page_1);
        Ighome_page_2 = (ImageView)findViewById(R.id.device_home_page_2);
        Ighome_page_3 = (ImageView)findViewById(R.id.device_home_page_3);

        Ighome_page_0.setOnClickListener(this);
        Ighome_page_1.setOnClickListener(this);
        Ighome_page_2.setOnClickListener(this);
        Ighome_page_3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.device_home_page_0:
                intent.setClass(DeviceMaintainActivity.this, MapProjectActivity.class);
                startActivity(intent);
                break;

            case R.id.device_home_page_1:
                intent.setClass(DeviceMaintainActivity.this, MapDeviceActivity.class);
                startActivity(intent);
                break;

            case R.id.device_home_page_2:
           /*   intent.setClass(DeviceMaintainActivity.this, DeviceMaintainActivity.class);
                startActivity(intent);
                break;
                */
                 break;
            case R.id.device_home_page_3:
                intent.setClass(DeviceMaintainActivity.this, AccountManageActivity.class);
                startActivity(intent);
                break;


        }
    }

    protected void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceMaintainActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DeviceMaintainActivity.this.finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Log.d("haha", "按下");
    }
}
