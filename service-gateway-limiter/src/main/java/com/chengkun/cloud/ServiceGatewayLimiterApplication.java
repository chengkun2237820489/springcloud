package com.chengkun.cloud;

import com.chengkun.cloud.resolver.HostAddrKeyResolver;
import com.chengkun.cloud.resolver.UrlKeyResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ServiceGatewayLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayLimiterApplication.class, args);
    }

    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }

    @Bean
    public UrlKeyResolver urlKeyResolver(){
        return new UrlKeyResolver();
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }
}
