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
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import com.example.map3dtest.Utils.CustomToast;
import com.example.map3dtest.activitycollector.ActivityCollector;
import com.example.map3dtest.activitycollector.BaseActivity;
import com.example.map3dtest.markers.MarkerManager;
import com.example.map3dtest.search.AddressSelect;
import com.example.nbapp.R;
import com.example.map3dtest.Utils.ByteCompile;
import com.example.map3dtest.Utils.GlobalStateManager;
import com.example.map3dtest.adapter.InfoWinAdapter;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;


import java.util.ArrayList;
import java.util.Iterator;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import mlxy.utils.T;

public class MapDeviceActivity extends BaseActivity implements AMap.OnMapClickListener, AMap.OnMarkerClickListener, View.OnClickListener, OnAddressSelectedListener{


    private MapView mapView = null;
    private AMap aMap;

    private InfoWinAdapter adapter;
    private ArrayList currentEntityList = new ArrayList<MarkerManager>();

    private boolean markerflag = false;//false为


    private LinearLayout jdSelect;

    private int counter = 0;
    private ImageView Ighome_page_0;
    private ImageView Ighome_page_1;
    private ImageView Ighome_page_2;
    private ImageView Ighome_page_3;
    private TextView msearch_tv;

    String[] IdString = {};
    String[] DvString = {};
    String[] PrjString = {};
    String[] PlateString = {};

    private int markerCounter = 0;

    private Handler mHandler = new Handler();

/*  这段代码与launchMode="singleTask"配合使用，参见：  https://blog.csdn.net/findsafety/article/details/9664061
  @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
        GlobalStateManager.projectdevice = false;
        clearMarkers();
        queryDevice();
    }

    private void processExtraData(){
        Intent intent = getIntent();
        //use the data received here
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_map);

        Log.d("hahaha", "MapDeviceActivity onCreate!");

        mapView = (MapView)findViewById(R.id.device_page_mapview_0);
        mapView.onCreate(savedInstanceState);

        initView();
        initMap();

        GlobalStateManager.projectdevice = false;
        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {

                //第一步，拿到所有设备的信息，
                //注:若为 #1#,解析出的 data.length = 2
                if(body.getType().equals("DSI") && body.getField().equals("all") && body.getQuerymode().equals("findAll")){
                    Log.d("haha", "receive all the dsi datas");
                    IdString = body.getP2().trim().split("#");
                    DvString = body.getP9().trim().split("#");
                    PrjString = body.getP4().trim().split("#");
                    PlateString = body.getP5().trim().split("#");

                    for(int i = 1; i < IdString.length; i++) {//解析后数组中IdString[0]为空的
                        Log.d("haha", "fasong: " + IdString[i] + "  " + DvString[i]);

                        //第二步按照设备列表依次查询GPSInformation
                        NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd>" +
                                "<field>GPSInformation</field><type>DDI</type><querymode>findLatest</querymode><p0>" + IdString[i] +
                                "</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>null</p5></query>", i * 400);//4526
                    }

                }else if(body.getType().equals("DDI") && body.getField().equals("GPSInformation") && body.getQuerymode().equals("findLatest")){
                    Log.d("haha", "收到数据");
                    String[] LatLngString = body.getP0().trim().split("#");
                    //Log.d("haha", "内容: " + LatLngString[0] + " a" + LatLngString[1] + "size: " + LatLngString.length);
                    Log.d("haha",  "size: " + LatLngString.length);
                    if(LatLngString.length == 0){

                       /* 其实isEmpty完全等同于string.length()==0如果String本身是null，那么使用string.isEmpty()会报空指针
                       异常（NullPointerException)判断一个String为空的最安全的方法，还是 string ==null || string.isEmpty()

                        作者：之韦华
                        链接：https://www.zhihu.com/question/20570393/answer/15511269
                        来源：知乎
                        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。*/
                        Log.d("haha", "空");

                    }else{
                        Log.d("haha", "非空");

                        //这段代码后期需要封装，拟定采用内部类
                        byte[] mLatLng = Base64.decode(LatLngString[1], Base64.DEFAULT);//LatLngString[1]中存储了数据，而LatLngString[0]中为空

                        LatLng resultLatLng = new LatLng(ByteCompile.byte2Int(mLatLng[0], mLatLng[1], mLatLng[2], mLatLng[3]
                        ) * 90.0 / Integer.MAX_VALUE, ByteCompile.byte2Int(mLatLng[4], mLatLng[5], mLatLng[6], mLatLng[7]
                        ) * 180.0 / Integer.MAX_VALUE);
                        Log.d("haha", "MapDeviceActivity" + resultLatLng);
                        Log.d("haha", "MapDeviceActivity" + body);

                        markerCounter++;
                        //Toast.makeText(getApplicationContext(), "获取到第" + markerCounter + "个新数据！" , Toast.LENGTH_SHORT).show()
                        CustomToast.showToast(MapDeviceActivity.this,  "获取到第" + markerCounter + "个新数据！", 400);
                        //切换地图显示区域
                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(resultLatLng,17,30,0));
                        aMap.moveCamera(mCameraUpdate);

                        addDeviceMarkerToMap(resultLatLng, getTitle(body.getId()));
                    }

                }

            }
        });

        Log.d("haha", "startquery");
        queryDevice();
    }


    @Override
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.device_jdselector:
                BottomDialog dialog = new BottomDialog(MapDeviceActivity.this);
                dialog.setOnAddressSelectedListener(MapDeviceActivity.this);
                dialog.show();
                break;

            case R.id.search_tv_device:
                intent.setClass(MapDeviceActivity.this, AddressSelect.class);
                startActivity(intent);
                //finish();
                break;



            case R.id.home_page_device_0:
                intent.setClass(MapDeviceActivity.this, MapProjectActivity.class);
                startActivity(intent);
                //finish();
                break;

            case R.id.home_page_device_1:

            case R.id.home_page_device_2:
                intent.setClass(MapDeviceActivity.this, DeviceMaintainActivity.class);
                startActivity(intent);
                //finish();
                break;

            case R.id.home_page_device_3:
                intent.setClass(MapDeviceActivity.this, AccountManageActivity.class);
                startActivity(intent);
                //finish();
                break;



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


    }


    private String getTitle(String Id){
        for(int i = 1; i < IdString.length; i++){
            if(IdString[i].equals(Id)){
                return IdString[i] + "#" + PrjString[i] + "#" + PlateString[i] + "#" + DvString[i];
            }

        }
        return "无数据#无数据#无数据#无数据";
    }

    public void queryDevice(){
        NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field>" +
                "<type>DSI</type><querymode>findAll</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3>" +
                "<p4>empty</p4><p5>empty</p5></query>", 0);

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
                            /*
                        可在调用ActivityCollector.getActivityCollector().finishAll();之后，
                        再调用killProcess()，以保证完全退出。

                        注意：killProcess()只能用于杀掉当前进程，不能杀死其他进程
                         */
                        ActivityCollector.getActivityCollector().finishAll();
                        android.os.Process.killProcess(android.os.Process.myPid());
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
        mapView = (MapView) findViewById(R.id.device_page_mapview_0);//地图
        jdSelect = (LinearLayout)findViewById(R.id.device_jdselector);//唤醒popupWindow的按键
        jdSelect.setOnClickListener(this);
        msearch_tv = (TextView)findViewById(R.id.search_tv_device);
        msearch_tv.setOnClickListener(this);


        Ighome_page_0 = (ImageView)findViewById(R.id.home_page_device_0);
        Ighome_page_1 = (ImageView)findViewById(R.id.home_page_device_1);
        Ighome_page_2 = (ImageView)findViewById(R.id.home_page_device_2);
        Ighome_page_3 = (ImageView)findViewById(R.id.home_page_device_3);

        Ighome_page_0.setOnClickListener(this);
        Ighome_page_1.setOnClickListener(this);
        Ighome_page_2.setOnClickListener(this);
        Ighome_page_3.setOnClickListener(this);


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
