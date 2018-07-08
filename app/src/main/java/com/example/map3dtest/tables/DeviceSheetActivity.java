package com.example.map3dtest.tables;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nbapp.R;

import com.example.map3dtest.charts.ChartActivitySec;
import com.example.map3dtest.transformdata.TransformData;

/**
 * Tables包存储表格加载相关活动
 */
public class DeviceSheetActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mproject_button_0;
    private Button mproject_button_1;
    private TextView mproject_content1;
    private TextView mproject_content2;
    private String ID;
    private String numberPlate;
    private String prjName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_sheet);
        init();

        getQuery();
    }

    private void init(){
        mproject_button_0 = findViewById(R.id.project_button_0);
        mproject_button_1 = findViewById(R.id.project_button_1);
        mproject_content1 = findViewById(R.id.project_content_1);
        mproject_content2 = findViewById(R.id.project_content_2);

        mproject_button_0.setOnClickListener(this);
        mproject_button_1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.project_button_0:
                Intent intent0 = new Intent(this, ProjectSheetActivity.class);
                Bundle data0 = new Bundle();
                TransformData trans0 = new TransformData(ID, numberPlate, prjName);
                data0.putParcelable("positionSec", trans0);
                intent0.putExtras(data0);
                startActivity(intent0);
                break;
            case R.id.project_button_1:
                Intent intent1 = new Intent(this, ChartActivitySec.class);
                Bundle data1 = new Bundle();
                TransformData trans1 = new TransformData(ID, numberPlate, prjName);
                data1.putParcelable("position", trans1);
                intent1.putExtras(data1);
                startActivity(intent1);
                break;
        }

    }

    private void getQuery(){

        TransformData transformDataTrd = getIntent().getParcelableExtra("stu");
        Log.d("haha", "DeviceSheetID" + transformDataTrd.getID());
        Log.d("haha", "DeviceSheetPlate" + transformDataTrd.getNumberPlate());
        Log.d("haha", "DeviceSheetPrj" + transformDataTrd.getPrjName());
        ID = transformDataTrd.getID();
        numberPlate = transformDataTrd.getNumberPlate();
        prjName = transformDataTrd.getPrjName();
        mproject_content1.setText(transformDataTrd.getNumberPlate());
        mproject_content2.setText(transformDataTrd.getPrjName());
    }
}
