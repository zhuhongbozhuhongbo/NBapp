package com.example.map3dtest.nettyclient;


import com.example.map3dtest.dom4j.ChartDatas;

/**
 * Created by 朱宏博 on 2018/4/5.
 */

public interface INettyClient {
    void connect(String host, int port);
    void sendMessage(int mt, String msg, long delayed);
    void addDataReceiveListener(OnDataReceiveListener listener);

    interface OnDataReceiveListener{
        void onDataReceive(int mt, ChartDatas json);
    }

    interface OnConnectStatusListener{
        void onDisconnected();
    }
}
