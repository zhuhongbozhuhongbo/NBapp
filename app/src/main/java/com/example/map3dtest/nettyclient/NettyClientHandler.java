package com.example.map3dtest.nettyclient;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.map3dtest.dom4j.ChartDatas;
import com.example.map3dtest.dom4j.ChartDatasDom4jUtil;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * Created by 朱宏博 on 2018/4/5.
 */

@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final String TAG = "Netty";
    private INettyClient.OnConnectStatusListener statusListener;
    private List<INettyClient.OnDataReceiveListener> listeners = new ArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        Log.d(TAG, "CHANNEL ACTIVE");
        Log.d("haha", "zzzzgggg");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{

        ByteBuf buf = (ByteBuf) msg;

        String body = buf.toString(CharsetUtil.UTF_8);
//verify(String body)方法对服务器返回的数据进行校验，并取出数据部分。
//具体校验的方法需要与后台同事进行协议。
        //body = verify(body);
        Log.d("haha", "NettyClientHandler channelRead body:" + body);

       ChartDatas chartDatas = new ChartDatasDom4jUtil(body).dataDecode();

        callListeners(Constant.MSG_TYPE, chartDatas);
  /*      if (null != body)
            parseJson(body);*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
//exceptionCaught()事件处理方法是当出现Throwable对象才会被调用，
//即当Netty由于IO错误或者处理器在处理事件时抛出的异常时。
//在大部分情况下，捕获的异常应该被记录下来并且把关联的channel给关闭掉。
        ctx.close();
        Log.e(TAG, "Unexpected exception from downstream : "
                + cause.getMessage());
        if (statusListener != null)//连接异常时触发onDisconnected()
            statusListener.onDisconnected();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
        Log.d(TAG, "channelReadComplete");
    }

    //对数据进行解析，拿出区分不同请求的 flag字段，再根据不同的flag字段去触发相对应的监听器
    /*private void parseJson(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            int msgType = jObject.getInt(Constant.FLAG_MT);
            Log.d(TAG, "parseJson message type: " + msgType + "  json: " + json);
            callListeners(msgType, json);
        } catch (Exception e) {
            Log.e(TAG, "parseJson exception: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

    //遍历监听器List，触发拥有正确msgType 的OnDataReceiveListener，
//回调 void onDataReceive(int mt, String json);方法
    private void callListeners(final int msgType , final ChartDatas body) {
        for (final INettyClient.OnDataReceiveListener listener : listeners)
            if (listener != null)
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {//主线程中进行
                        listener.onDataReceive(msgType, body);
                    }
                });
    }

    //绑定OnDataReceiveListener
    public void addDataReceiveListener(INettyClient.OnDataReceiveListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }
    //绑定OnConnectStatusListener
    public void setConnectStatusListener(INettyClient.OnConnectStatusListener listener) {//INettyClient.OnConnectStatusListener是一个接口
        statusListener = listener;
    }




}
