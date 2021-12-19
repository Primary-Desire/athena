package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysPermission;
import cn.ruiheyun.athena.admin.mapper.SysPermissionMapper;
import cn.ruiheyun.athena.admin.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统菜单权限 服务实现类
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Override
    public List<SysPermission> listAllPermissionByUser(String userSn) {
        return baseMapper.listAllPermissionByUser(userSn);
    }
}
