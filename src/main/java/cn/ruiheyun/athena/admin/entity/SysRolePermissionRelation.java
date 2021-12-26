package cn.ruiheyun.athena.admin.entity;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("SYS_ROLE_PERMISSION_RELATION")
@EqualsAndHashCode(callSuper = true)
public class SysRolePermissionRelation extends AbstractEntity {

    @Field("role_sn")
    private String roleSn;

    @Field("permission_sn")
    private String permissionSn;

}
