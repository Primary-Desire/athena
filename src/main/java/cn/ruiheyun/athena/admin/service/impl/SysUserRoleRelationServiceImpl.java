package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysUserRoleRelation;
import cn.ruiheyun.athena.admin.repository.ISysUserRoleRelationRepository;
import cn.ruiheyun.athena.admin.service.ISysUserRoleRelationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Arrays;

@Service
public class SysUserRoleRelationServiceImpl implements ISysUserRoleRelationService {

    @Resource
    private ISysUserRoleRelationRepository sysUserRoleRelationRepository;

    @Override
    public Flux<SysUserRoleRelation> findAllByUserSn(String userSn) {
        return sysUserRoleRelationRepository.findAllByUserSn(userSn);
    }

    @Override
    public Flux<Void> deleteByUserSn(String userSn) {
        return findAllByUserSn(userSn).flatMap(sysUserRoleRelation -> sysUserRoleRelationRepository.delete(sysUserRoleRelation));
    }

    @Override
    public Flux<SysUserRoleRelation> saveUserRoleRelation(String userSn, String... roleSnArray) {
        return Flux.fromIterable(Arrays.asList(roleSnArray))
                .map(roleSn -> new SysUserRoleRelation(userSn, roleSn))
                .flatMap(sysUserRoleRelation -> sysUserRoleRelationRepository.save(sysUserRoleRelation));
    }
}
