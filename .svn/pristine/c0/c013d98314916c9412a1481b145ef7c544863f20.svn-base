package com.wink.livemall.admin.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 默认地址配置
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //默认地址（可以是页面或后台请求接口）
        registry.addViewController("/").setViewName("login");
        //设置过滤优先级最高
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileDirPath = new String("livemall-web/src/main/resources/static/images");
        File fileDir = new File(fileDirPath);
        if(!fileDir.exists()){
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        registry.addResourceHandler("/admin/images/**").addResourceLocations(fileDir.getAbsolutePath());
    }
}
