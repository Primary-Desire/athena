package cn.ruiheyun.athena.admin.entity;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("SYS_ROLE")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends AbstractEntity {

    private String name;

}
