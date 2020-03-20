package com.tyj.onepiece;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tyj.onepiece.componet.ChenXingShare;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class RoomSuccessActivity extends AppCompatActivity implements View.OnClickListener{
    String roomId;
    String memberLimit; //人数
    String playCount; //局数
    String costLimit; //下注上限
    int roomPay; //2 代开房  1 AA房
    private ChenXingShare chenXingShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_success);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        this.roomId = intent.getStringExtra("roomId");
        this.memberLimit = intent.getStringExtra("memberLimit");
        this.playCount = intent.getStringExtra("playCount");
        this.costLimit = intent.getStringExtra("costLimit");
        String roomPay = intent.getStringExtra("roomPay");
        this.roomPay = Integer.parseInt(roomPay);
        TextView RefreshTextObject = (TextView) findViewById(R.id.room_num_text);
        RefreshTextObject.setText(roomId+"号房间创建成功!");
        findViewById(R.id.content_share_button).setOnClickListener(this);

        chenXingShare = new ChenXingShare();
        chenXingShare.initChenXingShare(RoomSuccessActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_share_button:
                chenXingShare.actionShareButtonClick(this.roomId, this.memberLimit, this.playCount, this.costLimit, this.roomPay );
                break;
        }
    }
}
