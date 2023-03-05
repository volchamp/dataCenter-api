package com.yxc.imapi.config;

import com.yxc.imapi.interceptor.AuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwx
 * @date 2021-05-25 16:12.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    AuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<>();
//        patterns.add("/**/cameras/previewURLs");
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }
}
