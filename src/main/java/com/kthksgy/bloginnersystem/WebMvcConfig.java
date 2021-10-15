package com.kthksgy.bloginnersystem;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(false);
        resolver.setMaxPageSize(25);
        argumentResolvers.add(resolver);
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
        .addResourceHandler("/admin/**")
        .addResourceLocations("classpath:/static/admin/");
        registry
          .addResourceHandler("/**")
          .addResourceLocations("file:./html/", "classpath:/static/");
    }
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/admin").setViewName("forward:/admin/index.html");
		registry.addViewController("/admin/").setViewName("forward:/admin/index.html");
	    registry.addViewController("/").setViewName("forward:/index.html");
	}
}
