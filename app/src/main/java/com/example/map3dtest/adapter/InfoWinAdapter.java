package com.example.map3dtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import com.example.map3dtest.Utils.GlobalStateManager;
import com.example.map3dtest.Utils.Position2ID;
import com.example.map3dtest.base.BaseApplication;
import com.example.map3dtest.transformdata.TransformData;
import com.example.nbapp.R;


/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */

/**
 * Caused by: android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity context
 * requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
 Context中有一个startActivity方法，Activity继承自Context，重载了startActivity方法。如果使用Activity的startActivity
 方法，不会有任何限制，而如果使用Context的startActivity方法的话，就需要开启一个新的task，遇到上面那个异常的，都是因为
 使用了Context的startActivity方法。解决办法是，加一个flag。
 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext = BaseApplication.getContext();
    private LatLng latLng;

    private String agentName;
    String[] devArgs = {};

    @Override
    public View getInfoWindow(Marker marker) {
        View view = null;
        initData(marker);
        if(GlobalStateManager.projectdevice == true){//工程类型
          view  = initView_project();
        }else if(GlobalStateManager.projectdevice == false){//设备类型
            view = initView_device();
        }else{
            view = initView_device();
        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        agentName = marker.getTitle();

    }

    @NonNull
    private View initView_project(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_project_window, null);
        Button mpj_wd_btn0 = (Button) view.findViewById(R.id.project_window_button0);
        Button mpj_wd_btn1 = (Button) view.findViewById(R.id.project_window_button1);
        TextView mpj_wd_txt = (TextView) view.findViewById(R.id.project_window_text);
        mpj_wd_txt.setText(agentName);
        mpj_wd_btn0.setOnClickListener(this);
        mpj_wd_btn1.setOnClickListener(this);
        return view;
    }

    @NonNull
    private View initView_device() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_device_window, null);
        Button mdv_wd_btn0 = (Button) view.findViewById(R.id.device_window_button_0);
        Button mdv_wd_btn1 = (Button) view.findViewById(R.id.device_window_button_1);
        Button mdv_wd_btn2 = (Button) view.findViewById(R.id.device_window_button_2);
        TextView mdv_wd_txt = (TextView) view.findViewById(R.id.device_window_text);

        devArgs = agentName.trim().split("#");
        mdv_wd_txt.setText(devArgs[2]);
        mdv_wd_btn0.setOnClickListener(this);
        mdv_wd_btn1.setOnClickListener(this);
        mdv_wd_btn2.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.device_window_button_0){//详情
            Intent intent = new Intent("com.example.nbiot_5.ACTION_START");
            intent.addCategory("com.example.nbiot_5.DEVICE_DETAILS_CATEGORY");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            TransformData transformDataTrd = new TransformData(devArgs[0], devArgs[2], devArgs[1]);
            intent.putExtra("deviceDetail", transformDataTrd);
            mContext.startActivity(intent);
        }
        else if(id == R.id.device_window_button_1){//数据
            Intent intent = new Intent("com.example.nbiot_5.ACTION_START");
            Bundle data = new Bundle();
            TransformData trans = new TransformData(devArgs[0], devArgs[2], devArgs[1]);
            data.putParcelable("deviceData", trans);
            intent.putExtras(data);
            intent.addCategory("com.example.nbiot_5.DEVICE_STATISTIC_CATEGORY");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        else if(id == R.id.device_window_button_2){//跳转到轨迹
            Intent intent = new Intent("com.example.nbiot_5.ACTION_START");
            Bundle data = new Bundle();
            TransformData trans = new TransformData(devArgs[0], devArgs[2], devArgs[1]);
            data.putParcelable("deviceTrack", trans);
            intent.putExtras(data);
            intent.addCategory("com.example.nbiot_5.DEVICE_TRACK_CATEGORY");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        else if(id == R.id.project_window_button0){//跳转到项目介绍页面需要修改
            Intent intentSec = new Intent("com.example.nbiot_5.ACTION_START");
            Bundle dataSec = new Bundle();
            TransformData transSec = new TransformData(agentName);
            dataSec.putParcelable("prjName", transSec);
            intentSec.putExtras(dataSec);
            intentSec.addCategory("com.example.nbiot_5.PROJECT_DETAIL_CATEGORY");
            intentSec.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentSec);
        } else if(id == R.id.project_window_button1){
            Intent intentSec = new Intent("com.example.nbiot_5.ACTION_START");
            Bundle dataSec = new Bundle();
            TransformData transTrd = new TransformData(agentName,false);
            dataSec.putParcelable("prjName", transTrd);
            intentSec.putExtras(dataSec);//跳转到项目的设备列表页面
            intentSec.addCategory("com.example.nbiot_5.PROJECT_CATEGORY");
            intentSec.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentSec);
        }
    }

}
