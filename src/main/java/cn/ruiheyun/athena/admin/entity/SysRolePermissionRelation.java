package cn.ruiheyun.athena.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统角色菜单权限关系
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Data
@TableName("sys_role_permission_relation")
public class SysRolePermissionRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色唯一序列号
     */
    private String roleSn;

    /**
     * 菜单权限唯一序列号
     */
    private String permissionSn;

}
