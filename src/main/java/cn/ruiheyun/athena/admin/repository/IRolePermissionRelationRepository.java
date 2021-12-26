package cn.ruiheyun.athena.admin.repository;

import cn.ruiheyun.athena.admin.entity.SysRolePermissionRelation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolePermissionRelationRepository extends ReactiveMongoRepository<SysRolePermissionRelation, String> {
}
