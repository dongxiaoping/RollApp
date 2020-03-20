package com.tyj.onepiece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tyj.onepiece.componet.BoxImageAdapter;

public class MainActivity extends AppCompatActivity {
    //九宫格图片
    private int[] mThumbIds = {
            R.drawable.share_ico, R.drawable.my_home, R.drawable.my_home
    };
    //九宫格文字
    private String text[] = {"房间分享", "我的房间", "创建房间"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new BoxImageAdapter(this, this.text, this.mThumbIds));
        //单击GridView元素的响应
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this, ShareActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, CreatRoomActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    default:
                }
            }
        });
    }
}