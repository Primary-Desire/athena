package cn.ruiheyun.athena.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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

}
