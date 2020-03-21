package com.tyj.onepiece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tyj.onepiece.componet.BoxImageAdapter;

public class MainActivity extends AppCompatActivity {
    //九宫格图片
    private int[] mThumbIds = {
            R.drawable.share_ico, R.drawable.create_room_icon, R.drawable.my_home
    };
    //九宫格文字
    private String text[] = {"进入游戏", "创建房间", "运行中房间"};

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
                        intent.setData(Uri.parse("https://www.toplaygame.cn/web-mobile/test/main"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        MainActivity.this.startActivity(intent); //启动浏览器
                        Toast.makeText(MainActivity.this, "0", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, CreatRoomActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, WaitGameListActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    default:
                }
            }
        });
    }
}