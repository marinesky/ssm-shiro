package com.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by wangyong on 2016/7/3.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.controller", useDefaultFilters = false, includeFilters = {@ComponentScan.Filter(RestController.class), @ComponentScan.Filter(Controller.class)})
public class MVConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MVConfig.class);


    /**
     * 视图解析器
     * velocity
     * 过期是由于velocity六年没有更新过了,spring官方推荐使用FreeMarker或者Thymeleaf
     * 后期考虑spring官方的Thymeleaf
     */
    @Bean
    public ViewResolver viewResolver() {
    /*    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setContentType("text/html;charset=utf-8");
        viewResolver.setPrefix("/static/");
        viewResolver.setSuffix(".html");
        return viewResolver;*/
        VelocityViewResolver velocityViewResolver = new VelocityViewResolver();
        velocityViewResolver.setCache(true);
        velocityViewResolver.setPrefix("");
        velocityViewResolver.setSuffix(".html");
        velocityViewResolver.setContentType("text/html;charset=utf-8");

        //关于Toolbox路径问题:如何路径开始头没有/会自动加上/，所以你如果写classpath:之类的识别不了
        velocityViewResolver.setToolboxConfigLocation("/WEB-INF/velocityToolBox.xml");
        velocityViewResolver.setViewClass(VelocityToolboxView.class);
        return velocityViewResolver;
    }

    /**
     * 视图解析器
     * Thymeleaf
     *
     * @param servletContext
     * @return
     */
  /*  @Bean(name = "templateResolver")
    public ServletContextTemplateResolver servletContextTemplateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setPrefix("/WEB-INF/template/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }*/


    /**
     * 过期是由于velocity六年没有更新过了,spring官方推荐使用FreeMarker或者Thymeleaf
     *
     * @return
     */
    @Bean(name = "velocityConfig")
    public VelocityConfigurer velocityConfigurer() {
        VelocityConfigurer configurer = new VelocityConfigurer();
        configurer.setResourceLoaderPath("/WEB-INF/template/");
        Properties properties = new Properties();

        //解决乱码问题
        properties.setProperty("input.encoding", "utf-8");
        properties.setProperty("output.encoding", "utf-8");
        configurer.setVelocityProperties(properties);
        return configurer;
    }

    /**
     * 配置静态资源可以访问
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bower_components/**")
                .addResourceLocations("/bower_components/");
        // .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());

        registry.addResourceHandler("/script/**")
                .addResourceLocations("/script/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("/images/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/");
        // .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());

        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
        // .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }

    private FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4() {
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4 = new FastJsonHttpMessageConverter4();
        fastJsonHttpMessageConverter4.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue);
        fastJsonHttpMessageConverter4.setFastJsonConfig(fastJsonConfig);

        return fastJsonHttpMessageConverter4;
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.debug("add HttpMessageConverter...");

        // converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(fastJsonHttpMessageConverter4());
        super.configureMessageConverters(converters);
    }


}
