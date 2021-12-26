package cn.ruiheyun.athena.admin.entity;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("SYS_PERMISSION")
@EqualsAndHashCode(callSuper = true)
public class SysPermission extends AbstractEntity {

    private String name;

    private String icon;

    private String url;

    private Integer sort;

    @Field("parent_sn")
    private String parentSn;

}
