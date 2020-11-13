package com.wink.livemall.admin.config;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class OurWxPayConfig implements WXPayConfig {
    @Override
    public String getAppID() {
        return "wx42f7270dbfb675d0";
    }

    @Override
    public String getMchID() {
        return "1603650355";
    }

    @Override
    public String getKey() {
        return "mqcatmz3io4qoinf0ed01wthr21h0yfg";
    }

    @Override
    public InputStream getCertStream() {
        ClassPathResource cp = new ClassPathResource("apiclient_cert.p12");
        InputStream instream = null;
        try {
            instream = cp.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instream;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 0;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 0;
    }
}
