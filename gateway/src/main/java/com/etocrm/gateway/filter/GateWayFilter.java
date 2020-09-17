package com.etocrm.gateway.filter;

import com.etocrm.gateway.config.GatewayPropertiesConfig;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author qi.li
 * @create 2020/9/15 14:15
 */
@Component
public class GateWayFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GateWayFilter.class);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String url = serverHttpRequest.getURI().toString();
        String method = serverHttpRequest.getMethodValue();
        log.info("url:{},method:{},headers:{}", url, method, serverHttpRequest.getHeaders());
        return chain.filter(exchange);
    }
    //执行顺序
    @Override
    public int getOrder() {
        return 1;
    }
}
