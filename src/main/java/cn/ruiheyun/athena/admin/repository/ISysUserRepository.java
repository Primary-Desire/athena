package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ISysUserRepository extends ReactiveMongoRepository<SysUser, String> {

    Mono<SysUser> findByUsername(String username);

    Mono<SysUser> findByEmail(String email);

}
