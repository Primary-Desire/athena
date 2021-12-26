package cn.ruiheyun.athena.common.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义服务器安全上下文存储库
 */
@Slf4j
@Component
public class CustomServerSecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private CustomReactiveAuthenticationManager customReactiveAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        String token = serverWebExchange.getAttribute("token");
        return customReactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(token, token))
                .onErrorResume(throwable -> Mono.empty())
                .map(SecurityContextImpl::new);
    }
}
