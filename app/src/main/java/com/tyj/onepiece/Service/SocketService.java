package com.tyj.onepiece.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tyj.onepiece.componet.JWebSocketClient;

import java.net.URI;
import java.util.ResourceBundle;

public class SocketService extends Service {

    /** 标识服务如果被杀死之后的行为 */
    int mStartMode;

    /** 绑定的客户端接口 */
    IBinder mBinder;

    /** 标识是否可以使用onRebind */
    boolean mAllowRebind;
    private int startId;

    public JWebSocketClient socketClient;

    public enum Control {
        SEND_MESSAGE, PAUSE, STOP
    }

    /** 当服务被创建时调用. */
    @Override
    public void onCreate() {
        URI uri = URI.create("wss://www.toplaygame.cn/wss");
        SocketService.this.socketClient = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                Log.i("JWebSClientService", message);
            }
        };
        try {
            this.socketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    /** 调用startService()启动服务时回调 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "服务已经启动", Toast.LENGTH_LONG).show();
        this.startId = startId;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Control control = ( Control) bundle.getSerializable("Key");
            if (control != null) {
                switch (control) {
                    case SEND_MESSAGE:
                        String toInfo = (String) bundle.getSerializable("Message");

                        SocketService.this.socketClient.send(toInfo);
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /** 通过bindService()绑定到服务的客户端 */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** 通过unbindService()解除所有客户端绑定时调用 */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** 通过bindService()将客户端绑定到服务时调用*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** 服务不再有用且将要被销毁时调用 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "服务已经停止", Toast.LENGTH_LONG).show();
    }
}
