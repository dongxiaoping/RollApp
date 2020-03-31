package com.tyj.onepiece.componet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyj.onepiece.R;
import com.tyj.onepiece.WaitGameDetailActivity;

import java.util.List;
import java.util.Map;

public class WaitGameListAdapter extends RecyclerView.Adapter<WaitGameListAdapter.MyViewHolder> {
    //当前上下文对象
    Context context;
    //RecyclerView填充Item数据的List对象
    List<Map<String, Object>> datas;

    public WaitGameListAdapter(Context context, List<Map<String, Object>> datas) {
        this.context = context;
        this.datas = datas;
    }

    //创建ViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_wait_game_list_item, parent, false);
        return new MyViewHolder(v);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        String memberCount = String.valueOf(datas.get(position).get("memberCount"));
        String memberLimit = String.valueOf(datas.get(position).get("memberLimit"));
        holder.wait_game_total_ren.setText(memberLimit);
        holder.wait_game_room_time_desc.setText(String.valueOf(datas.get(position).get("creatTime")));
        holder.wait_game_ren.setText(memberCount);
        holder.wait_game_room_name.setText(String.valueOf(datas.get(position).get("roomId")));
        holder.wait_game_room_state_desc.setText(String.valueOf(datas.get(position).get("roomStateDesc")));
        if(memberCount == memberLimit){
            holder.itemView.setBackgroundColor(Color.parseColor("#f34649"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomId = String.valueOf(holder.wait_game_room_name.getText());
                Intent intent = new Intent();
                intent.putExtra("roomId", roomId);
                intent.setClass(context, WaitGameDetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    //返回Item的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //继承RecyclerView.ViewHolder抽象类的自定义ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView wait_game_total_ren;
        TextView wait_game_room_time_desc;
        TextView wait_game_ren;
        TextView wait_game_room_name;
        TextView wait_game_room_state_desc;

        public MyViewHolder(View itemView) {
            super(itemView);
            wait_game_total_ren = itemView.findViewById(R.id.wait_game_total_ren);
            wait_game_room_time_desc = itemView.findViewById(R.id.wait_game_room_time_desc);
            wait_game_ren = itemView.findViewById(R.id.wait_game_ren);
            wait_game_room_name = itemView.findViewById(R.id.wait_game_room_name);
            wait_game_room_state_desc = itemView.findViewById(R.id.wait_game_room_state_desc);
        }
    }
}