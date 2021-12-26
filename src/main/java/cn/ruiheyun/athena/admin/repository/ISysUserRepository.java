package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysUserRepository extends ReactiveMongoRepository<SysUser, String> {
}
