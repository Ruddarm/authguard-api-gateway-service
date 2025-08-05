package com.authguard.authguard_api_gateway.globalFilter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserFilter extends AbstractGatewayFilterFactory<UserFilter.Config> {

    public UserFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Order filter pre: {}", exchange.getRequest().getURI());
            return chain.filter(exchange);
        };
    }

    static class Config {

    }
}
