package cn.ruiheyun.athena.common.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document
public class AbstractEntity implements Serializable {

    @Indexed(unique = true)
    private String sn;

    private Integer deleted;

    @CreatedBy
    private String creator;

    @CreatedDate
    @Field("create_time")
    private LocalDateTime createTime;

    @LastModifiedBy
    private String updater;

    @LastModifiedDate
    @Field("update_time")
    private LocalDateTime updateTime;

}
