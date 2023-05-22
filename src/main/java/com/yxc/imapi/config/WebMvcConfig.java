package com.yxc.imapi.config;

import com.yxc.imapi.interceptor.AuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 程序员大佬超
 * @date 2023-03-01 15:33.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    AuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<>();
        patterns.add("/**/login/login");
        patterns.add("/**/login/getValidateCode");
        patterns.add("/**/register/accountRegister");
        patterns.add("/**/attach/uploadAtt");
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }
}
