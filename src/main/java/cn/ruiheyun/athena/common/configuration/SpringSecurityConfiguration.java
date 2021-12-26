package cn.ruiheyun.athena.common.configuration;

import cn.ruiheyun.athena.common.auth.CustomServerSecurityContextRepository;
import cn.ruiheyun.athena.common.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SpringSecurityConfiguration {

    @Autowired
    private AuthFilter authFilter;
    @Autowired
    private CustomServerSecurityContextRepository customServerSecurityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
