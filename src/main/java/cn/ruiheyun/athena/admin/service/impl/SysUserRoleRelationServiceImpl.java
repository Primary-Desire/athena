package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysUserRoleRelation;
import cn.ruiheyun.athena.admin.repository.ISysUserRoleRelationRepository;
import cn.ruiheyun.athena.admin.service.ISysUserRoleRelationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Service
public class SysUserRoleRelationServiceImpl implements ISysUserRoleRelationService {

    @Resource
    private ISysUserRoleRelationRepository sysUserRoleRelationRepository;

    @Override
    public Flux<SysUserRoleRelation> findAllByUserSn(String userSn) {
        return sysUserRoleRelationRepository.findAllByUserSn(userSn);
    }
}
