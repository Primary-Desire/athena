package cn.ruiheyun.athena.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

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
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录地区
     */
    private String lastLoginRegion;

    /**
     * 账号状态: 0.注册; 1.认证; 2.冻结
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
