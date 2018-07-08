package com.example.map3dtest.nettyclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.xml.XmlFrameDecoder;

/**
 * Created by 朱宏博 on 2018/4/5.
 */

public class NettyClient implements INettyClient {
    private final String TAG = NettyClient.class.getSimpleName();
    private static NettyClient mInstance;
    private Bootstrap bootstrap;
    private Channel channel;
    private String host;
    private int port;
    private HandlerThread workThread = null;
    private Handler mWorkHandler = null;
    private NettyClientHandler nettyClientHandler;

    private final String ACTION_SEND_TYPE = "action_send_type";
    private final String ACTION_SEND_MSG = "action_send_msg";
    private final int MESSAGE_INIT = 0x1;
    private final int MESSAGE_CONNECT = 0x2;
    private final int MESSAGE_SEND = 0x3;

    private Handler.Callback mWorkHandlerCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Log.d("haha", "da");

            switch (msg.what) {
                case MESSAGE_INIT: {
                    NioEventLoopGroup group = new NioEventLoopGroup();
                    bootstrap = new Bootstrap();
                    bootstrap.channel(NioSocketChannel.class);
                    bootstrap.group(group);
                    bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                        /*
                        LineBasedFrameDecoder和StringDecoder的原理分析LineBasedFrameDecoder的工作原理是依次便利ByteBuf中的刻度子节，判断
                        看是否有”\n” 或者“\r”，如果有，就以此为止为结束位置，从可读索引到结束位置区间的字节久组成了一行。它是以换行符为
                        结束标志的解码器，支持携带结束符或者不携带结束符两种编码方式，同时支持配置单行的最大长度后仍然没有发现换行符，就会抛
                        出异常，同时忽略掉之前读到的异常码流。StringDecoder的功能非常简单，就是将接收到的对象转换成字符串，然后继续调用后面
                        的handler。LineBasedFrameDecoder+StringDecoder组合就是按行切换的文本解码器，它被设计用来支持TCP的粘包和拆包
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                          //  pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));//不能用这个，用了之后无法进入NettyClientHandler的channelRead
                          //  pipeline.addLast("encoder", new StringDecoder(CharsetUtil.UTF_8));
                            //pipeline.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));//利用LineBasedFrameDecoder解决TCP粘包问题
                            pipeline.addLast(new XmlFrameDecoder(2000000));
                            pipeline.addLast(nettyClientHandler);
                        }
                    });
               //     bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
              //      bootstrap.option(ChannelOption.TCP_NODELAY, true);
                    Log.d("haha", "init");
                    break;
                }
                case MESSAGE_CONNECT: {
                    try {
                        if (TextUtils.isEmpty(host) || port == 0) {
                            Exception exception = new Exception("Netty host | port is invalid");
                            Log.d("haha", "if");

                            throw exception;
                        }
                        Log.d("haha", "host" + host + "port" + port);


                        channel = bootstrap.connect(new InetSocketAddress(host, port)).sync().channel();
                        Log.d("haha", "connect success");

                     /*
                        这种初始化方式导致，只能完成INIT 和 CONNECT， 发送不了，估计是channel.closeFuture().sync()阻塞了，有时间一定好好研究下
                        channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
                        Log.d("haha", 1);
                        channel = channelFuture.sync().channel();
                        Log.d("haha", 2);

                        channelFuture.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                            }
                        });
                        channel.closeFuture().sync();*/
                    } catch (Exception e) {
                        Log.e(TAG, "connect failed  " + e.getMessage() + "  reconnect delay: " + Constant.DELAY_RECONNECT);
                       // ToastUtils.showOnOtherThread("服务器连接失败");
                        e.printStackTrace();
                        Log.d("haha", "se");

                        sendReconnectMessage();
                    }
                    break;
                }
                case MESSAGE_SEND: {//真正执行发送功能的函数

                    String sendMsg = msg.getData().getString(ACTION_SEND_MSG);//sendMsg为要发送的内容
                    int mt = msg.getData().getInt(ACTION_SEND_TYPE);//mt为要发送信息的类型
                    Log.d("haha", "开始发送");
                    try {
                        if (channel != null && channel.isOpen()) {
                            channel.writeAndFlush(constructMessage(sendMsg)).sync();//constructMessage用于在sendMsg的基础上增加校验部分，得到最终发送给服务器的内容
                            Log.d("haha", "send succeed " + constructMessage(sendMsg));
                        } else {
                            throw new Exception("channel is null | closed");
                        }
                    } catch (Exception e) {
                        Log.e("haha", "send failed " + e.getMessage());
                        sendReconnectMessage();//出现异常则进行重连
                        e.printStackTrace();
                    } finally {
                       // if (Constant.MT.HAND_SHAKE.getType() == mt)//若mt显示本次发送数据为心跳包，则再次调用sendMessage并加入心跳延时，这样经过延时后该mWorkerHandler
                        if (Constant.HAND_SHAKE_TYPE == mt){//若mt显示本次发送数据为心跳包，则再次调用sendMessage并加入心跳延时，这样经过延时后该mWorkerHandler
                            //将再次收到消息，发送一个空包给服务器，之后再次执行本finally块中的sendMessage，延时后再向mWorkerHandler发送该message，无限循环，实现了心跳包机制
                            /*此行用于发送心跳包sendMessage(mt, sendMsg, Constant.DELAY_HAND_SHAKE);
                            Log.d("haha", "发送心跳包");*/}
                    }

                    break;
                }
            }
            return true;
        }
    };

    private NettyClient() {
        init();
    }

    public synchronized static NettyClient getInstance() {
        if (mInstance == null)
            mInstance = new NettyClient();
        return mInstance;
    }

    private void init() {
        workThread = new HandlerThread(NettyClient.class.getName());//workThread为真正的工作线程
        workThread.start();
        mWorkHandler = new Handler(workThread.getLooper(), mWorkHandlerCallback);
        nettyClientHandler = new NettyClientHandler();
        nettyClientHandler.setConnectStatusListener(new OnConnectStatusListener() {//初始化断线重连
            @Override
            public void onDisconnected() {//断线重连的代码
                sendReconnectMessage();
            }
        });
        mWorkHandler.sendEmptyMessage(MESSAGE_INIT);
    }

    @Override
    public void connect(String host, int port) {
        this.host = host;
        this.port = port;
        mWorkHandler.sendEmptyMessage(MESSAGE_CONNECT);
    }

    @Override
    public void addDataReceiveListener(OnDataReceiveListener listener) {
        if (nettyClientHandler != null)
            nettyClientHandler.addDataReceiveListener(listener);
    }



    private void sendReconnectMessage() {
        mWorkHandler.sendEmptyMessageDelayed(MESSAGE_CONNECT, Constant.DELAY_RECONNECT);
        Log.d("haha", Constant.DELAY_RECONNECT + "ms后重连");
    }

    @Override
    public void sendMessage(int mt, String msg, long delayed) {//该函数用于向workThread对应的mWorkHandler发送message
        if (TextUtils.isEmpty(msg))
            return;
        Message message = new Message();
        Bundle bundle = new Bundle();
        message.what = MESSAGE_SEND;
        bundle.putString(ACTION_SEND_MSG, msg);
        bundle.putInt(ACTION_SEND_TYPE, mt);
        message.setData(bundle);
        mWorkHandler.sendMessageDelayed(message, delayed);//mWorkHandler为工作线程workThread接收来自NettyClient的消息

    }

    private ByteBuf constructMessage(String json) {
        String message = json;//先不设置校验部分
        //与后台协议好，如何设置校验部分，然后和json一起发给服务器
        ByteBuf buf = Unpooled.buffer(message.length());
        try {
            buf.writeBytes(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buf;
    }



}
