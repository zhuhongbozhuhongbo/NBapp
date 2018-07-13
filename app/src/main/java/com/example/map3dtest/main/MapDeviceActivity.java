package com.example.map3dtest.main;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import com.example.nbapp.R;
import com.example.map3dtest.Utils.ByteCompile;
import com.example.map3dtest.Utils.GlobalStateManager;
import com.example.map3dtest.adapter.InfoWinAdapter;
import com.example.map3dtest.Utils.Matching;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.search.SearchPageActivity;
import com.example.map3dtest.tables.LocationSelectActivity;


import java.util.ArrayList;
import java.util.Iterator;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import mlxy.utils.T;

public class MapDeviceActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMap.OnMarkerClickListener, View.OnClickListener, OnAddressSelectedListener{


    private MapView mapView = null;
    private AMap aMap;

    private InfoWinAdapter adapter;
    private ArrayList currentEntityList = new ArrayList<MarkerManager>();

    private boolean markerflag = false;//false为


    private LinearLayout jdSelect;
    private LinearLayout mselect_projectdevice;

    private TextView msearch_tv;
    private int counter = 0;
    private ImageView Ighome_page_0;
    private ImageView Ighome_page_1;
    private ImageView Ighome_page_2;
    private ImageView Ighome_page_3;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_map);

        mapView = (MapView)findViewById(R.id.home_page_mapview_0);
        mapView.onCreate(savedInstanceState);

        initView();
        initMap();

        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {

                //注:若为 #1#,解析出的 data.length = 2
                counter++;
                if(body.getType().equals("DDI") && body.getField().equals("GPSInformation")&&body.getQuerymode().equals("findLatest")) {
                    String data[] = body.getP0().trim().split("#");

                    if(data.length != 0){
                        Log.d("haha", "MapProjectActivity:" + " data0" + data[0] + "  data1" + data[1] + " counter" + counter);


                        //这段代码后期需要封装，拟定采用内部类
                        byte[] mLatLng = Base64.decode(data[1], Base64.DEFAULT);

                        LatLng resultLatLng = new LatLng(ByteCompile.byte2Int(mLatLng[0], mLatLng[1], mLatLng[2], mLatLng[3]
                        ) * 90.0 / Integer.MAX_VALUE, ByteCompile.byte2Int(mLatLng[4], mLatLng[5], mLatLng[6], mLatLng[7]
                        ) * 180.0 / Integer.MAX_VALUE);
                        Log.d("haha", "MapProjectActivity" + resultLatLng);
                        String title = "";
                        if (counter == 1) {
                            //4号
                            title = "豫C 83576";
                            Log.d("haha", "MapProjectActivity" + title);

                        } else if (counter == 2) {
                            //5

                            title = "皖K M1863";
                            Log.d("haha", "MapProjectActivity" + title);
                        } else if (counter == 3) {
                            //6

                            title = "鄂C 9C219";
                            Log.d("haha", "MapProjectActivity" + title);
                            counter = 0;
                        }

                        addDeviceMarkerToMap(resultLatLng, title);


                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(resultLatLng, 16, 30, 0));
                        aMap.moveCamera(mCameraUpdate);
                    }
                }


            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.jdselector:
                BottomDialog dialog = new BottomDialog(MapDeviceActivity.this);
                dialog.setOnAddressSelectedListener(MapDeviceActivity.this);
                dialog.show();
                break;

            case R.id.select_projectdevice:
                if(GlobalStateManager.projectdevice == true){//此时主页为“项目管理”
                    Intent intent = new Intent(MapDeviceActivity.this, LocationSelectActivity.class);
                    startActivity(intent);
                }else if(GlobalStateManager.projectdevice == false){//此时主页为“设备管理
                    Intent intent = new Intent(MapDeviceActivity.this, SearchPageActivity.class);
                    startActivity(intent);
                }
                break;


            case R.id.home_page_0:

            case R.id.home_page_1:

            case R.id.home_page_2:

            case R.id.home_page_3:



        }

    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s =
                (province == null ? "" : province.name);

/*        (province == null ? "" : province.name) +
                (city == null ? "" : "\n" + city.name) +
                (county == null ? "" : "\n" + county.name) +
                (street == null ? "" : "\n" + street.name);*/

        T.showShort(this, s);


        if(GlobalStateManager.projectdevice == true) {
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)  数小视野范围大
            LatLng updateLatLng = Matching.latLngConvert(s);
            GlobalStateManager.currentLatlng = updateLatLng;
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(updateLatLng, 8, 30, 0));
            aMap.moveCamera(mCameraUpdate);
            clearMarkers();
            addLandMarkerToMap(updateLatLng, s);
        }else if(GlobalStateManager.projectdevice == false){
            queryDevice(s);
          /*  LatLng updateLatLng = Matching.latLngConvert(s);
            GlobalStateManager.currentLatlng = updateLatLng;
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(updateLatLng, 8, 30, 0));
            aMap.moveCamera(mCameraUpdate);
            clearMarkers();
            addMarkerToMap(updateLatLng, s);*/
        }


    }

    public void queryDevice(String province){
        if(province.equals("江苏")){
            Log.d("haha", "开始查询");
            //查询项目部信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field><type>DDI</type>" +
                    "<querymode>findLatest</querymode><p0>4</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", 0);//4526
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field><type>DDI</type>" +
                    "<querymode>findLatest</querymode><p0>5</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", 0);

            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field><type>DDI</type>" +
                    "<querymode>findLatest</querymode><p0>6</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", 0);
        }else if(province.equals("山东")){
            Log.d("haha", "山东开始查询");
            //查询项目部信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>PDI</type>" +
                    "<querymode>findByProjectProvince</querymode><p0>empty</p0><p1>安徽</p1><p2>empty</p2><p3>null</p3><p4>null</p4><p5>null</p5></query>", 0);
        }else if(province.equals("内蒙古")){
            Log.d("haha", "开始查询");
            //查询项目部信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DSI</type>" +
                    "<querymode>findByProjectInformation</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>太湖隧道项目</p3><p4>empty</p4><p5>empty</p5></query>", 0);
        }else if(province.equals("黑龙江")){
            Log.d("haha", "heilong开始查询");
            //查询项目部信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DDI</type>" +
                    "<querymode>findLatest</querymode><p0>4</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", 0);
        }else if(province.equals("吉林")){
            Log.d("haha", "jilin开始查询");
            //查询项目部信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field><type>DDI</type>" +
                    "<querymode>findLatest</querymode><p0>4</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", 0);
        }else if(province.equals("辽宁")){
            Log.d("haha", "liaoning开始查询");
            //查询项目部信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DDI</type>" +
                    "<querymode>findByDate</querymode><p0>empty</p0><p1>2018-7-7</p1><p2>2018-7-7</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", 0);
        }
    }

    //maker的点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {

        /*if (!marker.getPosition().equals(myLatLng)){ //点击的marker不是自己位置的那个marker
            if (oldMarker != null) {
                oldMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_normal));
            }
            oldMarker = marker;
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected));
        }*/
        Log.d("haha", "点击Marker");
        marker.showInfoWindow();

        markerflag = true;
        return true; //返回 “false”，除定义的操作之外，默认操作也将会被执行
    }

    //地图的点击事件
    @Override
    public void onMapClick(LatLng latLng) {
   /*     if(markerflag == true){
            markerflag = false;
        }else if(markerflag == false) {
            //点击地图上没marker 的地方，隐藏inforwindow
            hideInfoWindow();
        }*/
        hideInfoWindow();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapDeviceActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MapDeviceActivity.this.finish();
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
    private void initView() {
        mapView = (MapView) findViewById(R.id.home_page_mapview_0);//地图
        msearch_tv = findViewById(R.id.search_tv);
        jdSelect = (LinearLayout)findViewById(R.id.jdselector);//唤醒popupWindow的按键
        jdSelect.setOnClickListener(this);
        mselect_projectdevice = (LinearLayout)findViewById(R.id.select_projectdevice);
        mselect_projectdevice.setOnClickListener(this);

        Ighome_page_0 = (ImageView)findViewById(R.id.home_page_0);
        Ighome_page_1 = (ImageView)findViewById(R.id.home_page_1);
        Ighome_page_2 = (ImageView)findViewById(R.id.home_page_2);
        Ighome_page_3 = (ImageView)findViewById(R.id.home_page_3);

        Ighome_page_0.setOnClickListener(this);
        Ighome_page_1.setOnClickListener(this);
        Ighome_page_2.setOnClickListener(this);
        Ighome_page_3.setOnClickListener(this);

        msearch_tv.setText("请选择项目地区");

    }

    private void initMap(){

        if(aMap == null){
            aMap = mapView.getMap();
        }

        adapter = new InfoWinAdapter();
        aMap.setInfoWindowAdapter(adapter);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);


    /*
    此段代码可用于初始化两个marker在地图上
    addMarkerToMap(new LatLng(30.666482, 104.036407), "太湖0");
        addMarkerToMap(new LatLng(31.335159, 111.617403), "太湖1");
*/

    }

    //单个title对应单个marker
    private void addDeviceMarkerToMap(LatLng latLng, String title) {
        Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dev4))
        );
        // marker.showInfoWindow();

        currentEntityList.add(new MarkerManager(latLng, title, marker));
    }

    //单个title对应单个marker
    private void addLandMarkerToMap(LatLng latLng, String title) {
        Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_normal))
        );
        // marker.showInfoWindow();

        currentEntityList.add(new MarkerManager(latLng, title, marker));
    }


    private void clearMarkers(){
        for(Iterator iterator = currentEntityList.iterator(); iterator.hasNext();){
            MarkerManager markerManager = (MarkerManager)iterator.next();
            markerManager.getMarker().remove();
        }
        currentEntityList.clear();
    }

    private void hideInfoWindow(){
        for(Iterator iterator = currentEntityList.iterator(); iterator.hasNext();){
            MarkerManager markerManager = (MarkerManager)iterator.next();
            Log.d("haha","title :" + markerManager.getMarker().getTitle());
            markerManager.getMarker().hideInfoWindow();
            Log.d("haha", "hideInfo");
        }
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //单个title对应多个Marker
    private void addMarkersToMap(ArrayList<LatLng> latLngList, String title) {
        for(int i = 0; i < latLngList.size(); i++) {
            Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(latLngList.get(i))
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_normal))
            );

            marker.showInfoWindow();
        }
    }
}
