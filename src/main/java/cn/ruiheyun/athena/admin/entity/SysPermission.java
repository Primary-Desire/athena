package cn.ruiheyun.athena.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 系统菜单权限
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Data
@TableName("sys_permission")
public class SysPermission implements Serializable {

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
     * 菜单权限名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路径
     */
    private String url;

    /**
     * 类型: 0.一级菜单; 1.子菜单; 2.按钮/权限
     */
    private Integer type;

    /**
     * 父级菜单权限唯一序列号
     */
    private String parentSn;

    /**
     * 排序, 0为默认选中
     */
    private Integer sort;

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

    @TableField(exist = false)
    private List<SysPermission> children;

    public static List<SysPermission> convertToTree(List<SysPermission> sysPermissionList) {
        List<SysPermission> permissionList = new ArrayList<>();
        Iterator<SysPermission> iterator = sysPermissionList.iterator();
        while (iterator.hasNext()) {
            SysPermission sysPermission = iterator.next();
            if (StringUtils.isNotBlank(sysPermission.getParentSn())) {
                continue;
            }
            sysPermission.setChildren(getChildren(sysPermission.getSn(), sysPermissionList));
            permissionList.add(sysPermission);
            iterator.remove();
        }
        return permissionList;
    }

    private static List<SysPermission> getChildren(String sn, List<SysPermission> sysPermissionList) {
        List<SysPermission> childrenList = new ArrayList<>();
        Iterator<SysPermission> iterator = sysPermissionList.iterator();
        while (iterator.hasNext()) {
            SysPermission sysPermission = iterator.next();
            if (!sn.equals(sysPermission.getParentSn())) {
                continue;
            }
            childrenList.add(sysPermission);
            iterator.remove();
        }
        for (SysPermission sysPermission : childrenList) {
            sysPermission.setChildren(getChildren(sysPermission.getSn(), sysPermissionList));
        }
        return childrenList;
    }

}
