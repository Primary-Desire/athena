package cn.ruiheyun.athena.admin.service;

import cn.ruiheyun.athena.admin.entity.SysUser;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

public interface ISysUserService extends ReactiveUserDetailsService {

    Mono<SysUser> findByEmail(String email);

    Mono<SysUser> findUser(String account);

    Mono<SysUser> save(SysUser sysUser);

}
