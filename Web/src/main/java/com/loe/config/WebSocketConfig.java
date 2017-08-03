package com.loe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/dashboard/Temperature").withSockJS();
		registry.addEndpoint("/dashboard/Sound").withSockJS();
		registry.addEndpoint("/dashboard/Dust").withSockJS();
		registry.addEndpoint("/dashboard/Pressure1").withSockJS();
		registry.addEndpoint("/dashboard/Pressure2").withSockJS();
//		registry.addEndpoint("/dashboard/Pressure3").withSockJS();
//		registry.addEndpoint("/dashboard/Pressuer4").withSockJS();
		registry.addEndpoint("/realwindow").withSockJS();
	}

}