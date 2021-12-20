package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysPermissionController;
import cn.ruiheyun.athena.admin.entity.SysPermission;
import cn.ruiheyun.athena.admin.service.ISysPermissionService;
import cn.ruiheyun.athena.common.request.PageRequestDTO;
import cn.ruiheyun.athena.common.response.JsonResult;
import cn.ruiheyun.athena.common.util.CommonUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"/api/v1/admin/permission"})
public class SysPermissionControllerImpl implements ISysPermissionController {

    @Resource
    private ISysPermissionService sysPermissionService;

    @Override
    @RequestMapping(value = {"/page"})
    public Object page(@RequestBody JSONObject requestBody) {
        PageRequestDTO pageRequestDTO = requestBody.toJavaObject(PageRequestDTO.class);
        Page<SysPermission> permissionPage = sysPermissionService.lambdaQuery()
                .select(SysPermission::getId, SysPermission::getSn, SysPermission::getName, SysPermission::getIcon,
                        SysPermission::getUrl, SysPermission::getParentSn, SysPermission::getType)
                .like(SysPermission::getName, pageRequestDTO.getKeyword())
                .or().like(SysPermission::getUrl, pageRequestDTO.getKeyword())
                .page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));
        return Mono.just(permissionPage).map(pageData -> JsonResult.success("查询成功", pageData));
    }

    @Override
    @RequestMapping(value = {"/save"})
    public Object save(@RequestBody JSONObject requestBody) {
        SysPermission permission = requestBody.toJavaObject(SysPermission.class);
        return Mono.just(permission).map(sysPermission -> {
            if (StringUtils.isBlank(sysPermission.getSn())) {
                sysPermission.setSn(CommonUtils.uuid());
                return sysPermissionService.save(sysPermission);
            } else {
                return sysPermissionService.lambdaUpdate().set(SysPermission::getIcon, sysPermission.getIcon())
                        .set(SysPermission::getName, sysPermission.getName())
                        .set(SysPermission::getUrl, sysPermission.getUrl())
                        .set(SysPermission::getParentSn, sysPermission.getParentSn())
                        .set(SysPermission::getType, sysPermission.getType())
                        .eq(SysPermission::getSn, sysPermission.getSn()).update();
            }
        }).map(JsonResult::isSuccess);
    }

    @Override
    @RequestMapping(value = {"/delete"})
    public Object delete(@RequestParam String sn) {
        return Mono.just(sysPermissionService.lambdaUpdate().set(SysPermission::getDeleted, 1).update())
                .map(JsonResult::isSuccess);
    }
}
