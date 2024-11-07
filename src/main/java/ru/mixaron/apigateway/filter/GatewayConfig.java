//package ru.mixaron.apigateway.filter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableHystrix
//public class GatewayConfig {
//
//    @Autowired
//    private AuthenticationFilter filter;  // Ваш пользовательский фильтр
//
//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("reservation-service", r -> r.path("/reservation/**")
//                        .filters(f -> f.filter(filter))  // Добавляем фильтр через код
//                        .uri("lb://reservation-service"))
//                .route("auth-service", r -> r.path("/auth/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://auth-service"))
//                .route("notification-service", r -> r.path("/notification/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://notification-service"))
//                .build();
//    }
//}
