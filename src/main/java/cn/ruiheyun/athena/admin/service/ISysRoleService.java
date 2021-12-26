package cn.ruiheyun.athena.admin.service;

import cn.ruiheyun.athena.admin.entity.SysRole;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISysRoleService {

    Mono<SysRole> findBySn(String sn);

    Flux<SysRole> findByUserSn(String userSn);

}
