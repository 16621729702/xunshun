package com.wink.livemall.admin.util.idCardUtils;

import com.baidu.aip.client.BaseClient;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIUrl extends BaseClient {
    protected APIUrl(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }
    public JSONObject idcard(String url, String idCardSide, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        this.preOperation(request);
        request.addBody("url", url);
        request.addBody("id_card_side", idCardSide);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri("https://aip.baidubce.com/rest/2.0/ocr/v1/idcard");
        this.postOperation(request);
        return this.requestServer(request);
    }


}
