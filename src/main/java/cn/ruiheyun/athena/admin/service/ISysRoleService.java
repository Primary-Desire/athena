package cn.ruiheyun.athena.admin.service;

import cn.ruiheyun.athena.admin.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> listByUserSn(String userSn);

}
