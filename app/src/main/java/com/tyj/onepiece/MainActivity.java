package com.tyj.onepiece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chengxin.talk.cxsdk.callback.RegisterAppCallBack;
import com.chengxin.talk.cxsdk.modelmsg.CXWebPageMessage;
import com.chengxin.talk.cxsdk.openapi.CXAPIFactory;
import com.chengxin.talk.cxsdk.openapi.ICXOpenAPI;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //九宫格图片
    private int[] mThumbIds = {
            R.drawable.share_ico, R.drawable.my_home
    };
    //九宫格文字
    private String text[]={"房间分享","我的房间"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new BoxImageAdapter(this,this.text,this.mThumbIds));
        //单击GridView元素的响应
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //弹出单击的GridView元素的位置
                Toast.makeText(MainActivity.this,mThumbIds[position], Toast.LENGTH_SHORT).show();
                try {
                    MainActivity.this.chenXingShareTest();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void chenXingShareTest() throws PackageManager.NameNotFoundException {
        String appID = "cxc737e86bc59a47a08ba75789cc2c43ef";
        String secret = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1gqFYOwLvon/3cl2vbelIwyLuQicdxm9Ega8sCOqJfj0WKueiPcs2OwZt/VNIWDPMzdXvY91cq1+ma5QYdz4f1hKKdjQeRve3pXyExWH5BC09iSdCAq50BdtrfXiRYVjlmNohfhOGf6itcWMhGvtkw4cbJfEUF4SlMQvLo3ggBwIDAQAB";

        final ICXOpenAPI mIcxOpenAPI = CXAPIFactory.createCXAPI(this, appID, secret);
        mIcxOpenAPI.registerApp(new RegisterAppCallBack() {
            @Override
            public void onSuccess() {
                Log.e("ok", "xxxxx");
                CXWebPageMessage mCxWebPageMessage = new CXWebPageMessage();
                mCxWebPageMessage._cx_title = "滚筒子";
                mCxWebPageMessage._cx_content = "滚筒子房间10，AA房，10局";
                mCxWebPageMessage._cx_page_Image = "https://www.toplaygame.cn/share_icon.png";
                mCxWebPageMessage._cx_page_Url = "https://www.toplaygame.cn/web-mobile/";
                mIcxOpenAPI.doReq(mCxWebPageMessage);
            }
            @Override
            public void onFail(int mCode, Object mMsg) {
                Log.e("xxxx", "xxxxx");
            }
        });
    }
}