package com.example.map3dtest.functest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.nbapp.R;

public class FuncActivityTrd extends AppCompatActivity implements View.OnClickListener{
    private TextView mtextView;
    private Button mbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_trd);

        mtextView = (TextView)findViewById(R.id.func_tv_trd);
        mbutton = (Button)findViewById(R.id.func_btn_trd);

        mbutton.setOnClickListener(this);

        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
            @Override
            public void onDataReceive(int mt, ChartDatas body) {

                String result = "querymode:" + body.getQuerymode() + " type:" + body.getType() + " field:" +
                        body.getField()  + " p2:" + body.getP2() + " " + " p3:" + body.getP3() + " "
                        + " p4:" + body.getP4() + " " + " p5:" + body.getP5() + " " + " p6:" + body.getP6() + " " + " p7:" + body.getP7() + " " + " p8:" + body.getP8() + " "
                        + " p9:" + body.getP9() + " " + " p10:" + body.getP10() + " p0:" + body.getP0() + " " + " p1:" + body.getP1();
                Log.d("haha", "FuncActivity收到:" + result);
                if(body.getType().equals("DDI")){

                }else if(body.getType().equals("DSI")){
                    mtextView.setText(result);
                }else if(body.getType().equals("SI")){
                    mtextView.setText("date:\n" + body.getP2() + "\nfuelCost:\n" + body.getP3());
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

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.func_btn_trd){
  /*          NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field>" +
                    "<type>PDI</type><querymode>findAll</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3>" +
                    "<p4>empty</p4><p5>empty</p5></query>", 0);*/

            NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field>" +
                    "<type>PDI</type><querymode>findAll</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>null</p3>" +
                    "<p4>null</p4><p5>null</p5></query>", 0);
            Log.d("haha", "functesttrd");
        }

    }
}
