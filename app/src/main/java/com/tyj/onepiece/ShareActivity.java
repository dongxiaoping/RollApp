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
import com.tyj.onepiece.componet.ChenXingShare;

import static com.tyj.onepiece.R.id.room_num_edit_text;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText roomNumEditText;

    private ChenXingShare chenXingShare;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.share_button).setOnClickListener(this);
        this.roomNumEditText = (EditText) findViewById(room_num_edit_text);
        chenXingShare = new ChenXingShare();
        chenXingShare.initChenXingShare(ShareActivity.this);
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
                String roomNum = this.roomNumEditText.getText().toString();
                chenXingShare.actionShareButtonClick(roomNum,"6", "12", "100",2);
                break;
        }
    }
}

