package com.tyj.onepiece.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.tyj.onepiece.componet.JWebSocketClient;

import org.java_websocket.enums.ReadyState;

import java.net.URI;
/*
 * websocket 服务
 * */
public class SocketService extends Service {

    /**
     * 标识服务如果被杀死之后的行为
     */
    int mStartMode;

    /**
     * 绑定的客户端接口
     */
    IBinder mBinder;

    /**
     * 标识是否可以使用onRebind
     */
    boolean mAllowRebind;
    private int startId;

    public JWebSocketClient socketClient;

    public enum Control {
        SEND_MESSAGE,SEND_SOCKET_STATUS, RECONNECT_SOCKET, PAUSE, STOP
    }

    /**
     * 当服务被创建时调用.
     */
    @Override
    public void onCreate() {
        URI uri = URI.create("wss://www.toplaygame.cn/wss");
        SocketService.this.socketClient = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) { //message就是接收到的消息
                Intent intent1 = new Intent();
                intent1.setAction("socket.getMessage");
                intent1.putExtra("msg", message);
                sendBroadcast(intent1);
                Log.i("JWebSClientService", message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("JWebSocketClient", "onClose()");
            }
        };
        try {
            this.socketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    /**
     * 调用startService()启动服务时回调
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "服务已经启动", Toast.LENGTH_LONG).show();
        this.startId = startId;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Control control = (Control) bundle.getSerializable("Key");
            if (control != null) {
                switch (control) {
                    case SEND_MESSAGE:
                        if (!SocketService.this.socketClient.getReadyState().equals(ReadyState.OPEN)) {
                            Log.i("JWebSocketClient", "socket未连接成功，无法发送信息！");
                            this.noticeSocketStatus();
                            break;
                        }
                        String toInfo = (String) bundle.getSerializable("Message");
                        SocketService.this.socketClient.send(toInfo);
                        break;
                    case SEND_SOCKET_STATUS://将socket的连接状态以通知的形式发出去
                        this.noticeSocketStatus();
                        break;
                    case RECONNECT_SOCKET://socket 重新连接
                    SocketService.this.socketClient.reconnect();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void noticeSocketStatus(){
        Intent intent1 = new Intent();
        intent1.setAction("socket.is_connected");
        String isConnected = "0";
        if (SocketService.this.socketClient.getReadyState().equals(ReadyState.OPEN)) {
            isConnected = "1";
        }
        intent1.putExtra("msg", isConnected);
        sendBroadcast(intent1);
    }

    /**
     * 通过bindService()绑定到服务的客户端
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 通过unbindService()解除所有客户端绑定时调用
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * 通过bindService()将客户端绑定到服务时调用
     */
    @Override
    public void onRebind(Intent intent) {

    }

    /**
     * 服务不再有用且将要被销毁时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "服务已经停止", Toast.LENGTH_LONG).show();
        Log.i("JWebSClientService", "socket服务停止了");
    }
}
