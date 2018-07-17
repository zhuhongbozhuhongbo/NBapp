package com.example.map3dtest.charts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.nbapp.R;
import com.example.map3dtest.Utils.Position2ID;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.transformdata.TransformData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import chihane.jdaddressselector.picker.PickDatePopupWindow;
import chihane.jdaddressselector.picker.PickTimePopupWindow;

/**
 * 通过netty的handler(xmlframedecoder)实现了粘包处理
 * 暂时不使用Handler的postDelayed，不开启定时刷新（手动向服务器查询数据）
 */

public class ChartActivitySec extends AppCompatActivity implements View.OnClickListener{

    private LineChart mASChart;
    private LineChart mACChart;
    private LineChart mRFChart;
    private LineChart mCSChart;
    private ArrayList<Entry> values ;//new ArrayList<>();

    private Button mquery_btn;//启动时间选择器的按钮
    private Button mdate_select;
    private TextView mchart_device_name;
    private EditText mid_select;
    private ImageView mimg_backward;

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

    private String queryID = "4";
    //private Handler mHandler = new Handler();//用于定时向服务器查询的handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_sec);

        mASChart = (LineChart)findViewById(R.id.AngularSpeed_linechart);
        mACChart = (LineChart)findViewById(R.id.Acceleration_linechart);
        mRFChart = (LineChart)findViewById(R.id.RemainedFuel_linechart);
        mCSChart = (LineChart)findViewById(R.id.CurrentSpeed_linechart);
        mdate_select = (Button)findViewById(R.id.date_select);
        mquery_btn = (Button)findViewById(R.id.query_btn);
        mchart_device_name = findViewById(R.id.chart_device_name);
        mid_select = findViewById(R.id.id_select);
        mimg_backward = (ImageView)findViewById(R.id.img_backward);

        //从前一个acrivity取出ID
        getQueryID();
        //设置datapicker的默认值为当天
        initQueryBox();

        //注意在执行getQueryID()后才得到numberPlate的值
        mchart_device_name.setText(numberPlate);

        mquery_btn.setOnClickListener(this);
        mid_select.setOnClickListener(this);
        mimg_backward.setOnClickListener(this);

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
        initCharts();

        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener(){
            @Override
            public void onDataReceive(int mt, ChartDatas body){

                if(body.getType().equals("DDI") && body.getField().equals("containingFuel")){
                    Log.d("haha", "ChartActivity onDataReceive Fuel" + body.getP0());
                    String chartData[] = body.getP0().trim().split("#");
                    //Log.d("haha", "chartData length:" + chartData.length);
                    drawChart(chartData, mRFChart);
                    //values.clear();

                }else if(body.getType().equals("DDI") && body.getField().equals("angleZ")){
                    Log.d(TAG, "ChartActivity onDataReceive angleZ" + body.getP0());
                    String chartData[] = body.getP0().trim().split("#");
                    drawChart(chartData, mASChart);
                }else if(body.getType().equals("DDI") && body.getField().equals("zAcceleration")){
                    Log.d(TAG, "ChartActivity onDataReceive zAcc" + body.getP0());
                    String chartData[] = body.getP0().trim().split("#");
                    drawChart(chartData, mACChart);

                }else if(body.getType().equals("DDI") && body.getField().equals("currentSpeed")){
                    Log.d(TAG, "ChartActivity onDataReceive currSpeed" + body.getP0());
                    String chartData[] = body.getP0().trim().split("#");
                    drawChart(chartData, mCSChart);
                }
            }
        });

        //mHandler.postDelayed(new QueryRunnable(), 1000);//查询最新的，定时刷新

    }

    private void drawChart(String[] chartData, LineChart mChart){
        if(chartData.length != 0) {
            float tempData;
            values = new ArrayList<>();
            int i = 1;
            for (i = 1; i < chartData.length; i++) {
                tempData = Float.parseFloat(chartData[i]);
                values.add(new Entry(i - 1, tempData));
            }

            LineDataSet dataSet = new LineDataSet(values, "Label");
            LineData lineData = new LineData(dataSet);
            mChart.setData(lineData);
            mChart.invalidate();//refresh
        }else{
            Log.d("haha", "chartData为空");
        }
    }

    private void getQueryID(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TransformData trans = bundle.getParcelable("deviceData");
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

    /**
     * MPAndroidChart属性介绍：
     * https://blog.csdn.net/u014769864/article/details/71479591
     */
    private void initCharts(){
        mASChart.getDescription().setEnabled(false);
        mASChart.setNoDataText("选择时间/日期以获取角速度图表");
        Description descriptionAS = new Description();
        descriptionAS.setText("单位：弧度/秒");
        //设置描述信息
        mASChart.setDescription(descriptionAS);
        mASChart.getLegend().setEnabled(false);

        mACChart.getDescription().setEnabled(false);
        mACChart.setNoDataText("选择时间/日期以获取加速度图表");
        Description descriptionAC = new Description();
        descriptionAC.setText("单位：米/二次方秒");
        //设置描述信息
        mACChart.setDescription(descriptionAC);
        mACChart.getLegend().setEnabled(false);

        mRFChart.getDescription().setEnabled(false);
        mRFChart.setNoDataText("选择时间/日期以获取剩余油量图表");
        Description descriptionRF = new Description();
        descriptionRF.setText("单位：升");
        //设置描述信息
        mRFChart.setDescription(descriptionRF);
        mRFChart.getLegend().setEnabled(false);

        mCSChart.getDescription().setEnabled(false);
        mCSChart.setNoDataText("选择时间/日期以获取当前线速度图表");
        Description descriptionCS = new Description();
        descriptionCS.setText("单位：米/秒");
        //设置描述信息
        mCSChart.setDescription(descriptionCS);
        mCSChart.getLegend().setEnabled(false);

        //设置一页最大显示个数为6，超出部分就滑动
        //float ratio = (float) i/(float) 6;//i表征了横轴点个数
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mASChart.zoom(10,1f,0,0);
        mACChart.zoom(10,1f,0,0);
        mRFChart.zoom(10,1f,0,0);
        mCSChart.zoom(10,1f,0,0);

  /*      Legend legend = mASChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置所有图例位置排序方向
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.CIRCLE);*/



    }



 /*   查询最新的，定时刷新
      class QueryRunnable implements Runnable{

        @Override
        public void run() {
            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<command><user_id>001</user_id><passwd>aaa</passwd><content>findLatestAngularSpeed</content><iot_Device_id>00001</iot_Device_id><startStamp>empty</startStamp><stopStamp>empty</stopStamp><someDay>2018-3-16</someDay></command>", 0);
                //每隔1s执行一次Run方法
            mHandler.postDelayed(this, 1000);
            Log.d("haha", "刷新");
        }
    }*/

    private void initDate() {//dialogpicker
        mdate_select = (Button) findViewById(R.id.date_select);
        mdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给出一个默认值或者是回去mTimeContent中的String格式化

                PickDatePopupWindow pickDatePopupWindow = new PickDatePopupWindow(ChartActivitySec.this, saved_year, saved_month, saved_day);
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.query_btn:
                //设备动态信息
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>containingFuel</field><type>DDI</type>" +
                        "<querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>23:59:59</p4><p5>" +
                        "null</p5></query>", 0);
                /*Log.d("haha", "xm" + "<query><userid>001</userid><passwd>aaa</passwd><field>containingFuel</field><type>DDI</type>" +
                        "<querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>23:59:59</p4><p5>" +
                        "null</p5></query>");*/
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>angleZ</field><type>DDI</type>" +
                        "<querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>23:59:59</p4><p5>null</p5></query>", 0);
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>zAcceleration</field><type>DDI</type>" +
                        "<querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>23:59:59</p4><p5>null</p5></query>", 0);
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>currentSpeed</field><type>DDI</type>" +
                        "<querymode>findByIdAndHour</querymode><p0>" + queryID + "</p0><p1>" + dateText + "</p1><p2>empty</p2><p3>00:00:00</p3><p4>23:59:59</p4><p5>null</p5></query>", 0);
         /*       NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<command><user_id>001</user_id><passwd>aaa</passwd><content>AccelerationApp</content>" +
                        "<iot_Device_id>00001</iot_Device_id><startStamp>empty</startStamp><stopStamp>empty</stopStamp><someDay>2018-4-11</someDay></command>", 0);
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<command><user_id>001</user_id><passwd>aaa</passwd><content>CurrentSpeedApp</content>" +
                        "<iot_Device_id>00001</iot_Device_id><startStamp>empty</startStamp><stopStamp>empty</stopStamp><someDay>2018-4-11</someDay></command>", 0);
                NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<command><user_id>001</user_id><passwd>aaa</passwd><content>RemainedFuelApp</content>" +
                        "<iot_Device_id>00001</iot_Device_id><startStamp>empty</startStamp><stopStamp>empty</stopStamp><someDay>2018-4-11</someDay></command>", 0);
*/
                break;

            case R.id.img_backward:
                finish();
                Log.d("haha", "退出");//仍然能在控制台打印出这句
                break;
        }
    }


}
