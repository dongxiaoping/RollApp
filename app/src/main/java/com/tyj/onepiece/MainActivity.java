package com.tyj.onepiece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.chengxin.talk.cxsdk.callback.RegisterAppCallBack;
import com.chengxin.talk.cxsdk.modelmsg.CXWebPageMessage;
import com.chengxin.talk.cxsdk.openapi.CXAPIFactory;
import com.chengxin.talk.cxsdk.openapi.ICXOpenAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.chenXingShareTest();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
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
                mCxWebPageMessage._cx_title = "a";
                mCxWebPageMessage._cx_content = "b";
                mCxWebPageMessage._cx_page_Image = "https://www.toplaygame.cn/share_icon.png";
                mCxWebPageMessage._cx_page_Url = "https://www.toplaygame.cn/web-mobile/";
                mIcxOpenAPI.doReq(mCxWebPageMessage);
            }

            @Override
            public void onFail(int mCode, Object mMsg) {
                Log.e("xxxx", "xxxxx");
            }
        });
        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ee);
    }
}
