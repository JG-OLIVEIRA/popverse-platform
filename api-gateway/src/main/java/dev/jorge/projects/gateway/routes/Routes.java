package dev.jorge.projects.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpStatus;

import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Value("${product.service.url}")
    private String productServiceUrl;
    @Value("${order.service.url}")
    private String orderServiceUrl;
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){
        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(productServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(config  -> config
                        .setId("productServiceCircuitBreaker")
                        .setFallbackUri("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(productServiceUrl))
                .before(BeforeFilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(config  -> config
                        .setId("productServiceSwaggerCircuitBreaker")
                        .setFallbackUri("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(orderServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(config  -> config
                        .setId("orderServiceCircuitBreaker")
                        .setFallbackUri("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(orderServiceUrl))
                .before(BeforeFilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(config  -> config
                        .setId("orderServiceSwaggerCircuitBreaker")
                        .setFallbackUri("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute(){
        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(inventoryServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(config  -> config
                        .setId("inventoryServiceCircuitBreaker")
                        .setFallbackUri("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(inventoryServiceUrl))
                .before(BeforeFilterFunctions.setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(config  -> config
                        .setId("inventoryServiceSwaggerCircuitBreaker")
                        .setFallbackUri("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return GatewayRouterFunctions.route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }

}
