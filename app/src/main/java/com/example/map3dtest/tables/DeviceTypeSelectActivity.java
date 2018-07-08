package com.example.map3dtest.tables;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nbapp.R;
import com.example.map3dtest.search.SearchPageActivity;


/**
 * Tables包存储表格加载相关活动
 */
public class DeviceTypeSelectActivity extends AppCompatActivity implements View.OnClickListener{

    private Button device_select_0;
    private Button device_select_1;
    private Button device_select_2;
    private Button device_select_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type_select);
        init();
    }

    private void init(){
        device_select_0 = (Button)findViewById(R.id.device_type_select_0);
        device_select_1 = (Button)findViewById(R.id.device_type_select_1);
        device_select_2 = (Button)findViewById(R.id.device_type_select_2);
        device_select_3 = (Button)findViewById(R.id.device_type_select_3);
        device_select_0.setOnClickListener(this);
        device_select_1.setOnClickListener(this);
        device_select_2.setOnClickListener(this);
        device_select_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.device_type_select_0:
                Log.d("haha", "0");
                Intent intent0 = new Intent(this, SearchPageActivity.class);
                startActivity(intent0);
                break;

            case R.id.device_type_select_1:
                Log.d("haha", "1");
                Intent intent1 = new Intent(this, SearchPageActivity.class);
                startActivity(intent1);
                break;

            case R.id.device_type_select_2:
                Log.d("haha", "2");
                Intent intent2 = new Intent(this, SearchPageActivity.class);
                startActivity(intent2);
                break;

            case R.id.device_type_select_3:
                Log.d("haha", "3");
                Intent intent3 = new Intent(this, SearchPageActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
