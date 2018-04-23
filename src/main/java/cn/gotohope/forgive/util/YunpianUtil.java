package cn.gotohope.forgive.util;

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;

import java.util.Map;

public class YunpianUtil {

    private final static YunpianClient client = new YunpianClient(Config.apikey).init();

    public static boolean send(String phone, String text) {
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, phone);
        param.put(YunpianClient.TEXT, text);
        Result<SmsSingleSend> r = client.sms().single_send(param);
        System.out.println(r);
        return r.isSucc();
    }

}
