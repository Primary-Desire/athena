package cn.ruiheyun.athena.auth.repository;

import cn.ruiheyun.athena.auth.entity.RSAKeyEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRSAKeyRepository extends ReactiveMongoRepository<RSAKeyEntity, String> {
}
