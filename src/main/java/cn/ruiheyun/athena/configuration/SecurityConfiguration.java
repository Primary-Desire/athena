package cn.ruiheyun.athena.configuration;

import cn.ruiheyun.athena.auth.CustomServerSecurityContextRepository;
import cn.ruiheyun.athena.auth.JsonWebTokenFilter;
import cn.ruiheyun.athena.common.util.RSAUtils;
import cn.ruiheyun.athena.common.util.StringRedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyPair;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Autowired
    private StringRedisUtils stringRedisUtils;
    @Autowired
    private JsonWebTokenFilter jsonWebTokenFilter;
    @Autowired
    private CustomServerSecurityContextRepository customServerSecurityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public void initializationRSAKey() {
        if (!stringRedisUtils.exist(RSAUtils.PUBLIC_KEY) || !stringRedisUtils.exist(RSAUtils.PRIVATE_KEY)) {
            KeyPair keyPair = RSAUtils.getKeyPair();
            stringRedisUtils.set(RSAUtils.PUBLIC_KEY, RSAUtils.getPublicKey(keyPair));
            stringRedisUtils.set(RSAUtils.PRIVATE_KEY, RSAUtils.getPrivateKey(keyPair));
        }
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/v1/auth/*").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAfter(jsonWebTokenFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(customServerSecurityContextRepository)
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable().build();
    }

}
