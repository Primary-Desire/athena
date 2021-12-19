package cn.ruiheyun.athena.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 唯一序列号
     */
    private String sn;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否启用: 0.已启用; 1.已禁用
     */
    private Integer status;

    /**
     * 是否删除: 0.未删除; 1.已删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
