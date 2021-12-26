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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private ISysUserRepository sysUserRepository;
    @Resource
    private ISysRoleService sysRoleService;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        Mono<SysUser> sysUserMono = sysUserRepository.findByUsername(s).switchIfEmpty(sysUserRepository.findByEmail(s));
        return sysUserMono.zipWith(sysUserMono.flatMap(sysUser -> sysRoleService.findByUserSn(sysUser.getSn()).collectList()), (sysUser, sysRoleList) -> {
            List<GrantedAuthority> roleNameList = sysRoleList.stream().map(sysRole -> new SimpleGrantedAuthority(sysRole.getName())).collect(Collectors.toList());
            return new User(sysUser.getUsername(), sysUser.getPassword(), roleNameList);
        });
    }

    @Override
    public Mono<SysUser> findByEmail(String email) {
        return sysUserRepository.findByEmail(email);
    }

    @Override
    public Mono<SysUser> save(SysUser sysUser) {
        return sysUserRepository.findByUsername(sysUser.getUsername())
                .switchIfEmpty(sysUserRepository.findByEmail(sysUser.getEmail()))
                .doOnNext(user -> {
                    throw new RuntimeException("用户名重复或用户邮箱已注册!");
                }).switchIfEmpty(sysUserRepository.save(sysUser));
    }
}
