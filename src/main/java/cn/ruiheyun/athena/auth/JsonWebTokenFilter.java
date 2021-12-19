package cn.ruiheyun.athena.auth;

import cn.ruiheyun.athena.common.util.CommonUtils;
import cn.ruiheyun.athena.common.util.JsonWebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Slf4j
@Component
public class JsonWebTokenFilter implements WebFilter {

    @Value("${jwt.header}")
    private String jsonWebTokenHeader;
    @Autowired
    private JsonWebTokenUtils jsonWebTokenUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("JsonWebTokenFilter......");

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (Pattern.matches("/api/v1/auth/.*?$", path)) {
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            return CommonUtils.response(response, HttpStatus.UNAUTHORIZED, "请求未携带令牌");
        }
        if (!authorization.startsWith(jsonWebTokenHeader)) {
            return CommonUtils.response(response, HttpStatus.UNAUTHORIZED, "令牌格式错误");
        }
        String token = authorization.substring(jsonWebTokenHeader.length()).trim();
        if (StringUtils.isBlank(token)) {
            return CommonUtils.response(response, HttpStatus.UNAUTHORIZED, "令牌格式错误");
        }
        if (!jsonWebTokenUtils.verificationToken(token, CommonUtils.getRealIpAddress(exchange))) {
            return CommonUtils.response(response, HttpStatus.UNAUTHORIZED, "令牌已过期或IP地址不匹配");
        }
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);
    }
}
