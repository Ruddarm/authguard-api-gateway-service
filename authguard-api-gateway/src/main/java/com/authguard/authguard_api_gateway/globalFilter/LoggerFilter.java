package com.authguard.authguard_api_gateway.globalFilter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggerFilter implements Ordered, GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Loging from global filte : Pre " + exchange.getRequest().getURI());
        // postfilter
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Loging from global filte : post " + exchange.getRequest().getURI());

        }));
    }

    @Override
    public int getOrder() {
        return 5;
    }

}
