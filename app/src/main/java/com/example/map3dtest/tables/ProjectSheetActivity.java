package com.example.map3dtest.tables;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nbapp.R;
import com.example.map3dtest.Utils.CustomAlertDialogs;
import com.example.map3dtest.Utils.Position2ID;
import com.example.map3dtest.charts.ChartActivitySec;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.main.SmoothMoveActivity;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.recyclerviewadapter.ProjectAdapter;
import com.example.map3dtest.recyclerviewinterface.MyOnItemClickListener;
import com.example.map3dtest.recyclerviewinterface.MyOnItemLongClickListener;
import com.example.map3dtest.transformdata.TransformData;

import java.util.ArrayList;
import java.util.List;


/**
 * Tables包存储表格加载相关活动
 */
public class ProjectSheetActivity extends AppCompatActivity implements View.OnClickListener{

    private List<String> mPrjDevDatas0;
    private List<String> mPrjDevDatas1;
    private RecyclerView mPrjsheetDevRecyView;
    private ProjectAdapter recycleProjectAdapter;
    private LinearLayoutManager proSheetLayoutManager;
    private TextView mDeviceContent0;
    private TextView mDeviceContent1;
    private TextView mDeviceContent8;

    private String queryPrjName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj_sheet);
        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {
                Log.d("haha", "ProjectSheetActivity:" + " " + body);
                if(body.getType().equals("DSI") && body.getField().equals("numberPlate") && body.getQuerymode().equals("findByProjectInformation")){
                    String data[] = body.getP0().trim().split("#");

                    if(data.length != 0){
                        //data[0]为空
                        Log.d("haha", "ProjectSheetActivity:" + " " + data[0]);
                        mPrjDevDatas0.clear();
                        mPrjDevDatas1.clear();
                        for(int i = 1; i < data.length; i++){
                            mPrjDevDatas0.add(data[i]);
                            //mPrjDevDatas1.add(data[i]);
                            mPrjDevDatas1.add("点击查看");
                        }
                        recycleProjectAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        //以下为recyclerView代码
        mPrjsheetDevRecyView = (RecyclerView)findViewById(R.id.prjsheet_dev_recycler);
        mDeviceContent0 = findViewById(R.id.device_content_0);
        mDeviceContent1 = findViewById(R.id.device_content_1);
        mDeviceContent8 = findViewById(R.id.device_content_8);

        //空数据，但还是先让mPrjDevdatas指向点东西
        mPrjDevDatas0 = new ArrayList<>();
        mPrjDevDatas1 = new ArrayList<>();
      /*  mPrjDevdatas0.add("yi");
        mPrjDevDatas1.add("er");*/

        recycleProjectAdapter = new ProjectAdapter(ProjectSheetActivity.this, mPrjDevDatas0, mPrjDevDatas1);
        proSheetLayoutManager = new LinearLayoutManager(this);
        mPrjsheetDevRecyView.setLayoutManager(proSheetLayoutManager);
        proSheetLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mPrjsheetDevRecyView.setAdapter(recycleProjectAdapter);
        mPrjsheetDevRecyView.setItemAnimator(new DefaultItemAnimator());

        /*点击事件*/
        recycleProjectAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                //CustomAlertDialogs.simple(ProjectSheetActivity.this);
                myShowDialog(position);
                //Toast.makeText(getApplicationContext(), "position:" + position + " " + mPrjDevDatas0.get(position), Toast.LENGTH_SHORT).show();
           /*     Intent intent =new Intent(ProjectSheetActivity.this,ChartActivitySec.class);
                startActivity(intent);*/
            }
        });

        /*长按事件*/
        recycleProjectAdapter.setOnItemLongClickListener(new MyOnItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(View view, int position) {
                mPrjDevDatas0.remove(position);//长按后将删除条目
                mPrjDevDatas1.remove(position);
                recycleProjectAdapter.notifyItemRemoved(position);
            }
        });

        getQueryPrjName();

        //查询项目部信息
        NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>numberPlate</field><type>DSI</type>" +
                "<querymode>findByProjectInformation</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>" + queryPrjName + "</p3><p4>empty</p4><p5>empty</p5></query>", 0);
        Log.d("haha", "queryPDI");
    }

    private void getQueryPrjName(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TransformData trans = bundle.getParcelable("positionSec");
        queryPrjName = trans.getPrjName();
        Log.d("haha", "prjName:" + queryPrjName);

        if(queryPrjName.equals("太湖隧道项目")){
            mDeviceContent0.setText(queryPrjName);
            mDeviceContent1.setText("江苏省无锡市");
            mDeviceContent8.setText("");
        }else{
            mDeviceContent0.setText("无");
            mDeviceContent1.setText("无");
            mDeviceContent8.setText("无");
        }
    }

    @Override
    public void onClick(View v){

    }

    public void myShowDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("查询类型")
                //.setMessage("对话框")//注：若开启message则不能显示选择项
                .setCancelable(true)
                .setSingleChoiceItems(CustomAlertDialogs.choiceItems, 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Log.d("haha", "选中了" + which);
                        Bundle data = new Bundle();
                        String ID = Position2ID.p2ID(position);
                        TransformData trans = new TransformData(ID, mPrjDevDatas0.get(position), "");
                        data.putParcelable("position", trans);
                        Intent intent =new Intent();
                        intent.putExtras(data);
                        switch (which){

                            case 0:
                                intent.setClass(ProjectSheetActivity.this, ChartActivitySec.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent.setClass(ProjectSheetActivity.this, SmoothMoveActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
