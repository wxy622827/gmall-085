package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author wxy
 * @create 2020-01-03 18:32
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){

        //cors配置对象
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:1000");//跨域请求路径
        config.addAllowedHeader("*");//允许携带的头信息
        config.setAllowCredentials(true);//是否允许携带cooike
        config.addAllowedMethod("*");//允许那些请求方法跨域访问
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",config);
        return  new CorsWebFilter(configurationSource);

    }
}
