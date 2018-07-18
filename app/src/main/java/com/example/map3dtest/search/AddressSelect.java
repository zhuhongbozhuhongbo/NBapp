package com.example.map3dtest.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.map3dtest.Utils.GlobalStateManager;
import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.main.MapDeviceActivity;
import com.example.map3dtest.main.MapProjectActivity;
import com.example.map3dtest.nettyclient.Constant;
import com.example.map3dtest.nettyclient.INettyClient;
import com.example.map3dtest.nettyclient.NettyClient;
import com.example.map3dtest.tables.DeviceSheetActivity;
import com.example.map3dtest.tables.ProjectSheetActivity;
import com.example.map3dtest.transformdata.TransformData;
import com.example.nbapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class AddressSelect extends Activity {


    String[] IdString = {};
    String[] DvString = {};
    String[] PrjString = {};
    String[] PlateString = {};
    private int spinnerPosition;

    String province, city;//最终选择结果放在这两个变量******************************************
   //因为spinner在加载视图的时候会自动调用点击响应事件，这两个变量在那个时候就已经初始化了

    HashMap<String, String> provinceHash = new HashMap<>();
    String[] provinceString = new String[34];

    HashMap<String, String> cityHash = new HashMap<>();
    String[] cityString;

    String file;

    String cityNo = null;// 最重要的参数，选中的城市的cityNo

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    Spinner provinceSpinner;
    Spinner citySpinner;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_address_layout);

        provinceSpinner = findViewById(R.id.spinnerprovince);
        citySpinner = findViewById(R.id.spinnercity);

        file = readFile(); // 读取txt文件
        getProvinces(file); // 得到省的列表

        // 设置spinner，不用管什么作用
        provinceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, provinceString);
        provinceAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        provinceSpinner.setAdapter(provinceAdapter); // 将adapter 添加到spinner中
        provinceSpinner.setOnItemSelectedListener(new ProvinceSelectedListener(AddressSelect.this));// 添加监听
        provinceSpinner.setVisibility(View.VISIBLE);// 设置默认值


        // 初始化控件
        spinner = (Spinner) findViewById(R.id.spinnerDevice);


        NettyClient.getInstance().addDataReceiveListener(new INettyClient.OnDataReceiveListener() {
                                                             @Override
                                                             public void onDataReceive(int mt, ChartDatas body) {

                                                                 //第一步，拿到所有设备的信息，
                                                                 //注:若为 #1#,解析出的 data.length = 2
                                                                 if (body.getType().equals("DSI") && body.getField().equals("all") && body.getQuerymode().equals("findAll")) {
                                                                     Log.d("haha", "receive all the dsi datas");
                                                                     IdString = body.getP2().trim().split("#");
                                                                     DvString = body.getP9().trim().split("#");
                                                                     PrjString = body.getP4().trim().split("#");
                                                                     PlateString = body.getP5().trim().split("#");

                                                                     String[] mItems = new String[IdString.length - 1];
                                                                     // 建立spinner数据源
                                                                     for (int i = 1; i < IdString.length; i++) {//解析后数组中IdString[0]为空的
                                                                         Log.d("haha", "fasong: " + IdString[i] + "  " + DvString[i]);
                                                                         mItems[i - 1] = PlateString[i];
                                                                     }

                                                                     // 建立Adapter并且绑定数据源
                                                                     ArrayAdapter<String> adapter=new ArrayAdapter<String>(AddressSelect.this,android.R.layout.simple_spinner_item, mItems);
                                                                     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                     //绑定 Adapter到控件
                                                                     spinner .setAdapter(adapter);
                                                                 }
                                                             }
                                                         });





        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                //CustomToast.makeText(AddressSelect.this, "你点击的是:"+pos, CustomToast.LENGTH_SHORT).show();
                spinnerPosition = pos;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        NettyClient.getInstance().sendMessage(Constant.MSG_TYPE, "<query><userid>001</userid><passwd>aaa</passwd><field>all</field>" +
                "<type>DSI</type><querymode>findAll</querymode><p0>empty</p0><p1>empty</p1><p2>empty</p2><p3>empty</p3>" +
                "<p4>empty</p4><p5>empty</p5></query>", 0);

    }


  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //创建
            if(GlobalStateManager.projectdevice) {
                Intent intent = new Intent(AddressSelect.this, MapProjectActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(AddressSelect.this, MapDeviceActivity.class);
                startActivity(intent);
            }
            return false;
        }
        return false;
    }


    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Log.d("haha", "AddressSelect返回键按下");
    }*/


    public void click(View v) {
        Intent intent = new Intent();
        Bundle data = new Bundle();

        if(v.getId() == R.id.project_find_button) {
            //CustomToast.makeText(this, province + "  " + city, CustomToast.LENGTH_SHORT).show();
            intent.setClass(AddressSelect.this, ProjectSheetActivity.class);
            TransformData trans = new TransformData(province);
            trans.setProvinceOrName(true);
            data.putParcelable("prjName", trans);
            intent.putExtras(data);//跳转到项目的设备列表页面
            startActivity(intent);
        }else if(v.getId() == R.id.device_find_button){
            TransformData transforSec = new TransformData(IdString[spinnerPosition + 1],
                    PlateString[spinnerPosition + 1], PrjString[spinnerPosition + 1]);
            intent.setClass(AddressSelect.this, DeviceSheetActivity.class);
            intent.putExtra("deviceDetail", transforSec);
            intent.putExtras(data);//跳转到项目的设备列表页面
            startActivity(intent);
        }
    }

    public String readFile() {

        /*
         * 读取文件中数据的方法
         */

        InputStream myFile = null;
        myFile = getResources().openRawResource(R.raw.ub_city);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(myFile, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("haha", e.toString());
        }
        String temp;
        String str = "";
        try {
            while ((temp = br.readLine()) != null) {
                str = str + temp;
                // Log.i("zhiyinqing", "断点3"+temp);
            }
            br.close();
            myFile.close();
            return str;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

    public void getProvinces(String jsonData) {

        /*
         * 从json字符串中得到省的信息
         */

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < 34; i++) {
                // 获取了34个省市区信息
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String guid = jsonObject.getString("guid");
                String cityName = jsonObject.getString("cityName");
                // Log.i("zhiyinqing", i+guid+cityName);
                provinceHash.put(cityName, guid);
                provinceString[i] = cityName;

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String[] getCitys(String guid, String jsonData) {
        /*
         * 此方法用于查找一个省下的所有城市
         */
        // 初始化hashmap
        cityHash.clear();
        // 暂时存放城市的数组
        String[] cityBuffer = new String[21];
        int j = 0;

        // 解析数据
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(jsonData);
            int length = jsonArray.length();
            int i = 33;
            int continuous = 0;// 这个变量用于判断是否连续几次没有找到，如果是，则该省信息全部找到了
            boolean isFind = false;

            while (i < length) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String fGuid = jsonObject.getString("fGuid");
                String cityName = jsonObject.getString("cityName");
                String cityNo = jsonObject.getString("cityNo");
                if (fGuid.equals(guid)) {
                    isFind = true;
                    cityHash.put(cityName, cityNo);
                    cityBuffer[j] = cityName;
                    j++;
                    // Log.i("zhiyinqing", cityName);
                } else {
                    if (isFind == true) {
                        continuous += 1;

                        if (continuous > 5) {
                            Log.i("zhiyinqing", "城市数:" + j);
                            break;
                        }
                    }
                }
                i++;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] citys = new String[j];
        for (int i = 0; i < j; i++) {
            citys[i] = cityBuffer[i];
        }
        return citys;
    }

    class ProvinceSelectedListener implements AdapterView.OnItemSelectedListener {

        Context context;

        // 省被选择的监听器

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {

            String provinceName = provinceString[arg2];
            province = provinceName;
            String guid = provinceHash.get(provinceName);
            cityString = getCitys(guid, file);

            // 省被选中后，初始化城市Spinner
            cityAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, cityString);
            cityAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
            citySpinner.setAdapter(cityAdapter); // 将adapter 添加到spinner中
            citySpinner.setOnItemSelectedListener(new CitySelectedListener());// 添加监听
            citySpinner.setVisibility(View.VISIBLE);// 设置默认

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

        public ProvinceSelectedListener(Context context) {
            this.context = context;
        }

    }

    class CitySelectedListener implements AdapterView.OnItemSelectedListener {

        // 城市被点击的监听事件

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            String cityName = cityString[arg2];
            city = cityName;
            if (cityName.equals("") || cityName == null) {
                cityName = cityString[0];
                cityNo = cityHash.get(cityName);

            } else {
                cityNo = cityHash.get(cityName);
                Log.i("haha", "cityNo" + cityNo);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }
}
