package cn.ruiheyun.athena.admin.service.impl;

import cn.ruiheyun.athena.admin.entity.SysPermission;
import cn.ruiheyun.athena.admin.mapper.SysPermissionMapper;
import cn.ruiheyun.athena.admin.service.ISysPermissionService;
import cn.ruiheyun.athena.common.request.PageRequestDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public List<SysPermission> listPermissionByParentSn(String parentSn, String keyword) {
        List<SysPermission> permissionList = lambdaQuery().eq(SysPermission::getParentSn, parentSn)
                .and(sysPermissionLambdaQueryWrapper -> sysPermissionLambdaQueryWrapper
                        .like(SysPermission::getName, keyword)
                        .or().like(SysPermission::getUrl, keyword)
                ).list();

        for (SysPermission permission : permissionList) {
            permission.setChildren(listPermissionByParentSn(permission.getSn(), keyword));
        }
        return permissionList;
    }

    @Override
    public Page<SysPermission> pagePermission(PageRequestDTO pageRequestDTO) {
        Page<SysPermission> permissionPage = lambdaQuery().eq(SysPermission::getParentSn, "")
                .and(sysPermissionLambdaQueryWrapper -> sysPermissionLambdaQueryWrapper
                        .like(SysPermission::getName, pageRequestDTO.getKeyword())
                        .or().like(SysPermission::getUrl, pageRequestDTO.getKeyword())
                ).page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));

        for (SysPermission permission : permissionPage.getRecords()) {
            permission.setChildren(listPermissionByParentSn(permission.getSn(), pageRequestDTO.getKeyword()));
        }
        return permissionPage;
    }
}
