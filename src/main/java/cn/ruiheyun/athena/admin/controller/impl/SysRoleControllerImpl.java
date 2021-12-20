package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysRoleController;
import cn.ruiheyun.athena.admin.entity.SysRole;
import cn.ruiheyun.athena.admin.entity.SysRolePermissionRelation;
import cn.ruiheyun.athena.admin.service.ISysRolePermissionRelationService;
import cn.ruiheyun.athena.admin.service.ISysRoleService;
import cn.ruiheyun.athena.common.request.PageRequestDTO;
import cn.ruiheyun.athena.common.response.JsonResult;
import cn.ruiheyun.athena.common.util.CommonUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = {"/api/v1/admin/role"})
public class SysRoleControllerImpl implements ISysRoleController {

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysRolePermissionRelationService sysRolePermissionRelationService;

    @Override
    @RequestMapping(value = {"/page"})
    public Object page(@RequestBody JSONObject requestBody) {
        PageRequestDTO pageRequestDTO = requestBody.toJavaObject(PageRequestDTO.class);
        Page<SysRole> rolePage = sysRoleService.lambdaQuery()
                .select(SysRole::getId, SysRole::getSn, SysRole::getName, SysRole::getStatus)
                .like(SysRole::getName, pageRequestDTO.getKeyword())
                .page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));
        return Mono.just(rolePage).map(pageData -> JsonResult.success("查询成功", pageData));
    }

    @Override
    @RequestMapping(value = {"/save"})
    public Object save(@RequestBody JSONObject requestBody) {
        SysRole saveRole = requestBody.toJavaObject(SysRole.class);
        return Mono.just(saveRole).map(sysRole -> {
            if (StringUtils.isBlank(sysRole.getSn())) {
                sysRole.setSn(CommonUtils.uuid());
                return sysRoleService.save(sysRole);
            } else {
                return sysRoleService.lambdaUpdate().set(SysRole::getName, sysRole.getName())
                        .eq(SysRole::getSn, saveRole.getSn()).update();
            }
        }).map(JsonResult::isSuccess);
    }

    @Override
    @RequestMapping(value = {"/delete"})
    public Object delete(@RequestParam String sn) {
        return Mono.just(sysRoleService.lambdaUpdate().set(SysRole::getDeleted, 1).eq(SysRole::getSn, sn).update())
                .map(JsonResult::isSuccess);
    }

    @Override
    @RequestMapping(value = {"/permission/relation/bind"})
    public Object permissionRelationBind(@RequestBody JSONObject requestBody) {
        String roleSn = requestBody.getString("roleSn");
        String permissions = requestBody.getString("permissions");
        sysRolePermissionRelationService.remove(sysRolePermissionRelationService.lambdaQuery().eq(SysRolePermissionRelation::getRoleSn, roleSn).getWrapper());
        Set<SysRolePermissionRelation> permissionRelationSet = new HashSet<>();
        for (String permissionSn : permissions.split(",")) {
            if (StringUtils.isNoneBlank(permissionSn)) {
                SysRolePermissionRelation permission = new SysRolePermissionRelation();
                permission.setRoleSn(roleSn);
                permission.setPermissionSn(permissionSn);
                permissionRelationSet.add(permission);
            }
        }
        return Mono.just(sysRolePermissionRelationService.saveBatch(permissionRelationSet)).map(JsonResult::isSuccess);
    }
}
