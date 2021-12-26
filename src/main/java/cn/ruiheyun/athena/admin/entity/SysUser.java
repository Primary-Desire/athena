package cn.ruiheyun.athena.admin.entity;

import cn.ruiheyun.athena.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("SYS_USER")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends AbstractEntity {

    @Indexed
    private String username;

    @Indexed
    private String email;

    private String password;

}
