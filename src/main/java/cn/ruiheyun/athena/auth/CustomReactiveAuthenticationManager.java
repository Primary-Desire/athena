package cn.ruiheyun.athena.auth;

import cn.ruiheyun.athena.common.util.JsonWebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户认证的管理类
 */
@Slf4j
@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JsonWebTokenUtils jsonWebTokenUtils;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("反应式身份验证管理器......");
        return Mono.just(authentication)
                .map(auth -> jsonWebTokenUtils.getClaimsForToken(auth.getCredentials().toString()))
                .log().onErrorResume(throwable -> {
                    log.error("验证Json Web Token时发生错误, 错误类型: {}, 错误信息: {}", throwable.getClass(), throwable.getMessage());
                    return Mono.empty();
                }).map(claims -> new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                        Stream.of(claims.get(JsonWebTokenUtils.CLAIM_KEY_ROLES))
                                .map(roleList -> (String[]) roleList)
                                .map(roleList -> new SimpleGrantedAuthority(Arrays.toString(roleList)))
                                .collect(Collectors.toList())
                ));
    }
}
