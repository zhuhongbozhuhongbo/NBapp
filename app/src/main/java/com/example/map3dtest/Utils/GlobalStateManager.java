package com.example.map3dtest.Utils;

import com.amap.api.maps.model.LatLng;

/**
 * Created by 朱宏博 on 2018/4/11.
 */

public class GlobalStateManager {
    public static boolean projectdevice = true;//true代表project  false代表device
    public static boolean netstate = false;//true代表成功连接到服务器 false代表无法连接到服务器
    public static LatLng currentLatlng = new LatLng(39.55, 116.24);
    public static String currentID = "";


}
