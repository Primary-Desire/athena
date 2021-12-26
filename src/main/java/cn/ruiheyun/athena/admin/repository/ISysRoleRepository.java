package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysRole;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysRoleRepository extends ReactiveMongoRepository<SysRole, String> {
}
