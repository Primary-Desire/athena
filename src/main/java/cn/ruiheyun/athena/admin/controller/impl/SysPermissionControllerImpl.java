package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysPermissionController;
import cn.ruiheyun.athena.admin.entity.SysPermission;
import cn.ruiheyun.athena.admin.service.ISysPermissionService;
import cn.ruiheyun.athena.common.request.PageRequestDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"/api/v1/admin/permission"})
public class SysPermissionControllerImpl implements ISysPermissionController {

    @Resource
    private ISysPermissionService sysPermissionService;

    @Override
    @RequestMapping(value = {"/page"}, method = {RequestMethod.GET})
    public Object page(@RequestBody JSONObject requestBody) {
        PageRequestDTO pageRequestDTO = requestBody.toJavaObject(PageRequestDTO.class);
        Page<SysPermission> permissionPage = sysPermissionService.lambdaQuery()
                .select(SysPermission::getId, SysPermission::getSn, SysPermission::getName, SysPermission::getIcon,
                        SysPermission::getUrl, SysPermission::getParentSn, SysPermission::getType)
                .like(SysPermission::getName, pageRequestDTO.getKeyword())
                .or().like(SysPermission::getUrl, pageRequestDTO.getKeyword())
                .page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));
        return null;
    }

    @Override
    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST})
    public Object save(JSONObject requestBody) {
        return null;
    }

    @Override
    @RequestMapping(value = {"/delete"}, method = {RequestMethod.DELETE})
    public Object delete(String sn) {
        return null;
    }
}
