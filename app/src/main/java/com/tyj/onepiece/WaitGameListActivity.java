package com.tyj.onepiece;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tyj.onepiece.componet.Conf;
import com.tyj.onepiece.componet.GeneralAdapter;
import com.tyj.onepiece.componet.InterfaceUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 *待开始房间列表
 * */
public class WaitGameListActivity extends AppCompatActivity {
    private Handler handler;
    public RefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_game_list);
        Toolbar toolbar = findViewById(R.id.toolbarcc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WaitGameListActivity.this.refreshLayout = (RefreshLayout)findViewById(R.id.waitRefreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                WaitGameListActivity.this.doGetOnRoom();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 1:         //刷新加载
                        List<Map<String, Object>> mList  = ( List<Map<String, Object>>) msg.obj;
                        RecyclerView recyclerView = findViewById(R.id.lv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(WaitGameListActivity.this));
                        recyclerView.setAdapter(new GeneralAdapter(WaitGameListActivity.this,mList));
                        WaitGameListActivity.this.refreshLayout.finishRefresh(true);
                        break;
                    case 2:         //加载更多

                        break;
                }
                return false;
            }
        });
        this.doGetOnRoom();
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

    public void doGetOnRoom() {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String url = Conf.serviceAddress + InterfaceUrl.get_on_room_list_by_user_id + "?userId=2969";
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
                                        JSONArray JSONArrayList = demoJson.getJSONArray("data");
                                        List<Map<String, Object>> list = WaitGameListActivity.this.transData(JSONArrayList);
                                        Message message = new Message();
                                        message.what = 1 ;
                                        message.obj = list ;
                                        handler.sendMessageDelayed(message,2000);
                                    } else {
                                        System.out.println("房间创建失败");
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

    public List<Map<String, Object>> transData(JSONArray datalist) throws JSONException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < datalist.length(); i++) {
            JSONObject dataobj = datalist.getJSONObject(i);
            String roomId = dataobj.getString("id");
            String memberLimit = dataobj.getString("memberLimit");
            String memberCount = dataobj.getString("memberCount");
            String[] creatTime = dataobj.getString("creatTime").split(" ");
            int roomState = Integer.parseInt(dataobj.getString("roomState"));
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("roomId", roomId);
            map1.put("memberLimit", memberLimit);
            map1.put("memberCount", memberCount);
            map1.put("creatTime", creatTime[1]);
            if (roomState == 1) {
                map1.put("roomStateDesc", "待开始：");
            } else {
                map1.put("roomStateDesc", "进行中：");
            }
            list.add(map1);
        }
        return list;
    }
}
