package cn.ruiheyun.athena.admin.entity;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("SYS_USER_ROLE_RELATION")
@EqualsAndHashCode(callSuper = true)
public class SysUserRoleRelation extends AbstractEntity {

    @Field("user_sn")
    private String userSn;

    @Field("role_sn")
    private String roleSn;

}
