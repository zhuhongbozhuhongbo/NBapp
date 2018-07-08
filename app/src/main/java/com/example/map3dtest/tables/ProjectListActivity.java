package com.example.map3dtest.tables;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nbapp.R;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.recyclerviewadapter.ProjectAdapter;
import com.example.map3dtest.recyclerviewinterface.MyOnItemClickListener;
import com.example.map3dtest.recyclerviewinterface.MyOnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import chihane.jdaddressselector.AddressSelector;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

public class ProjectListActivity extends AppCompatActivity implements OnAddressSelectedListener {
    private RecyclerView pj_recyclerView;
    private List<String> mProDatas0;
    private List<String> mProDatas1;
    private ProjectAdapter recycleProjectAdapter;
    private LinearLayoutManager proLayoutManager;
    private TextView mlocationtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        //以下为recyclerView代码
        pj_recyclerView = (RecyclerView)findViewById(R.id.project_list_recycler);
        getProjectData();
        recycleProjectAdapter = new ProjectAdapter(ProjectListActivity.this, mProDatas0, mProDatas1);
        proLayoutManager = new LinearLayoutManager(this);
        pj_recyclerView.setLayoutManager(proLayoutManager);
        proLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        pj_recyclerView.setAdapter(recycleProjectAdapter);
        pj_recyclerView.setItemAnimator(new DefaultItemAnimator());

        //点击事件
        recycleProjectAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Log.d("haha", "view name:" + view.getId());
                Toast.makeText(getApplicationContext(), "position:" + position, Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(ProjectListActivity.this,ProjectSheetActivity.class);
                startActivity(intent);
            }
        });

        //长按事件
        recycleProjectAdapter.setOnItemLongClickListener(new MyOnItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(View view, int position) {
                mProDatas0.remove(position);//长按后将删除条目
                mProDatas1.remove(position);
                recycleProjectAdapter.notifyItemRemoved(position);
            }
        });


        //以下为地址选择框代码
        AddressSelector selector = new AddressSelector(this);
        selector.setOnAddressSelectedListener(this);
//        selector.setAddressProvider(new TestAddressProvider());


        Button buttonBottomDialog = (Button) findViewById(R.id.project_list_bottom_dialog);
        assert buttonBottomDialog != null;
        buttonBottomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BottomDialog.show(MainActivity.this, MainActivity.this);
                BottomDialog dialog = new BottomDialog(ProjectListActivity.this);
                dialog.setOnAddressSelectedListener(ProjectListActivity.this);
                dialog.show();
            }
        });

        //界面控件初始化
        mlocationtv = findViewById(R.id.location_tv);
        Bundle bundle = this.getIntent().getExtras();
        String location = bundle.getString("location");
        mlocationtv.setText(location);
        refresh();

        NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field><type>DSI</type>" +
                "<querymode>findByIotDeviceId</querymode><p0>empty</p0><p1>6</p1><p2>empty</p2><p3>empty</p3><p4>empty</p4><p5>empty</p5></query>", 0);
        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {
                Log.e("haha", "ProjectListActivity收到:" + body.getType());
            }
        });

    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
/*        String s =
                (province == null ? "" : province.name) +
                        (city == null ? "" : "\n" + city.name) +
                        (county == null ? "" : "\n" + county.name) +
                        (street == null ? "" : "\n" + street.name);*/
        String s =
                (province == null ? "" : province.name) + "#" +
                        (city == null ? "" :city.name) +
                        (county == null ? "" :county.name) +
                        (street == null ? "" :street.name);
        Intent intent = new Intent(ProjectListActivity.this, ProjectListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("location", s);
        intent.putExtras(bundle);
        startActivity(intent);
        //T.showShort(this, s);
    }

    private void refresh(){
        mProDatas1.clear();
        mProDatas0.clear();
        mProDatas0.add("xiangmu0");
        mProDatas1.add("xiangmu1");
        recycleProjectAdapter.notifyDataSetChanged();
    }

    private void getProjectData(){
        mProDatas0 = new ArrayList<>();
        mProDatas1 = new ArrayList<>();

        mProDatas0.add("项目一");
        mProDatas0.add("项目二");
        mProDatas0.add("项目三");
        mProDatas0.add("项目四");
        mProDatas0.add("项目五");
        mProDatas0.add("项目一");
        mProDatas0.add("项目二");
        mProDatas0.add("项目三");
        mProDatas0.add("项目四");
        mProDatas0.add("项目五");
        mProDatas0.add("项目一");
        mProDatas0.add("项目二");
        mProDatas0.add("项目三");
        mProDatas0.add("项目四");
        mProDatas0.add("项目五");
        mProDatas0.add("项目一");
        mProDatas0.add("项目二");
        mProDatas0.add("项目三");
        mProDatas0.add("项目四");
        mProDatas0.add("项目五");

        mProDatas1.add("内容一");
        mProDatas1.add("内容二");
        mProDatas1.add("内容三");
        mProDatas1.add("内容四");
        mProDatas1.add("内容五");
        mProDatas1.add("内容一");
        mProDatas1.add("内容二");
        mProDatas1.add("内容三");
        mProDatas1.add("内容四");
        mProDatas1.add("内容五");
        mProDatas1.add("内容一");
        mProDatas1.add("内容二");
        mProDatas1.add("内容三");
        mProDatas1.add("内容四");
        mProDatas1.add("内容五");
        mProDatas1.add("内容一");
        mProDatas1.add("内容二");
        mProDatas1.add("内容三");
        mProDatas1.add("内容四");
        mProDatas1.add("内容五");
    }


}
