package com.example.map3dtest.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nbapp.R;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.tables.DeviceSheetActivity;
import com.example.map3dtest.transformdata.TransformData;


public class SearchPageActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText msearchpage_edtxt;
    private TextView msearchpage_search;
    private ImageView msearchpage_return;
    private String queryID = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        initView();

        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {
                if(body.getType().equals("PDI")){
                    Log.d("haha", "SearchPageActivity" + "收到： " + body);
                }

            }
        });


        msearchpage_edtxt.addTextChangedListener(new TextWatcher() {
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

        msearchpage_edtxt.setOnKeyListener(new View.OnKeyListener() {
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

    private void initView(){
        msearchpage_edtxt = findViewById(R.id.searchpage_edtxt);
        msearchpage_search = findViewById(R.id.searchpage_search);
        msearchpage_return = (ImageView)findViewById(R.id.searchpage_return);
        msearchpage_search.setOnClickListener(this);
        msearchpage_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.searchpage_search:
                String modelNumber = queryID;
                TransformData transformDataTrd = new TransformData(queryID, "豫C 83576", "");

                Log.d("haha", modelNumber);
                Intent intent = new Intent(this, DeviceSheetActivity.class);
                /*Bundle data = new Bundle();
                data.putParcelable("positionTrd", transformDataTrd);*/
                intent.putExtra("stu", new TransformData(queryID, "", ""));
                startActivity(intent);
                break;
            case R.id.searchpage_return:
                finish();
                Log.d("haha", "退出");//仍然能在控制台打印出这句
                break;

        }

    }
}
