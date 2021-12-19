package cn.ruiheyun.athena.admin.service;

import cn.ruiheyun.athena.admin.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统菜单权限 服务类
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermission> listAllPermissionByUser(String userSn);

}
