package com.loe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login");
        registry.addViewController("/login").setViewName("LGN0001");
        registry.addViewController("/join").setViewName("JON0001");
        registry.addViewController("/join2").setViewName("JON0002");
        registry.addViewController("/main").setViewName("MAN0001");
        registry.addViewController("/coupon").setViewName("COU0001");
        registry.addViewController("/event").setViewName("MAN0002");
        registry.addViewController("/inquery").setViewName("INQ0001");
        registry.addViewController("/inquery2").setViewName("INQ0002");
        registry.addViewController("/map").setViewName("MAP0001");
        registry.addViewController("/map2").setViewName("MAP0002");
        registry.addViewController("/map3").setViewName("MAP0003");
	}
	
}