package cn.ruiheyun.athena.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统用户角色关系
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Data
@TableName("sys_user_role_relation")
public class SysUserRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户唯一序列号
     */
    private String userSn;

    /**
     * 角色唯一序列号
     */
    private String roleSn;

}
