package com.tyj.onepiece;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.tyj.onepiece.componet.ChenXingShare;
import com.tyj.onepiece.componet.Conf;
import com.tyj.onepiece.componet.JWebSocketClient;
import com.tyj.onepiece.componet.InterfaceUrl;
import com.tyj.onepiece.model.Room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WaitGameDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public String roomId;
    private Handler handler;
    public Room room;
    public JWebSocketClient socketClient;
    private ChenXingShare chenXingShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_game_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handler = new Handler();

        chenXingShare = new ChenXingShare();
        chenXingShare.initChenXingShare(WaitGameDetailActivity.this);

        Intent intent = getIntent();
        this.roomId = intent.getStringExtra("roomId");
        this.doGetRoomInfo(this.roomId);

        findViewById(R.id.wait_game_detail_share_button).setOnClickListener(this);
        findViewById(R.id.wait_game_detail_start_button).setOnClickListener(this);

        URI uri = URI.create("wss://www.toplaygame.cn/wss");
        this.socketClient = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                Log.e("JWebSClientService", message);
            }
        };
        try {
            this.socketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ///////////////////
        ListView lv = (ListView) findViewById(R.id.wait_game_detail_member_list_view);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "我是风ddddddddddddddddddddddddddddddddddddddd");
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        lv.setAdapter(new SimpleAdapter(this, list, R.layout.play_member_item,
                new String[]{"name"}, new int[]{ R.id.name}));

        ///////////////////
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //返回按钮
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doGetRoomInfo(String roomId) {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String url =  Conf.serviceAddress+ InterfaceUrl.get_room_info_by_id+"?id=" + roomId;
        Request request = builder.get().url(url).build();
        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                new Thread() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject demoJson = null;
                                try {
                                    demoJson = new JSONObject(result);
                                    Integer flag = Integer.parseInt(demoJson.getString("status"));
                                    if (flag == 1) {
                                        String roomString = demoJson.getString("data");
                                        System.out.println(roomString);
                                        Gson gson = new Gson();
                                        WaitGameDetailActivity.this.room = gson.fromJson(roomString, Room.class);
                                    } else {
                                        System.out.println("获取数据失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }.start();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wait_game_detail_share_button:
                chenXingShare.actionShareButtonClick(this.room.getId(), String.valueOf(this.room.getMemberLimit()),
                        String.valueOf(this.room.getPlayCount()), String.valueOf(this.room.getCostLimit()),
                        this.room.getRoomPay());
                break;
            case R.id.wait_game_detail_start_button:
                JSONObject jsonObjecta = new JSONObject();
                try {
                    jsonObjecta.put("roomId", this.roomId.toString().trim());
                    jsonObjecta.put("userId", "2969");
                    JSONObject jsonObjectb = new JSONObject();
                    jsonObjectb.put("type", "startRoomGame");
                    jsonObjectb.put("info", jsonObjecta);
                    final String toInfo = jsonObjectb.toString();
                    this.socketClient.send(toInfo);//发送开始游戏通知
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
