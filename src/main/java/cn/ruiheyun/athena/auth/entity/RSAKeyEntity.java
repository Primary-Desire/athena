package cn.ruiheyun.athena.auth.entity;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("RSA_KEY")
@EqualsAndHashCode(callSuper = true)
public class RSAKeyEntity extends AbstractEntity {

    @Field("private_key")
    private String privateKey;

    @Field("public_key")
    private String publicKey;

}
