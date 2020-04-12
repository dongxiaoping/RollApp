package com.tyj.onepiece;

import android.app.Application;

import com.tyj.onepiece.model.AgencyConfig;

/* 全局变量存储
* 调用方法：  MeterApplication.getInstance().getXXX();   //读取

                     MeterApplication.getInstance().setXXX(X xxx);//存入
                     * */
public class MeterApplication extends Application {
    private static MeterApplication instance;
    private AgencyConfig agencyConfig;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        instance = this;
        super.onCreate();
    }
    public static MeterApplication getInstance() {
        return instance;
    }

    public void setAgencyConfig(AgencyConfig agencyConfig) {
        this.agencyConfig = agencyConfig;
    }

    public AgencyConfig getAgencyConfig() {
        return this.agencyConfig;
    }
}
