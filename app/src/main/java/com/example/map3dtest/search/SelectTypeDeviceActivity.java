package com.example.map3dtest.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.nbapp.R;


/**
 * Created by 朱宏博 on 2018/4/1.
 */

public class SelectTypeDeviceActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mdevice_type_select_0;
    private Button mdevice_type_select_1;
    private Button mdevice_type_select_2;
    private Button mdevice_type_select_3;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type_select);
        initView();
    }

    private void initView(){
        mdevice_type_select_0 = findViewById(R.id.device_type_select_0);
        mdevice_type_select_1 = findViewById(R.id.device_type_select_1);
        mdevice_type_select_2 = findViewById(R.id.device_type_select_2);
        mdevice_type_select_3 = findViewById(R.id.device_type_select_3);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.device_type_select_0:
                Intent intent0 = new Intent(this, SearchPageActivity.class);
                startActivity(intent0);
                break;
            case R.id.device_type_select_1:
                Intent intent1 = new Intent(this, SearchPageActivity.class);
                startActivity(intent1);
                break;
            case R.id.device_type_select_2:
                Intent intent2 = new Intent(this, SearchPageActivity.class);
                startActivity(intent2);
                break;
            case R.id.device_type_select_3:
                Intent intent3 = new Intent(this, SearchPageActivity.class);
                startActivity(intent3);
                break;

        }
    }
}
