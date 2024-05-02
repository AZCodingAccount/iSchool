package com.ischool.config;

import com.ischool.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;


/*
        处理token的过滤器

 */
@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    JwtProperties jwtProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        URI uri = request.getURI();
        log.info("经过了JwtAuthenticationFilter请求uri为{}", uri);

        // 只对不匹配 /user/login和/user/register ** 的 URL 应用此过滤器
        if (uri.getPath().startsWith("/user/login")
                || uri.getPath().startsWith("/user/register")) {
            return chain.filter(exchange);  // 不匹配则继续其他过滤器处理
        }

        // 提取token
        String token = request.getHeaders().getFirst("token");

        try {
            if (token != null && !token.isEmpty()) {
                // 解析token
                Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
                Long userId = (Long) claims.get("userId");
                String userRole = String.valueOf(claims.get("userRole"));
                log.info("解析用户token，userId为{}，userRole为{}", userId, userRole);
                // 可以根据需要将用户信息添加到请求头中
                request = exchange.getRequest().mutate()
                        .header("id", String.valueOf(userId))
                        .header("role", userRole)
                        .build();
            }
        } catch (Exception e) {
            log.error("Token解析失败", e);
            return exchange.getResponse().setComplete(); // 直接结束请求
        }

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -1; // 控制过滤器执行顺序，数字越小优先级越高
    }
}
