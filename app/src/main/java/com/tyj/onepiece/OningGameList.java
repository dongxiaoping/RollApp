package com.tyj.onepiece;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.view.textclassifier.ConversationActions;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.widget.SimpleAdapter;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OningGameList extends AppCompatActivity {
    private Handler handler;
    public JSONArray noBeginRoomList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oning_game_list);
        handler = new Handler();
        this.doGetNotBeginRoom();
    }

    public void doGetNotBeginRoom() {
        //创建okhttpClient对象
        OkHttpClient okhttpClient = new OkHttpClient();
        //构建Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("https://www.toplaygame.cn/phpserver/public/index.php/race/room/get_not_begin_room_list_by_user_id?userId=2969").build();
        //将Request封装为Call
        Call call = okhttpClient.newCall(request);
        //执行Call
        //异步执行   提供回调接口
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
                                        JSONArray list = demoJson.getJSONArray("data");
                                        OningGameList.this.showList(list);
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
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("roomId", roomId);
            map1.put("memberLimit", memberLimit);
            map1.put("memberCount", memberCount);
            list.add(map1);
        }
        return list;
    }

    public void showList(JSONArray jSONArrayDatalist) throws JSONException {
        OningGameList.this.noBeginRoomList = jSONArrayDatalist;
        List<Map<String, Object>> list = this.transData(jSONArrayDatalist);
        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new SimpleAdapter(this, list, R.layout.activity_oning_game_list_item,
                new String[]{"roomId", "memberCount"}, new int[]{R.id.title, R.id.context}));
        lv.setOnItemClickListener(new OnItemClickListener() {
            //list点击事件
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                try {
                    String roomId =   String.valueOf((Integer)OningGameList.this.noBeginRoomList.getJSONObject(p3).get("id"));
                    Toast.makeText(OningGameList.this, roomId, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
