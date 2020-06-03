package com.tyj.onepiece.componet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tyj.onepiece.R;
import java.io.ByteArrayOutputStream;

public class WechatShare {
    private IWXAPI wxapi;

    public WechatShare(Context that) {
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

    public void shareWebToWx(int type, Context that, String url, String title, String des) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = des;
        Bitmap thumbBmp = BitmapFactory.decodeResource(that.getResources(), R.drawable.tongzi);
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        //  req.userOpenId
        //调用api接口，发送数据到微信
        wxapi.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
