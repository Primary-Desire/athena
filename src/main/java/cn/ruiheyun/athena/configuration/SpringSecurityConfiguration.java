package cn.ruiheyun.athena.configuration;

import cn.ruiheyun.athena.auth.CustomServerSecurityContextRepository;
import cn.ruiheyun.athena.auth.service.IRSAKeyService;
import cn.ruiheyun.athena.filter.AuthFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.annotation.Resource;

@Configuration
public class SpringSecurityConfiguration {

    @Autowired
    private AuthFilter authFilter;
    @Autowired
    private CustomServerSecurityContextRepository customServerSecurityContextRepository;

    @Resource
    private IRSAKeyService keyService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public void initializationKey() {
        keyService.findKey()
                .filter(key -> StringUtils.isNotBlank(key.getPrivateKey()) && StringUtils.isNotBlank(key.getPublicKey()))
                .switchIfEmpty(keyService.saveKey())
                .subscribe();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/v1/auth/*").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAfter(authFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(customServerSecurityContextRepository)
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable().build();
    }

}
