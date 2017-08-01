package com.loe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/join").setViewName("join");
        registry.addViewController("/join2").setViewName("join2");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/coupon").setViewName("coupon");
        registry.addViewController("/event").setViewName("event");
        registry.addViewController("/inquery").setViewName("inquery");
        registry.addViewController("/inquery2").setViewName("inquery2");
        registry.addViewController("/map").setViewName("map");
        registry.addViewController("/map2").setViewName("map2");
        registry.addViewController("/map3").setViewName("map3");
	}
	
}