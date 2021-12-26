package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysUserRoleRelation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ISysUserRoleRelationRepository extends ReactiveMongoRepository<SysUserRoleRelation, String> {

    Flux<SysUserRoleRelation> findAllByUserSn(String userSn);

}
