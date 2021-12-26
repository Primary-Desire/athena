package cn.ruiheyun.athena.auth.service;

import cn.ruiheyun.athena.auth.entity.RSAKeyEntity;
import reactor.core.publisher.Mono;

public interface IRSAKeyService {

    Mono<RSAKeyEntity> findKey();

    Mono<Void> deleteKey();

    Mono<RSAKeyEntity> saveKey();

}
