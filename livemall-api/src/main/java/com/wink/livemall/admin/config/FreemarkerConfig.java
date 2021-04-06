package com.wink.livemall.admin.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * freemark配置+shiro标签
 */
@Configuration
public class FreemarkerConfig {
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
        freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
        configuration.setDefaultEncoding("UTF-8");
        //这里可以添加其他共享变量 比如sso登录地址
        configuration.setSharedVariable("shiro", new ShiroTags());
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}
