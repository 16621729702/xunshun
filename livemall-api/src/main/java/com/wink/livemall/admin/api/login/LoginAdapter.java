package com.wink.livemall.admin.api.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginAdapter implements WebMvcConfigurer {
    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Content-Type","X-Requested-With","accept,Origin","Access-Control-Request-Method","Access-Control-Request-Headers","token")
                .allowedMethods("*")
                .allowedOrigins("*")
                //是否允许使用cookie
                .allowCredentials(true);
    }

    @Autowired
    private TokenInterceptor tokenInterceptor ;

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("进入拦截器");
        //需要放行的请求
        String[] pattrens = new String[]{"/**/login","/**/swagger-resources/**","/**/*.html","/**/webjars/**",
                "/**/index/**","/**/good/**","/**/merchgoods/**","/**/sms/**",
                "/**/swagger-ui.html","/**/live/topcategory","/**/live/list","/**/live/detail",
                "/**/video/**", "/**/register","/**/topimg","/**/search","/**/hot","/**/recommend",
                "/**/merch/detail","/**/merch/merchgood","/**/merch/category","/**/merch/list/**","/**/user/**",
                "/**/pay/notifyWeiXinPay","/**/pay/ordersuccess","/**/falsify/falsifysuccess","/**/falsify/falsifyWeiXinPay",
                "/**/getIOSVersion","/**/getversion","/**/event/**","/**/payLive/**","/**/merMargin/**","/**/download/**",
                "/**/forgotpassword","/**/invite/getInviteURL"};
        //addPathPatterns是表明拦截哪些请求
        //excludePathPatterns是对哪些请求不做拦截
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
                .excludePathPatterns(pattrens);
    }


}
