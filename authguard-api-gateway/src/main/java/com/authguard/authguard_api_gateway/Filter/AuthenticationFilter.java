package com.authguard.authguard_api_gateway.Filter;

import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.authguard.authguard_api_gateway.Service.JwtService;

import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtService jwtService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Data
    static class Config {
        private boolean enabled;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!config.enabled) {
                return chain.filter(exchange);
            } else {
                log.info("Authorization request " + exchange.getRequest().getURI());
                final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    log.error("Auth token header not found");
                    return exchange.getResponse().setComplete();
                }
                // jwt verification ,get ur id
                try {

                    String token = tokenHeader.substring(7).trim();
                    // log.info("token : {}", token);
                    String userId = jwtService.generateUserIdFromToken(token);
                    ServerWebExchange mutatedRequest = exchange.mutate().request(r -> r.header("X-USER-Id", userId))
                            .build();
                    log.info("Authentication Sucessful {}",  userId);
                    return chain.filter(mutatedRequest);

                } catch (JwtException e) {
                    // e.printStackTrace();
                    log.error("JWT Exception : {} ", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

            }
        };
    }
}
