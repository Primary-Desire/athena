package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.repository.ISysUserRepository;
import cn.ruiheyun.athena.admin.service.ISysRoleService;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.LinkedHashSet;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private ISysUserRepository sysUserRepository;
    @Resource
    private ISysRoleService sysRoleService;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        Mono<SysUser> sysUserMono = findUser(s);
        return sysUserMono.zipWith(sysUserMono.flatMap(sysUser -> sysRoleService.findByUserSn(sysUser.getSn())
                .collect(LinkedHashSet<GrantedAuthority>::new, (authorities, sysRole) -> authorities.add(new SimpleGrantedAuthority(sysRole.getName())))),
                (sysUser, authorities) -> new User(sysUser.getUsername(), sysUser.getPassword(), authorities));
    }

    @Override
    public Mono<SysUser> findByEmail(String email) {
        return sysUserRepository.findByEmail(email);
    }

    @Override
    public Mono<SysUser> findUser(String account) {
        return sysUserRepository.findByUsername(account).switchIfEmpty(sysUserRepository.findByEmail(account));
    }

    @Override
    public Mono<SysUser> save(SysUser sysUser) {
        return sysUserRepository.findByUsername(sysUser.getUsername())
                .switchIfEmpty(sysUserRepository.findByEmail(sysUser.getEmail()))
                .onErrorResume(throwable -> Mono.empty())
                .doOnNext(user -> {
                    throw new RuntimeException("用户名重复或用户邮箱已注册!");
                }).switchIfEmpty(sysUserRepository.save(sysUser));
    }
}
