package com.chengkun.cloud;


import com.chengkun.cloud.factory.RequestTimeGatewayFilterFactory;
import com.chengkun.cloud.filter.RequestTimeFilter;
import com.chengkun.cloud.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceGatewayFilterApplication {

    @Autowired
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayFilterApplication.class, args);
    }

    @Bean
    public RouteLocatorBuilder routeLocatorBuilder() {
        return new RouteLocatorBuilder(context);
    }

    @Bean
    public TokenFilter tokenFilter(){
        return new TokenFilter();
    }
    @Bean
    public RequestTimeGatewayFilterFactory myGatewayFilterFactory(){
        return new RequestTimeGatewayFilterFactory();
    }
    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        // @formatter:off
        return builder.routes()
                .route(r -> r.path("/customer/**")
                        .filters(f -> f.filter(new RequestTimeFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://httpbin.org:80/get")
                        .order(0)
                        .id("customer_filter_router")
                )
                .build();
        // @formatter:on
    }
}
