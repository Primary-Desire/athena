package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysRole;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ISysRoleRepository extends ReactiveMongoRepository<SysRole, String> {

    Mono<SysRole> findBySn(String sn);

    Mono<SysRole> findByName(String name);

}
