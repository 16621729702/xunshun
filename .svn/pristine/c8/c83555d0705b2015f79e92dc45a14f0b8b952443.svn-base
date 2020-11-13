package com.wink.livemall.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class UploadConfig {
    //设置文件上传的配置
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(524288000);//设置上传文件的最大值
        resolver.setResolveLazily(true);
        return resolver;
    }
}
