package com.ischool.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import com.ischool.model.Result;
import com.ischool.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;


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

        // 处理knife4j的uri
        String path = exchange.getRequest().getURI().getPath();
        if (path.contains("/v3/api-docs")) {
            String newPath = path.substring(0, path.length() - "/default".length());
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate().path(newPath).build())
                    .build();
            return chain.filter(modifiedExchange);
        }

        // 只对不匹配 /user/login和/user/register ** 的 URL 应用此过滤器
        // todo：后续设置白名单
        if (uri.getPath().startsWith("/user/login")
                || uri.getPath().startsWith("/user/register")
                || uri.getPath().startsWith("/search/page")) {
            return chain.filter(exchange);  // 不匹配则继续其他过滤器处理
        }


        // todo: ai聊天计费、限流
        // 提取token
        String token = request.getHeaders().getFirst("token");
        log.info("token为{}", token);
        if (token == null || token.isEmpty()) {
            log.error("Token为空");
            BaseResponse<?> errorResult = Result.error(ErrorCode.NOT_LOGIN_ERROR, "Token解析失败");
            return this.writeResponse(exchange.getResponse(), errorResult);
        }

        try {
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
        } catch (Exception e) {
            log.error("Token解析失败", e);
            BaseResponse<?> errorResult = Result.error(ErrorCode.NOT_LOGIN_ERROR, "Token解析失败");
            return this.writeResponse(exchange.getResponse(), errorResult);
        }

        return chain.filter(exchange.mutate().request(request).build());
        // return chain.filter(exchange);  // 不匹配则继续其他过滤器处理

    }


    /*
     * 构建返回参数
     * */
    private Mono<Void> writeResponse(ServerHttpResponse response, BaseResponse<?> result) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = toJson(result);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    private byte[] toJson(BaseResponse<?> result) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }


    @Override
    public int getOrder() {
        return -1; // 控制过滤器执行顺序，数字越小优先级越高
    }
}
