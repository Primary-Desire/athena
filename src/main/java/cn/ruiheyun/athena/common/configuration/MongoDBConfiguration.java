package cn.ruiheyun.athena.common.configuration;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import cn.ruiheyun.athena.common.util.CommonUtils;
import cn.ruiheyun.athena.common.util.JsonWebTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMongoAuditing
public class MongoDBConfiguration {

    @Autowired
    private JsonWebTokenUtils jsonWebTokenUtils;

    @Bean
    public ReactiveBeforeConvertCallback<Object> reactiveBeforeConvertCallback() {
        return (entity, collection) -> ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> jsonWebTokenUtils.getSubjectForToken(String.valueOf(securityContext.getAuthentication().getPrincipal())))
                .onErrorResume(throwable -> Mono.empty())
                .map(username -> {
                    String sn = ((AbstractEntity) entity).getSn();
                    if (StringUtils.isBlank(sn)) {
                        ((AbstractEntity) entity).setSn(CommonUtils.uuid());
                        ((AbstractEntity) entity).setDeleted(0);
                        ((AbstractEntity) entity).setCreator(username);
                        ((AbstractEntity) entity).setUpdater(username);
                    } else {
                        ((AbstractEntity) entity).setUpdater(username);
                    }
                    return entity;
                }).defaultIfEmpty(entity).map(object -> {
                    if (StringUtils.isBlank(((AbstractEntity) object).getSn())) {
                        ((AbstractEntity) object).setSn(CommonUtils.uuid());
                    }
                    return entity;
                });
    }

}
