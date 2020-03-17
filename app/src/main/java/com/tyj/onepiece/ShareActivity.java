package com.tyj.onepiece;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.chengxin.talk.cxsdk.callback.RegisterAppCallBack;
import com.chengxin.talk.cxsdk.modelmsg.CXWebPageMessage;
import com.chengxin.talk.cxsdk.openapi.CXAPIFactory;
import com.chengxin.talk.cxsdk.openapi.ICXOpenAPI;

public class ShareActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Toolbar的事件---返回
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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

