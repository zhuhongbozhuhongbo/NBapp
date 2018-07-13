package com.example.map3dtest.functest;

/**
 * Created by 朱宏博 on 2018/7/6.
 */


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import com.example.map3dtest.Utils.ByteCompile;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.nbapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import android.util.Base64;


import chihane.jdaddressselector.javautilbase64decode.JavaBase64Decode;
import chihane.jdaddressselector.picker.PickDatePopupWindow;
import chihane.jdaddressselector.picker.PickTimePopupWindow;


public class FuncActivitySec extends AppCompatActivity implements View.OnClickListener{
    private Button mbtn0;
    private Button mbtn1;
    private Button mbtn2;
    private Button mbtn3;
    private Button mbtn4;
    private TextView mtv0;
    private ArrayList<Entry> values;
    private LineChart mTestChart;

    private TextView mtime_select;//启动时间选择器的按钮
    private TextView mdate_select;
    private EditText mid_select;

    private int saved_year = 2018;
    private int saved_month = 6;
    private int saved_day = 22;
    private int saved_hour = 0;
    private int saved_minute = 0;
    private int saved_second = 0;
    private String Id = "4";

    private String dateText = "2018-6-22";
    private String timeText = "00:00:00";
    private String TAG = "ChartActivity";

    private float scale = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);
        initView();
        initDate();
        initTime();

        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {
                Log.e("haha", "FuncActivity收到:");
                String result = "querymode:" + body.getQuerymode() + " type:" + body.getType() + " field:" +
                        body.getField() + " p1:" + body.getP1() + " " + " p2:" + body.getP2() + " " + " p3:" + body.getP3() + " "
                        + " p4:" + body.getP4() + " " + " p5:" + body.getP5() + " " + " p6:" + body.getP6() + " " + " p7:" + body.getP7() + " " + " p8:" + body.getP8() + " "
                        + " p9:" + body.getP9() + " " + " p10:" + body.getP10() + " p0:" + body.getP0() + " " ;

                if(body.getType().equals("DDI")){

                }else if(body.getType().equals("DSI")){

                }else if(body.getType().equals("SI")){
                    mtv0.setText("date:\n" + body.getP2() + "\nfuelCost:\n" + body.getP3());
                }else if(body.getType().equals("PDI")){

                }else if(body.getType().equals("II")){

                }else if(body.getType().equals("USER")){

                }


  /*              String data[] = body.getP0().trim().split("#");
                Log.d("haha", "size: " + data.length + "内容为：" + result);
                Log.d("haha", "data部分内容为：" + "0 " + data[0] + " 1 " + data[1] + " 2 " + data[2]
                        + " 3 " + data[3] + " 4 " + data[4] + " 5 " + data[5] + " 6 " + data[6] + " 7 " + data[7] + " 8 " + data[8] + " 9 " + data[9]);

*/
            }


        });

    }

    private void initView(){
        mbtn0 = findViewById(R.id.button0);
        mbtn1 = findViewById(R.id.button1);
        mbtn2 = findViewById(R.id.button2);
        mbtn3 = findViewById(R.id.button3);
        mbtn4 = findViewById(R.id.button4);
        mtv0 = findViewById(R.id.tv0);
        mTestChart = findViewById(R.id.testChart);
        mid_select = findViewById(R.id.testIdSelect);

        float ratio = 2;//(float) i / (float) (1000);//i表征了横轴点个数
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mTestChart.zoom(ratio, 1f, 0, 0);

        mbtn0.setOnClickListener(this);
        mbtn1.setOnClickListener(this);
        mbtn2.setOnClickListener(this);
        mbtn3.setOnClickListener(this);
        mbtn4.setOnClickListener(this);
        mid_select.setOnClickListener(this);

        //mid_select.setInputType(EditorInfo.TYPE_CLASS_PHONE);
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
                Id = "" + s;
                Log.d("haha",  "Id:" + Id);
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


    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.button0){

            //设备动态信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>GPSInformation</field>" +
                    "<type>DDI</type><querymode>findByIdAndHour</querymode><p0>" + Id + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>" +
                    "23:59:59</p4><p5>null</p5></query>", 0);
            Log.d("haha", "xm");
        }else if(v.getId() == R.id.button1){
            //设备静态信息
           /* NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DSI</type>" +
                    "<querymode>findByIotDeviceId</querymode><p0>empty</p0><p1>6</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>empty</p5></query>", 0);
            Log.d("haha", "jt");*/

           /* 查询所有设备
           NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DSI</type>" +
                    "<querymode>findAll</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>empty</p5></query>", 0);
            Log.d("haha", "jt");*/

           /*按项目查询设备所有信息
           NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DSI</type>" +
                    "<querymode>findByProjectInformation</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>太湖隧道项目</p3><p4>empty</p4><p5>empty</p5></query>", 0);
            Log.d("haha", "jt");*/

            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>numberPlate</field><type>DSI</type>" +
                    "<querymode>findByProjectInformation</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>太湖隧道项目</p3><p4>empty</p4><p5>empty</p5></query>", 0);
            Log.d("haha", "jt");
        }else if(v.getId() == R.id.button2){
            //项目部
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>PDI</type>" +
                    "<querymode>findByProjectProvince</querymode><p0>安徽省</p0><p1>empty</p1><p2>empty</p2><p3>null</p3><p4>null</p4><p5>null</p5></query>", 0);
            Log.d("haha", "t");

        }else if(v.getId() == R.id.button3){
            //统计信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE,"<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>SI</type><querymode>findById" +
                    "</querymode><p0>" + Id + "</p0><p1>empty</p1><p2> null</p2><p3> null</p3><p4> null</p4><p5> null</p5></query>", 0);
            Log.d("haha", "tj");
        }else if(v.getId() == R.id.button4){
            //用户信息
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>User</type><querymode>findAll" +
                    "</querymode><p0>null</p0><p1>null</p1><p2>null</p2><p3>null</p3><p4>null</p4><p5>null</p5></query>", 0);
            Log.d("haha", "yh");
        }
    }

    private void initDate() {//dialogpicker
        mdate_select = (TextView) findViewById(R.id.testDateSelect);
        mdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给出一个默认值或者是回去mTimeContent中的String格式化

                PickDatePopupWindow pickDatePopupWindow = new PickDatePopupWindow(FuncActivitySec.this, saved_year, saved_month, saved_day);
                pickDatePopupWindow.setDatePickListener(new PickDatePopupWindow.DatePickListener() {
                    @Override
                    public void onPickDate(Integer year, Integer month, Integer day) {
                        dateText = String.format("%d-%02d-%02d", year, month, day);
                        saved_day = day;
                        saved_month = month;
                        saved_year = year;
                        Log.d("haha", dateText);
                        mdate_select.setText(dateText);
                    }
                });
                pickDatePopupWindow.showAtLocation(mdate_select, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private void initTime(){
        mtime_select = (TextView) findViewById(R.id.testTimeSelect);
        mtime_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给出一个默认值或者是回去mTimeContent中的String格式化

                PickTimePopupWindow pickTimePopupWindow = new PickTimePopupWindow(FuncActivitySec.this, saved_hour, saved_minute, saved_second);
                pickTimePopupWindow.setDatePickListener(new PickTimePopupWindow.DatePickListener() {
                    @Override
                    public void onPickDate(Integer hour, Integer minute, Integer second) {
                        timeText = String.format("%d-%02d-%02d", hour, minute, second);
                        saved_second = second;
                        saved_minute = minute;
                        saved_hour = hour;
                        Log.d("haha", timeText);
                        mtime_select.setText(timeText);

                    }
                });
                pickTimePopupWindow.showAtLocation(mdate_select, Gravity.BOTTOM, 0, 0);
            }
        });
    }
}
