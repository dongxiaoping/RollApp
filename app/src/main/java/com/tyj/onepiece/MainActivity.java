package com.tyj.onepiece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.tyj.onepiece.Service.SocketService;
import com.tyj.onepiece.componet.BoxImageAdapter;
import com.tyj.onepiece.componet.Conf;
import com.tyj.onepiece.componet.InterfaceUrl;
import com.tyj.onepiece.model.AgencyConfig;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //九宫格图片
    private int[] mThumbIds = {
            R.drawable.begin_game_icon, R.drawable.create_room_icon, R.drawable.my_home
    };
    //九宫格文字
    private String text[] = {"进入游戏", "创建房间", "运行中房间"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startService(new Intent(getBaseContext(), SocketService.class)); //启动一个服务，服务里面开启websocket
//        //////////
/*        Intent intent = new Intent();
        intent.putExtra("roomId", "756");
        intent.setClass(this, WaitGameDetailActivity.class);
       this.startActivity(intent);*/
//        //////////
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new BoxImageAdapter(this, this.text, this.mThumbIds));
        this.doGetConfig();
        //单击GridView元素的响应
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        String adddr =   MeterApplication.getInstance().getAgencyConfig().getGameUrl();
                        intent.setData(Uri.parse(adddr));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        MainActivity.this.startActivity(intent); //启动浏览器
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, CreatRoomActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, WaitGameListActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    default:
                }
            }
        });
    }

    public void doGetConfig() {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        String url = Conf.serviceAddress + InterfaceUrl.get_game_agency_config;
        Request request = builder.get().url(url).build();
        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                System.out.println(result);
                try {
                    JSONObject JsonOb = new JSONObject(result);
                    String obString = JsonOb.getString("data");
                    AgencyConfig ob = JSON.parseObject(obString, AgencyConfig.class);
                    MeterApplication.getInstance().setAgencyConfig(ob);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}