package com.tyj.onepiece;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tyj.onepiece.Service.SocketService;
import com.tyj.onepiece.componet.ChenXingShare;
import com.tyj.onepiece.componet.Conf;
import com.tyj.onepiece.componet.InterfaceUrl;
import com.tyj.onepiece.model.Player;
import com.tyj.onepiece.model.Room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
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
    private ChenXingShare chenXingShare;
    private Player selectedMember;
    public List<Player> memberList;

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
        this.doGetMemberInfo(this.roomId);

        findViewById(R.id.wait_game_detail_share_button).setOnClickListener(this);
        findViewById(R.id.wait_game_detail_start_button).setOnClickListener(this);
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

    public void doGetMemberInfo(String roomId) {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String url = Conf.serviceAddress + InterfaceUrl.get_members_by_room_id + "?id=" + roomId;
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
                                try {
                                    JSONObject jsonObject = JSONObject.parseObject(result);
                                    Integer flag = Integer.parseInt(jsonObject.getString("status"));
                                    if (flag == 1) {
                                        JSONArray list = (JSONArray) jsonObject.getJSONArray("data");
                                        WaitGameDetailActivity.this.memberList = (List<Player>) JSONArray.parseArray(list.toString(), Player.class);
                                        WaitGameDetailActivity.this.showMemberList();
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

    public void showMemberList() {
        ListView lv = (ListView) findViewById(R.id.wait_game_detail_member_list_view);
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < this.memberList.size(); i++) {
            Player memberItem = (Player) this.memberList.get(i);
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("nick", memberItem.getNick());
            dataList.add(map1);
        }
        lv.setAdapter(new SimpleAdapter(this, dataList, R.layout.play_member_item,
                new String[]{"nick"}, new int[]{R.id.name}));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //list点击事件
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                Player member = WaitGameDetailActivity.this.memberList.get(p3);
                ShowChoise(member);
            }
        });
    }

    private void ShowChoise(Player member) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        this.selectedMember = member;
        builder.setTitle("踢出玩家");
        builder.setMessage("踢出玩家"+member.getNick()+"?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonObjecta = new JSONObject();
                jsonObjecta.put("roomId", WaitGameDetailActivity.this.selectedMember.getRoomId().trim());
                jsonObjecta.put("kickUserId", WaitGameDetailActivity.this.selectedMember.getId());
                JSONObject jsonObjectb = new JSONObject();
                jsonObjectb.put("type", "kickOutMemberFromRoom");
                jsonObjectb.put("info", jsonObjecta);
                final String toInfo = jsonObjectb.toString();

                Intent intent = new Intent(WaitGameDetailActivity.this, SocketService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Key", SocketService.Control.SEND_MESSAGE);
                bundle.putSerializable("Message", toInfo);
                intent.putExtras(bundle);
                startService(intent);
                System.out.println("点了确定"); //TODO 成功怎么回调通知
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("点了取消");
            }
        });
        //一样要show
        builder.show();
    }

    public void doGetRoomInfo(String roomId) {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String url = Conf.serviceAddress + InterfaceUrl.get_room_info_by_id + "?id=" + roomId;
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
                                try {
                                    JSONObject jsonObject = JSONObject.parseObject(result);
                                    Integer flag = Integer.parseInt(jsonObject.getString("status"));
                                    if (flag == 1) {
                                        String roomString = jsonObject.getString("data");
                                        System.out.println(roomString);
                                        WaitGameDetailActivity.this.room = JSON.parseObject(roomString, Room.class);
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
                try {
                    JSONObject jsonObjecta = new JSONObject();
                    jsonObjecta.put("roomId", this.roomId.toString().trim());
                    jsonObjecta.put("userId", "2969");
                    JSONObject jsonObjectb = new JSONObject();
                    jsonObjectb.put("type", "startRoomGame");
                    jsonObjectb.put("info", jsonObjecta);
                    final String toInfo = jsonObjectb.toString();

                    Intent intent = new Intent(this, SocketService.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Key", SocketService.Control.SEND_MESSAGE);
                    bundle.putSerializable("Message", toInfo);
                    intent.putExtras(bundle);
                    startService(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
