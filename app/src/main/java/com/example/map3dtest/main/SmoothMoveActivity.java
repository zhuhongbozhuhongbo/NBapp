package com.example.map3dtest.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.example.nbapp.R;
import com.example.map3dtest.Utils.ByteCompile;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.transformdata.TransformData;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import chihane.jdaddressselector.picker.PickDatePopupWindow;

/**
 * 包名： com.amap.map3d.demo.smooth
 * <p>
 * 创建时间：2016/12/5
 * 项目名称：AMap3DDemo
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 * <p>
 * 类说明：应用于出行应用的小车平滑移动
 *
 *
 * 修改获得经纬度数据，小车开始移动后的地图缩放级别需要将原来的mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50)); 改为
 * mAMap.animateCamera(CameraUpdateFactory.zoomTo(10));（或者说不在使用newLatLngBounds这个方法）。
 * mAMap.animateCamera(CameraUpdateFactory.zoomTo(10))与SmoothMoveActivity的181~183行共同控制缩放。
 */
public class SmoothMoveActivity extends Activity implements View.OnClickListener{
    private Button mquery_btn;//启动时间选择器的按钮
    private Button mdate_select;
    private TextView msmooth_device_name;
    private EditText mid_select;
    private ImageView msmooth_backward;

    private int saved_year = 2018;
    private int saved_month = 6;
    private int saved_day = 5;
    private int saved_hour = 0;
    private int saved_minute = 0;
    private int saved_second = 0;

    private String dateText = "2018-7-5";
    private String timeText = "00:00:00";
    private String TAG = "ChartActivity";
    private String numberPlate = "";

    private MapView mMapView;
    private AMap mAMap;
    private Polyline mPolyline;
    private String queryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smooth_move);
        mdate_select = (Button) findViewById(R.id.smooth_date_select);
        mid_select = findViewById(R.id.smooth_id_select);
        mquery_btn = (Button)findViewById(R.id.smooth_query_btn);
        msmooth_device_name = findViewById(R.id.smooth_device_name);
        msmooth_backward = findViewById(R.id.smooth_backward);

        getQueryID();
        initQueryBox();

        msmooth_device_name.setText(numberPlate);

        mquery_btn.setOnClickListener(this);
        mid_select.setOnClickListener(this);
        msmooth_backward.setOnClickListener(this);

        mid_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println("监听EditText输入内容的变化，在这里可以监听输入内容的长度。");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //System.out.println("这里可以实现所输即所得，用户输入的同时可以立即在这里根据输入内容执行操作，显示搜索结果！");
                queryID = "" + s;
                Log.d("haha",  "Id:" + queryID);
            }
        });

        mid_select.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    //System.out.println("手指弹起时执行确认功能");
                    Log.d("haha", "anxia");
                    return true;
                }

                return false;
            }
        });

        initDate();

        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
                                                             @Override
                                                             public void onDataReceive(int mt, ChartDatas body) {

                                                                 if (body.getType().equals("DDI") && body.getField().equals("GPSInformation")&&body.getQuerymode().equals("findByIdAndHour")) {
                                                                     Log.d("haha", "ChartActivity onDataReceive GPSInformation " + body.getP0());
                                                                     String chartData[] = body.getP0().trim().split("#");
                                                                     Log.d("haha", "chartData length:" + chartData.length);
                                                                     if(chartData.length != 0){
                                                                         //注：#开头，故data[0]为空
                                                                         doubLatLng = new double[(chartData.length - 1) * 2];
                                                                         for(int i = 1; i < chartData.length; i++) {
                                                                             byte[] mLatLng = Base64.decode(chartData[i], Base64.DEFAULT);


                                                                             //Log.d("haha", "chartdata0:  " + chartData[1] + " chartdatasiz" + chartData.length);

                                                                             /*Log.d("haha", "after" + ByteCompile.byte2Int(mLatLng[0], mLatLng[1], mLatLng[2], mLatLng[3]
                                                                             ) * 90.0 / Integer.MAX_VALUE);
                                                                             Log.d("haha", "after" + ByteCompile.byte2Int(mLatLng[4], mLatLng[5], mLatLng[6], mLatLng[7]
                                                                             ) * 180.0 / Integer.MAX_VALUE);*/
                                                                             doubLatLng[(i - 1) * 2] = ByteCompile.byte2Int(mLatLng[4], mLatLng[5], mLatLng[6], mLatLng[7]
                                                                             ) * 180.0 / Integer.MAX_VALUE;
                                                                             doubLatLng[(i - 1) * 2 + 1] = ByteCompile.byte2Int(mLatLng[0], mLatLng[1], mLatLng[2], mLatLng[3]
                                                                             ) * 90.0 / Integer.MAX_VALUE;
                                                                             //Log.d("haha", doubLatLng[(i - 1) * 2] + "," + doubLatLng[(i - 1) * 2 + 1]);

                                                                         }

                                                                          /*LatLng updateLatLng = new LatLng(doubLatLng[1], doubLatLng[0]);
                                                                     CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(updateLatLng,8,30,0));
                                                                     mAMap.moveCamera(mCameraUpdate);*/
                                                                         LatLng updateLatLng = new LatLng(doubLatLng[1], doubLatLng[0]);
                                                                         CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(updateLatLng,8,30,0));
                                                                         mAMap.moveCamera(mCameraUpdate);
                                                                         setLine();
                                                                         startMove();
                                                                     }else if(chartData.length == 0){
                                                                         //此处为报告“无数据”的代码

                                                                         Toast.makeText(getApplicationContext(), "未查询到数据！" , Toast.LENGTH_SHORT).show();
                                                                     }


                                                                 }
                                                             }
                                                         });

                    mMapView = (MapView) findViewById(R.id.smooth_home_page_mapview_0);
        mMapView.onCreate(savedInstanceState);

        /*NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field>" +
                "<type>DDI</type><querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>" +
                "23:59:59</p4><p5>null</p5></query>", 0);*/


        init();


    }

    private double[] doubLatLng;

    private void getQueryID(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TransformData trans = bundle.getParcelable("deviceTrack");
        queryID = trans.getID();
        //Log.d("haha", "ID" + queryID);
        numberPlate = trans.getNumberPlate();
    }

    //将下侧搜索框日期、ID号初始化为默认值
    private void  initQueryBox(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        saved_year = cal.get(Calendar.YEAR);//获取年份
        saved_month=cal.get(Calendar.MONTH) + 1;//获取月份,注意月份的起始值为0而非1
        saved_day=cal.get(Calendar.DATE);//获取日
        //Log.d("haha", saved_year + " " + saved_month + " " + saved_day);
        dateText = String.format("%d-%02d-%02d", saved_year, saved_month, saved_day);
        mdate_select.setText(dateText);
        mid_select.setHint("已选择:" + queryID + " 点击重新输入ID");
    }

    private void initDate() {//dialogpicker
        mdate_select = (Button) findViewById(R.id.smooth_date_select);
        mdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给出一个默认值或者是回去mTimeContent中的String格式化

                PickDatePopupWindow pickDatePopupWindow = new PickDatePopupWindow(SmoothMoveActivity.this, saved_year, saved_month, saved_day);
                pickDatePopupWindow.setDatePickListener(new PickDatePopupWindow.DatePickListener() {
                    @Override
                    public void onPickDate(Integer year, Integer month, Integer day) {
                        dateText = String.format("%d-%02d-%02d", year, month, day);
                        saved_day = day;
                        saved_month = month;
                        saved_year = year;
                        //Log.d("haha", dateText);
                        mdate_select.setText(dateText);
                  /*      NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<command><user_id>001</user_id><passwd>aaa</passwd><content>AngularSpeedApp</content><iot_Device_id>00001</iot_Device_id><startStamp>empty</startStamp><stopStamp>empty</stopStamp><someDay>2018-4-11</someDay></command>", 0);
                     */
                    }
                });
                pickDatePopupWindow.showAtLocation(mdate_select, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.smooth_query_btn:
                //设备动态信息
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field>" +
                        "<type>DDI</type><querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>" +
                        "23:59:59</p4><p5>null</p5></query>", 0);
                break;
            case R.id.smooth_backward:
                finish();
                Log.d("haha", "退出");//仍然能在控制台打印出这句
                break;

        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();

        }
     }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    public void setLine() {
        addPolylineInPlayGround();
    }

    /**
     * 开始移动
     */
    public void startMove() {

        if (mPolyline == null) {
            //ToastUtil.showShortToast(this, "请先设置路线");
            Log.d("haha", "请先设置路线");
            return;
        }



        // 读取轨迹点
        List<LatLng> points = readLatLngs();
        // 构建 轨迹的显示区域
        //LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));

        //注：此处原来为mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));  现在改为下列代码，与SmoothMoveActivity的181~183行共同控制缩放。
        mAMap.animateCamera(CameraUpdateFactory.zoomTo(14));



      /*
      自己加的代码
      LatLng updateLatLng = new LatLng(doubLatLng[1], doubLatLng[0]);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(updateLatLng,8,30,0));
        mAMap.moveCamera(mCameraUpdate);*/


        // 实例 SmoothMoveMarker 对象
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(mAMap);
        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.smooth_icon_car));

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(25);

        // 设置  自定义的InfoWindow 适配器
        mAMap.setInfoWindowAdapter(smoothInfoWindowAdapter);
        // 显示 infowindow
        smoothMarker.getMarker().showInfoWindow();

        // 设置移动的监听事件  返回 距终点的距离  单位 米
        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(final double distance) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (infoWindowLayout != null && title != null) {

                            title.setText("距离终点还有： " + (int)distance + "米");
                        }
                    }
                });

            }
        });

        // 开始移动
        smoothMarker.startSmoothMove();
        Log.d("haha", "开始生效");

    }

    /**
     *  个性化定制的信息窗口视图的类
     *  如果要定制化渲染这个信息窗口，需要重载getInfoWindow(Marker)方法。
     *  如果只是需要替换信息窗口的内容，则需要重载getInfoContents(Marker)方法。
     */
    AMap.InfoWindowAdapter smoothInfoWindowAdapter = new AMap.InfoWindowAdapter(){

        // 个性化Marker的InfoWindow 视图
        // 如果这个方法返回null，则将会使用默认的信息窗口风格，内容将会调用getInfoContents(Marker)方法获取
        @Override
        public View getInfoWindow(Marker marker) {

            return getInfoWindowView(marker);
        }

        // 这个方法只有在getInfoWindow(Marker)返回null 时才会被调用
        // 定制化的view 做这个信息窗口的内容，如果返回null 将以默认内容渲染
        @Override
        public View getInfoContents(Marker marker) {

            return getInfoWindowView(marker);
        }
    };

    LinearLayout infoWindowLayout;
    TextView title;
    TextView snippet;

    /**
     * 自定义View并且绑定数据方法
     * @param marker 点击的Marker对象
     * @return  返回自定义窗口的视图
     */
    private View getInfoWindowView(Marker marker) {
        if (infoWindowLayout == null) {
            infoWindowLayout = new LinearLayout(this);
            infoWindowLayout.setOrientation(LinearLayout.VERTICAL);
            title = new TextView(this);
            snippet = new TextView(this);
            title.setTextColor(Color.BLACK);
            snippet.setTextColor(Color.BLACK);
            infoWindowLayout.setBackgroundResource(R.drawable.infowindow_bg);

            infoWindowLayout.addView(title);
            infoWindowLayout.addView(snippet);
        }

        return infoWindowLayout;
    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {
        List<LatLng> list = readLatLngs();
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0),Color.argb(255, 255, 255, 0),Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.smooth_custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = mAMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.smooth_custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));

        LatLngBounds bounds = new LatLngBounds(list.get(0), list.get(list.size() - 2));
        //mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /**
     * 读取坐标点
     * @return
     */
    private List<LatLng> readLatLngs() {
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < doubLatLng.length; i += 2) {
            points.add(new LatLng(doubLatLng[i+1], doubLatLng[i]));
        }
        return points;
    }


}
