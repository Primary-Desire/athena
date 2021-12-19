package cn.ruiheyun.athena.admin.mapper;

import cn.ruiheyun.athena.admin.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统菜单权限 Mapper 接口
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> listAllPermissionByUser(String userSn);

}
