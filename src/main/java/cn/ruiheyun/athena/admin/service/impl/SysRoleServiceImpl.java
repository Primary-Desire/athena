package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysRole;
import cn.ruiheyun.athena.admin.repository.ISysRoleRepository;
import cn.ruiheyun.athena.admin.service.ISysRoleService;
import cn.ruiheyun.athena.admin.service.ISysUserRoleRelationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private ISysRoleRepository sysRoleRepository;
    @Resource
    private ISysUserRoleRelationService sysUserRoleRelationService;

    @Override
    public Mono<SysRole> findBySn(String sn) {
        return sysRoleRepository.findBySn(sn);
    }

    @Override
    public Flux<SysRole> findByUserSn(String userSn) {
        return sysUserRoleRelationService.findAllByUserSn(userSn)
                .flatMap(sysUserRoleRelation -> findBySn(sysUserRoleRelation.getRoleSn()));
    }
}
