package com.neogiciel.gw.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    Logger logger = LoggerFactory.getLogger(SpringCloudConfig.class);

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        logger.info("[Service GW Springboot] SpringCloudConfig : gatewayRoutes");

        return builder.routes()
                .route(r -> r.path("/security")
                        .uri("http://dev2.neogiciel.com"))
                        //.uri("http://localhost:8081"))
                
                //.route(r -> r.path("/test")
                //        .uri("http://dev2.neogiciel.com"))

                .build();
    }

}