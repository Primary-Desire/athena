package cn.ruiheyun.athena.auth;

import cn.ruiheyun.athena.common.response.JsonResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JsonWebTokenFilter implements WebFilter {

    @Value("${jwt.header}")
    private String jsonWebTokenHeader;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("JsonWebTokenFilter......");

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (Pattern.matches(".*?/auth/(signIn|signUp)$", path)) {
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            return response(response, HttpStatus.UNAUTHORIZED);
        }
        if (!authorization.startsWith(jsonWebTokenHeader)) {
            return response(response, HttpStatus.FORBIDDEN);
        }
        String token = authorization.substring(jsonWebTokenHeader.length()).trim();
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);
    }

    private Mono<Void> response(ServerHttpResponse response, HttpStatus status) {
        response.setStatusCode(status);
        String responseBody = JSONObject.toJSONString(JsonResult.failed(status.getReasonPhrase()));
        DataBuffer body = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(body));
    }
}
