package com.tyj.onepiece;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.tyj.onepiece.componet.ChenXingShare;
import com.tyj.onepiece.model.Room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WaitGameDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public String roomId;
    private Handler handler;
    public Room room;
    private ChenXingShare chenXingShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_game_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new Handler();

        chenXingShare = new ChenXingShare();
        chenXingShare.initChenXingShare(WaitGameDetailActivity.this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        this.roomId = intent.getStringExtra("roomId");
        this.doGetRoomInfo(this.roomId);

        findViewById(R.id.wait_game_detail_share_button).setOnClickListener(this);
    }

    public void doGetRoomInfo(String roomId) {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("https://www.toplaygame.cn/phpserver/public/index.php/race/room/get_room_info_by_id?id=186").build();
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
                chenXingShare.actionShareButtonClick( this.room.getId(),String.valueOf(this.room.getMemberLimit()),
                        String.valueOf(this.room.getPlayCount()), String.valueOf(this.room.getCostLimit()),
                        this.room.getRoomPay());
                break;
        }
    }
}
