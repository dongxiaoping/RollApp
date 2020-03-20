package com.tyj.onepiece.componet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyj.onepiece.R;

public class BoxImageAdapter extends BaseAdapter {
    private Context mContext;
    private String textList[]=null;
    private int imgIdList[]=null;
    LayoutInflater inflater;
    private class Holder{
        ImageView item_img;
        TextView item_tex;

        public ImageView getItem_img() {
            return item_img;
        }

        public void setItem_img(ImageView item_img) {
            this.item_img = item_img;
        }

        public TextView getItem_tex() {
            return item_tex;
        }

        public void setItem_tex(TextView item_tex) {
            this.item_tex = item_tex;
        }

    }

    public BoxImageAdapter(Context context, String[] textList, int[] imgIdList) {
        this.mContext = context;
        this.textList = textList;
        this.imgIdList = imgIdList;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.textList.length;
    }

    @Override
    public Object getItem(int position) {
        return this.textList[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.activity_zfb_item,null);
            holder=new Holder();
            holder.item_img=(ImageView)convertView.findViewById(R.id.iv_item);
            holder.item_tex=(TextView)convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        }else{
            holder=(Holder) convertView.getTag();
        }
        holder.item_tex.setText(this.textList[position]);
        holder.item_img.setImageResource(this.imgIdList[position]);

        return convertView;
    }
}
