package com.tyj.onepiece;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.chengxin.talk.cxsdk.callback.RegisterAppCallBack;
import com.chengxin.talk.cxsdk.modelmsg.CXWebPageMessage;
import com.chengxin.talk.cxsdk.openapi.CXAPIFactory;
import com.chengxin.talk.cxsdk.openapi.ICXOpenAPI;

import static com.tyj.onepiece.R.id.room_num_edit_text;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText roomNumEditText;
    private ICXOpenAPI mIcxOpenAPI;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.share_button).setOnClickListener(this);
        this.roomNumEditText = (EditText) findViewById(room_num_edit_text);
        this.mIcxOpenAPI = this.initChenXingShare();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Toolbar的事件---返回
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_button:
                this.actionShareButtonClick();
                break;
        }
    }

    public void actionShareButtonClick() {
        String roomNum = this.roomNumEditText.getText().toString();
        Log.e("ok", "xxxxx");
        CXWebPageMessage mCxWebPageMessage = new CXWebPageMessage();
        mCxWebPageMessage._cx_title = "滚筒子";
        mCxWebPageMessage._cx_content = "邀请您一起玩，房间【" + roomNum+"】，抢庄4，局8人，最高下注100";
        mCxWebPageMessage._cx_page_Image = "https://www.toplaygame.cn/share_icon.png";
        mCxWebPageMessage._cx_page_Url = this.getPlayUrlByRoomNum(roomNum);
        this.mIcxOpenAPI.doReq(mCxWebPageMessage);
        Toast.makeText(this, this.getPlayUrlByRoomNum(roomNum), Toast.LENGTH_SHORT).show();
    }

    private String getPlayUrlByRoomNum(String num) {
        return "https://www.toplaygame.cn/web-mobile/test/1_0_44?roomNum=" + num;
    }

    public ICXOpenAPI initChenXingShare() {
        String appID = "cxc737e86bc59a47a08ba75789cc2c43ef";
        String secret = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1gqFYOwLvon/3cl2vbelIwyLuQicdxm9Ega8sCOqJfj0WKueiPcs2OwZt/VNIWDPMzdXvY91cq1+ma5QYdz4f1hKKdjQeRve3pXyExWH5BC09iSdCAq50BdtrfXiRYVjlmNohfhOGf6itcWMhGvtkw4cbJfEUF4SlMQvLo3ggBwIDAQAB";
        final ICXOpenAPI mIcxOpenAPI = CXAPIFactory.createCXAPI(this, appID, secret);
        mIcxOpenAPI.registerApp(new RegisterAppCallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(int mCode, Object mMsg) {
                Log.e("xxxx", "xxxxx");
            }
        });
        return mIcxOpenAPI;
    }
}

