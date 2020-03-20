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
        TextView RefreshTextObject = (TextView) findViewById(R.id.room_num_text);
        RefreshTextObject.setText(roomId+"号房间创建成功!");
        chenXingShare = new ChenXingShare();
        chenXingShare.initChenXingShare(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_share_button:
                chenXingShare.actionShareButtonClick(this.roomId );
                break;
        }
    }
}
