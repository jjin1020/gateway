package com.example.geteway.filters;

import java.net.InetSocketAddress;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class ClientGlobalPreFilter implements GlobalFilter{

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		
		XForwardedRemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
		InetSocketAddress inetSocketAddress = resolver.resolve(exchange);
		
		ServerHttpRequest request = exchange.getRequest()
				.mutate()
				.header("X-Real-IP", inetSocketAddress.getAddress().getHostAddress())
				.build();
		
		ServerWebExchange mutateExchange = exchange.mutate().request(request).build();
		
		return chain.filter(mutateExchange);
	}

	
}
