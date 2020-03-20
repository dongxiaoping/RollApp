package com.tyj.onepiece.componet;

import android.content.Context;
import android.util.Log;
import com.chengxin.talk.cxsdk.callback.RegisterAppCallBack;
import com.chengxin.talk.cxsdk.modelmsg.CXWebPageMessage;
import com.chengxin.talk.cxsdk.openapi.CXAPIFactory;
import com.chengxin.talk.cxsdk.openapi.ICXOpenAPI;

public class ChenXingShare {
    private ICXOpenAPI mIcxOpenAPI;
    public void actionShareButtonClick(String roomNum) {
        Log.e("ok", "xxxxx");
        CXWebPageMessage mCxWebPageMessage = new CXWebPageMessage();
        mCxWebPageMessage._cx_title = "滚筒子";
        mCxWebPageMessage._cx_content = "邀请您一起玩，房间【" + roomNum+"】，抢庄4，局8人，最高下注100";
        mCxWebPageMessage._cx_page_Image = "https://www.toplaygame.cn/share_icon.png";
        mCxWebPageMessage._cx_page_Url = this.getPlayUrlByRoomNum(roomNum);
        mIcxOpenAPI.doReq(mCxWebPageMessage);
    }

    private String getPlayUrlByRoomNum(String num) {
        return "https://www.toplaygame.cn/web-mobile/test/main?roomId=" + num;
    }

    public void initChenXingShare(Context that) {
        String appID = "cxc737e86bc59a47a08ba75789cc2c43ef";
        String secret = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1gqFYOwLvon/3cl2vbelIwyLuQicdxm9Ega8sCOqJfj0WKueiPcs2OwZt/VNIWDPMzdXvY91cq1+ma5QYdz4f1hKKdjQeRve3pXyExWH5BC09iSdCAq50BdtrfXiRYVjlmNohfhOGf6itcWMhGvtkw4cbJfEUF4SlMQvLo3ggBwIDAQAB";
        final ICXOpenAPI mIcxOpenAPI = CXAPIFactory.createCXAPI(that, appID, secret);
        mIcxOpenAPI.registerApp(new RegisterAppCallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(int mCode, Object mMsg) {
                Log.e("xxxx", "xxxxx");
            }
        });
        this.mIcxOpenAPI = mIcxOpenAPI;
    }
}
