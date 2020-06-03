package com.tyj.onepiece;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tyj.onepiece.componet.ChenXingShare;
import com.tyj.onepiece.componet.WechatShare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 创建成功提示页面，当前该页面有分享功能
 * */
public class RoomSuccessActivity extends AppCompatActivity implements View.OnClickListener {
    String roomId;
    String memberLimit; //人数
    String playCount; //局数
    String costLimit; //下注上限
    String showContent;
    int roomPay; //2 代开房  1 AA房
    private ChenXingShare chenXingShare;
    private WechatShare wechatShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_success);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.roomId = intent.getStringExtra("roomId");
        this.memberLimit = intent.getStringExtra("memberLimit");
        this.playCount = intent.getStringExtra("playCount");
        this.costLimit = intent.getStringExtra("costLimit");
        String roomPay = intent.getStringExtra("roomPay");
        this.roomPay = Integer.parseInt(roomPay);
        TextView RefreshTextObject = (TextView) findViewById(R.id.room_num_text);
        RefreshTextObject.setText(roomId + "号房间创建成功!");
        findViewById(R.id.content_share_button).setOnClickListener(this);
        findViewById(R.id.edit_share_edit_copy_id).setOnClickListener(this);

        chenXingShare = new ChenXingShare();
        chenXingShare.initChenXingShare(RoomSuccessActivity.this);
        this.showContent = this.getShareContent(this.roomId, this.memberLimit, this.playCount, this.costLimit, this.roomPay);
        EditText editTextShow = (EditText) findViewById(R.id.edit_share_edit_id);
        editTextShow.setGravity(Gravity.LEFT);
        editTextShow.setText(this.showContent);
        wechatShare = new WechatShare(this);
    }

    public String getShareContent(String roomNum, String renShu, String juShu, String costLimit, int roomPay) {
        String roomPayString = "";
        if (roomPay == 2) {
            roomPayString = "代开";
        } else {
            roomPayString = "AA";
        }
        String item = " 邀请您一起玩，" + roomPayString + "房间【" + roomNum + "】，人数上限" + renShu +
                "，局数" + juShu + "，最高下" + costLimit + ",点击URL地址进入：" + this.getPlayUrlByRoomNum(roomNum);
        return item;
    }

    private String getPlayUrlByRoomNum(String num) {
        String adddr = MeterApplication.getInstance().getAgencyConfig().getGameUrl();
        return adddr + "?roomId=" + num;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //返回按钮
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_share_button:
                //chenXingShare.actionShareButtonClick(this.roomId, this.memberLimit, this.playCount, this.costLimit, this.roomPay);
                wechatShare.shareTextToWx("aaa", SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.edit_share_edit_copy_id:
                ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(this.showContent, this.showContent);
                mClipboardManager.setPrimaryClip(clipData);
                Toast.makeText(RoomSuccessActivity.this, "复制成功！", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
