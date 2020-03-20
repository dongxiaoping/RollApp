package com.tyj.onepiece;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class CreatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_room);
        findViewById(R.id.creat_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.creat_finish:
                this.actionCreatButtonClick();
                break;
        }
    }

    public void actionCreatButtonClick() {
        final int memberCount = this.getMemberCount();
        final int raceCount = this.getRaceCount();
        final int diamondType = this.getDiamondType();
        final int costLimit = this.getCostLimit();
        System.out.println(String.valueOf(memberCount));
        System.out.println(String.valueOf(raceCount));
        System.out.println(String.valueOf(diamondType));
        System.out.println(String.valueOf(costLimit));
        new Thread(new Runnable() {
            @Override
            public void run() {
                this.requestCreateRoom(String.valueOf(memberCount),String.valueOf(raceCount),String.valueOf(costLimit),String.valueOf(diamondType));
            }

            private void requestCreateRoom(String memberCount, String raceCount, String costLimit, String roomPay) {
                String uriAPI = "https://www.toplaygame.cn/phpserver/public/index.php/race/room/create_room?creatUserId=2969&memberLimit="+memberCount+"&playCount="+raceCount+"&roomPay="+roomPay+"&costLimit="+costLimit;
                HttpGet httpRequest = new HttpGet(uriAPI);
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    HttpResponse response = httpClient.execute(httpRequest);
                    showResponseResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponseResult(HttpResponse response) {
        if (null == response) {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;

            }
            System.out.println(result);
            JSONObject demoJson = new JSONObject(result);
            Integer flag = Integer.parseInt(demoJson.getString("status"));
            if(flag == 1){
                String roomInfo = demoJson.getString("data");
                JSONObject roomInfoOb = new JSONObject(roomInfo);
                String roomId = roomInfoOb.getString("id");
                String memberLimit = roomInfoOb.getString("memberLimit");
                String playCount = roomInfoOb.getString("playCount");
                String roomPay = roomInfoOb.getString("playCount");
                String costLimit = roomInfoOb.getString("costLimit");
                System.out.println(roomId);
              //  Toast.makeText(this, "房间创建成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("roomId", roomId);
                intent.putExtra("memberLimit", memberLimit);
                intent.putExtra("playCount", playCount);
                intent.putExtra("costLimit", costLimit);
                intent.putExtra("roomPay", roomPay);
                intent.setClass(this, RoomSuccessActivity.class);
                this.startActivity(intent);
            }else{
                System.out.println("房间创建失败");
               // Toast.makeText(this, "房间创建失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void requestCreateRoom() {
        /*URL可以随意改*/
        String uriAPI = "https://www.toplaygame.cn/phpserver/public/index.php/race/room/create_room?creatUserId=2969&memberLimit=6&playCount=10&roomPay=2&costLimit=10";
        /*建立HTTP Get对象*/
        HttpGet httpRequest = new HttpGet(uriAPI);
        try {
            /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            /*若状态码为200 ok*/
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /*读*/
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                /*去没有用的字符*/
            } else {

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //房间人数设置
    public int getMemberCount() {
        int val = 0;
        RadioGroup RadioGroupRadioMemberCount = (RadioGroup) findViewById(R.id.radioMemberCount);
        for (int i = 0; i < RadioGroupRadioMemberCount.getChildCount(); i++) {
            RadioButton rb = (RadioButton) RadioGroupRadioMemberCount.getChildAt(i);
            if (rb.isChecked()) {
                if (i == 0) {
                    val = 6;
                } else if (i == 1) {
                    val = 8;
                } else {
                    val = 10;
                }
                break;
            }
        }
        return val;
    }

    //比赛场次
    public int getRaceCount() {
        int val = 0;
        RadioGroup RadioGroupRadioMemberCount = (RadioGroup) findViewById(R.id.radioRaceCount);
        for (int i = 0; i < RadioGroupRadioMemberCount.getChildCount(); i++) {
            RadioButton rb = (RadioButton) RadioGroupRadioMemberCount.getChildAt(i);
            if (rb.isChecked()) {
                if (i == 0) {
                    val = 10;
                } else if (i == 1) {
                    val = 20;
                } else {
                    val = 25;
                }
                break;
            }
        }
        return val;
    }

    //扣钻方式
    public int getDiamondType() {
        int val = 0;
        RadioGroup RadioGroupRadioMemberCount = (RadioGroup) findViewById(R.id.radioCostDaimondType);
        for (int i = 0; i < RadioGroupRadioMemberCount.getChildCount(); i++) {
            RadioButton rb = (RadioButton) RadioGroupRadioMemberCount.getChildAt(i);
            if (rb.isChecked()) {
                if (i == 0) {
                    val = 2;
                } else {
                    val = 1;
                }
                break;
            }
        }
        return val;
    }

    //下注上限
    public int getCostLimit() {
        int val = 0;
        RadioGroup RadioGroupRadioMemberCount = (RadioGroup) findViewById(R.id.radioPayLimit);
        for (int i = 0; i < RadioGroupRadioMemberCount.getChildCount(); i++) {
            RadioButton rb = (RadioButton) RadioGroupRadioMemberCount.getChildAt(i);
            if (rb.isChecked()) {
                if (i == 0) {
                    val = 10;
                } else if (i == 1) {
                    val = 20;
                } else {
                    val = 50;
                }
                break;
            }
        }
        return val;
    }
}
