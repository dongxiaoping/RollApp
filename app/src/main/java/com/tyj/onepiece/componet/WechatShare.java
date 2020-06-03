package com.tyj.onepiece.componet;

import android.content.Context;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WechatShare {
    private IWXAPI wxapi;

    public WechatShare(Context that){
        wxapi = WXAPIFactory.createWXAPI(that, "wx8f0020a1e2c7355c", true);
        wxapi.registerApp("wx8f0020a1e2c7355c");
    }

    /**
     * 分享文本类型
     *
     * @param text 文本内容
     * @param type 微信会话或者朋友圈等
     */
    public void shareTextToWx(String text, int type) {
        if (text == null || text.length() == 0) {
            return;
        }

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = type;
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
