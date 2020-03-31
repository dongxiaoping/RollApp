package com.tyj.onepiece.componet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyj.onepiece.R;

import java.util.List;
import java.util.Map;

public class WaitGameListAdapter extends RecyclerView.Adapter<WaitGameListAdapter.MyViewHolder> {
    //当前上下文对象
    Context context;
    //RecyclerView填充Item数据的List对象
    List<Map<String, Object>>  datas;

    public WaitGameListAdapter(Context context, List<Map<String, Object>> datas){
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
            holder.wait_game_total_ren.setText(String.valueOf(datas.get(position).get("memberLimit")));
            holder.wait_game_room_time_desc.setText(String.valueOf(datas.get(position).get("creatTime")));
            holder.wait_game_ren.setText(String.valueOf(datas.get(position).get("memberCount")));
            holder.wait_game_room_name.setText(String.valueOf(datas.get(position).get("roomId")));
            holder.wait_game_room_state_desc.setText(String.valueOf(datas.get(position).get("roomStateDesc")));

            holder.wait_game_room_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,holder.wait_game_ren.getText()+"被点击了",
                            Toast.LENGTH_SHORT).show();
                }
            });
    }

    //返回Item的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //继承RecyclerView.ViewHolder抽象类的自定义ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
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