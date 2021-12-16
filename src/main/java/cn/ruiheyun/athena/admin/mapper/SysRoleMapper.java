package cn.ruiheyun.athena.admin.mapper;

import cn.ruiheyun.athena.admin.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统角色 Mapper 接口
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> listByUserSn(String userSn);

}
