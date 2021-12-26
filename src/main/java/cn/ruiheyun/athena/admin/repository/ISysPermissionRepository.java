package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysPermission;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysPermissionRepository extends ReactiveMongoRepository<SysPermission, String> {
}
