package com.tyj.onepiece;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
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
    BroadcastMain receiver; //广播通知接收器
   //接受广播通知
    public class BroadcastMain extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String message =  intent.getStringExtra("msg");
            if(action == "socket.getMessage"){
                JSONObject jsonObject = JSONObject.parseObject(message);
                String flag = jsonObject.getString("type");
                if(flag.equals("startGameResultNotice")){
                    String resultFlag = jsonObject.getString("info");
                    if(resultFlag.equals("0")){
                        Toast.makeText(WaitGameDetailActivity.this, "游戏启动失败", Toast.LENGTH_LONG).show();
                    }else{
                      //  Toast.makeText(WaitGameDetailActivity.this, "游戏启动成功", Toast.LENGTH_LONG).show();
                        WaitGameDetailActivity.this.finish();
                    }
                }else if(flag.equals("kickOutMemberFromRoomResult")){//
                    WaitGameDetailActivity.this.doGetMemberInfo(WaitGameDetailActivity.this.roomId);
                }
                System.out.println(message);
            }else if (action == "socket.is_connected"){
                Log.i("JWebSClientService","socket连接状态:"+message);
                if(message.equals("0")){
                    WaitGameDetailActivity.this.showToConnectSocketDial();
                }else{
                 //   Toast.makeText(WaitGameDetailActivity.this, "socket已连接", Toast.LENGTH_LONG).show();
                }
            }
            System.out.println(message);
           // Toast.makeText(WaitGameDetailActivity.this, "接到了通知", Toast.LENGTH_LONG).show();
        }
    }

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

        ///
        //新添代码，在代码中注册广播接收程序
        receiver = new BroadcastMain();
        IntentFilter filter = new IntentFilter();
        filter.addAction("socket.getMessage");
        filter.addAction("socket.is_connected");
        registerReceiver(receiver, filter);
        ///

        this.noticeGetSocketStatus();
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
            if(memberItem.getState() != 3){ //被踢出
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("nick", memberItem.getNick());
                dataList.add(map1);
            }
        }
        lv.setAdapter(new SimpleAdapter(this, dataList, R.layout.play_member_item,
                new String[]{"nick"}, new int[]{R.id.name}));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //list点击事件
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                Player member = WaitGameDetailActivity.this.memberList.get(p3);
                outPlayerDial(member);
            }
        });
    }

    private void outPlayerDial(Player member) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        this.selectedMember = member;
        builder.setTitle("踢出玩家");
        builder.setMessage("踢出玩家"+member.getNick()+"?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonObjecta = new JSONObject();
                jsonObjecta.put("roomId", WaitGameDetailActivity.this.selectedMember.getRoomId().trim());
                jsonObjecta.put("kickUserId", WaitGameDetailActivity.this.selectedMember.getUserId());
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

    //通过通知的形式通知获取socket状态
    public  void  noticeGetSocketStatus(){
        Intent intent = new Intent(WaitGameDetailActivity.this, SocketService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Key", SocketService.Control.SEND_SOCKET_STATUS);
        bundle.putSerializable("Message", "");
        intent.putExtras(bundle);
        startService(intent);
    }

    //显示连接socket对话框
    public  void  showToConnectSocketDial(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("服务断开，请重新连接");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(WaitGameDetailActivity.this, SocketService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Key", SocketService.Control.RECONNECT_SOCKET);
                bundle.putSerializable("Message", "");
                intent.putExtras(bundle);
                startService(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
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
                this.startPlayDial();
                break;
        }
    }

    //开始游戏
    private void startPlay() {
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
         //   WaitGameDetailActivity.this.finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startPlayDial() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("开始游戏");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WaitGameDetailActivity.this.startPlay();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
