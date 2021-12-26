package cn.ruiheyun.athena.auth.service.impl;

import cn.ruiheyun.athena.auth.entity.RSAKeyEntity;
import cn.ruiheyun.athena.auth.repository.IRSAKeyRepository;
import cn.ruiheyun.athena.auth.service.IRSAKeyService;
import cn.ruiheyun.athena.common.util.RSAUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;

@Service
public class RSAKeyServiceImpl implements IRSAKeyService {

    @Resource
    private IRSAKeyRepository keyRepository;

    @Override
    public Mono<RSAKeyEntity> findKey() {
        return keyRepository.findAll().singleOrEmpty();
    }

    @Override
    public Mono<Void> deleteKey() {
        return keyRepository.deleteAll();
    }

    @Override
    public Mono<RSAKeyEntity> saveKey() {
        return Mono.just(RSAUtils.getKeyPair())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(keyPair -> deleteKey().subscribe())
                .flatMap(keyPair -> {
                    RSAKeyEntity key = new RSAKeyEntity();
                    key.setPrivateKey(RSAUtils.getPrivateKey(keyPair));
                    key.setPublicKey(RSAUtils.getPublicKey(keyPair));
                    return keyRepository.save(key);
                });
    }
}
