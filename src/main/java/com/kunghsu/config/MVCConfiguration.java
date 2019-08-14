package com.kunghsu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by xuyaokun On 2019/8/12 21:46
 * @desc: 
 */
@Configuration
public class MVCConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        //这个欢迎页必须要有，因为单点成功之后，是先跳转到欢迎页，然后再跳到具体的集成客户端页面
        registry.addViewController("/").setViewName("forward:index.jsp");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);

    }
}
