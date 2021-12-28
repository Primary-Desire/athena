package cn.ruiheyun.athena.admin.service;

import cn.ruiheyun.athena.admin.entity.SysUserRoleRelation;
import reactor.core.publisher.Flux;

public interface ISysUserRoleRelationService {

    Flux<SysUserRoleRelation> findAllByUserSn(String userSn);

    Flux<Void> deleteByUserSn(String userSn);

    Flux<SysUserRoleRelation> saveUserRoleRelation(String userSn, String...roleSnArray);

}
