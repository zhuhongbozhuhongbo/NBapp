package com.example.map3dtest.tables;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.example.map3dtest.Utils.ByteCompile;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.transformdata.TransformData;
import com.example.nbapp.R;

public class ProjectDetailActivity extends AppCompatActivity {
    private TextView mprjdetail_content_0;
    private TextView mprjdetail_content_1;
    private TextView mprjdetail_content_2;
    private TextView mprjdetail_content_3;
    private TextView mprjdetail_content_4;
    private TextView mprjdetail_content_5;
    private TextView mprjdetail_content_6;
    private TextView mprjdetail_content_7;
    //private String[] PrjInfoString = {};

    private String queryPrjName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        initView();
        getQueryPrjName();



        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {

                //第一步，拿到所有设备的信息，
                //注:若为 #1#,解析出的 data.length = 2
                if(body.getType().equals("PDI") && body.getField().equals("all") && body.getQuerymode().equals("findByProjectName")){
                    Log.d("haha", "receive all the dsi datas");

                    mprjdetail_content_0.setText(body.getP1());//名称
                    mprjdetail_content_1.setText(body.getP2());//省
                    mprjdetail_content_2.setText(body.getP3());//市
                    mprjdetail_content_3.setText(body.getP5());//状态
                    mprjdetail_content_4.setText(body.getP6());//详情
                    mprjdetail_content_5.setText(body.getP7());//开始日期
                    mprjdetail_content_6.setText(body.getP4());//结束日期


                }

            }
        });
    }

    private void initView(){
        mprjdetail_content_0 = findViewById(R.id.prjdetail_content_0);
        mprjdetail_content_1 = findViewById(R.id.prjdetail_content_1);
        mprjdetail_content_2 = findViewById(R.id.prjdetail_content_2);
        mprjdetail_content_3 = findViewById(R.id.prjdetail_content_3);
        mprjdetail_content_4 = findViewById(R.id.prjdetail_content_4);
        mprjdetail_content_5 = findViewById(R.id.prjdetail_content_5);
        mprjdetail_content_6 = findViewById(R.id.prjdetail_content_6);
        mprjdetail_content_7 = findViewById(R.id.prjdetail_content_7);
    }

    private void getQueryPrjName(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TransformData trans = bundle.getParcelable("prjName");
        queryPrjName = trans.getPrjName();
        Log.d("haha", "prjName:" + queryPrjName);

        queryProject(queryPrjName);

    }

    public void queryProject(String prjName){
        NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field>" +
                "<type>PDI</type><querymode>findByProjectName</querymode><p0>" + prjName + "</p0><p1>empty</p1><p2>empty</p2><p3>null</p3>" +
                "<p4>null</p4><p5>null</p5></query>", 0);

    }
}
