package com.example.map3dtest.Utils;

/**
 * Created by 朱宏博 on 2018/4/2.
 */

import com.amap.api.maps.model.LatLng;

/**
 * P代表工程物联网下位机，D代表设备物联网下位机，两种类型下位机ID分别独立从0开始递增
 */
public class Matching {
    public static String match(String title){
        switch (title){
            case "太湖0":
                return "P";
            case "太湖1":
                return "P";
            case "太湖2":
                return "P";
            case "太湖3":
                return "P";
            case "太湖4":
                return "P";
            case "太湖5":
                return "P";
            case "太湖6":
                return "P";
            case "太湖7":
                return "D";
            case "太湖8":
                return "D";
            case "太湖9":
                return "D";
            case "太湖10":
                return "D";
            case "太湖11":
                return "D";
            case "太湖12":
                return "D";
            case "太湖13":
                return "D";
            case "太湖14":
                return "D";
            case "太湖15":
                return "D";
            case "灰罐车":
                return "D";
            default:
                return "EMPTY";
        }
    }

    public static int convert(String title){
        switch(title){
            case "太湖0":
                return 0;
            case "太湖1":
                return 1;
            case "太湖2":
                return 2;
            case "太湖3":
                return 3;
            case "太湖4":
                return 4;
            case "太湖5":
                return 5;
            case "太湖6":
                return 6;
            case "太湖7":
                return 7;
            case "太湖8":
                return 8;
            case "太湖9":
                return 9;
            case "太湖10":
                return 10;
            case "太湖11":
                return 11;
            case "太湖12":
                return 12;
            case "太湖13":
                return 13;
            case "太湖14":
                return 14;
            case "太湖15":
                return 15;
            default:
                return 999;
        }
    }

    public static LatLng latLngConvert(String province) {
      /*  if(province.equals("江苏无锡市")){
            return new LatLng(31.574729,120.301663);
        }*/

        switch (province) {
            case "北京":
                return new LatLng(39.55, 116.24);
            
            case "上海":
                return new LatLng(31.14, 121.29);
            
            case "天津":
                return new LatLng(39.02, 117.12);
            
            case "重庆":
                return new LatLng(29.56667,106.45000);
            
            case "河北":
                return new LatLng(38.02, 114.30);
            
            case "山西":
                return new LatLng(37.54, 112.33);
            
            case "河南":
                return new LatLng(34.46, 113.40);
            
            case "辽宁":
                return new LatLng(41.48, 123.25);
            
            case "吉林":
                return new LatLng(43.54, 125.19);
             
            case "黑龙江":
                return new LatLng(45.44, 126.36);
             
            case "内蒙古":
                return new LatLng(40.48, 111.41);

            case "江苏":
                return new LatLng(32.03, 118.46);

            case "山东":
                return new LatLng(36.40, 117.00);

            case "安徽":
                return new LatLng(31.52, 117.17);

            case "浙江":
                return new LatLng(30.16, 120.10);

            case "福建":
                return new LatLng(26.05, 119.18);

            case "湖南":
                return new LatLng(28.12, 112.59);

            case "湖北":
                return new LatLng(30.35, 114.17);

            case "广东":
                return new LatLng(23.08, 113.14);

            case "广西":
                return new LatLng(22.48, 108.19);

            case "江西":
                return new LatLng(28.40, 115.55);

            case "四川":
                return new LatLng(30.40, 104.04);

            case "海南":
                return new LatLng(20.02, 110.20);

            case "贵州":
                return new LatLng(26.35, 106.42);

            case "云南":
                return new LatLng(25.04, 102.42);

            case "西藏":
                return new LatLng(29.39, 91.08);

            case "陕西":
                return new LatLng(34.17, 108.57);

            case "甘肃":
                return new LatLng(36.04, 103.51);

            case "青海":
                return new LatLng(36.38, 101.48);

            case "宁夏":
                return new LatLng(38.27, 106.16);

            case "新疆":
                return new LatLng(43.45, 87.36);

            case "台湾":
                return new LatLng(25.03, 121.30);

            case "香港":
                return new LatLng(114.10000, 22.20000);

            case "澳门":
                return new LatLng(21.33, 115.07);

            case "钓鱼岛":
                return new LatLng(25.742234, 123.475139);


        }
        return new LatLng(39.55, 116.24);

    }



}
