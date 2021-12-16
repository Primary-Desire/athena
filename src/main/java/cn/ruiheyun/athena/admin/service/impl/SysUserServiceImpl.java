package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysRole;
import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.mapper.SysUserMapper;
import cn.ruiheyun.athena.admin.service.ISysRoleService;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private ISysRoleService sysRoleService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        SysUser sysUser = lambdaQuery().eq(SysUser::getUsername, username).one();
        if (Objects.isNull(sysUser)) {
            return Mono.empty();
        }
        List<SysRole> roleList = sysRoleService.listByUserSn(sysUser.getSn());
        List<GrantedAuthority> authorities = roleList.stream().map(sysRole -> new SimpleGrantedAuthority(sysRole.getName())).collect(Collectors.toList());
        return Mono.just(new User(sysUser.getUsername(), sysUser.getPassword(), authorities));
    }
}
