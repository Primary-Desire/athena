package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysUserController;
import cn.ruiheyun.athena.admin.entity.SysPermission;
import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.entity.SysUserRoleRelation;
import cn.ruiheyun.athena.admin.service.ISysPermissionService;
import cn.ruiheyun.athena.admin.service.ISysUserRoleRelationService;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.common.request.PageRequestDTO;
import cn.ruiheyun.athena.common.response.JsonResult;
import cn.ruiheyun.athena.common.util.JsonWebTokenUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = {"/api/v1/admin/user"})
public class SysUserControllerImpl implements ISysUserController {

    @Autowired
    private JsonWebTokenUtils jsonWebTokenUtils;

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysPermissionService sysPermissionService;
    @Resource
    private ISysUserRoleRelationService sysUserRoleRelationService;

    @Override
    @RequestMapping(value = {"/page"})
    public Object page(@RequestBody JSONObject requestBody) {
        PageRequestDTO pageRequestDTO = requestBody.toJavaObject(PageRequestDTO.class);
        Page<SysUser> userPage = sysUserService.lambdaQuery()
                .select(SysUser::getId, SysUser::getSn, SysUser::getUsername, SysUser::getLastLoginIp, SysUser::getLastLoginRegion, SysUser::getStatus)
                .like(SysUser::getUsername, pageRequestDTO.getKeyword())
                .page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));
        return Mono.just(userPage).map(pageData -> JsonResult.success("????????????", pageData));
    }

    @Override
    @RequestMapping(value = {"/update"})
    public Object save(@RequestBody JSONObject requestBody) {
        return null;
    }

    @Override
    @RequestMapping(value = {"/delete"})
    public Object delete(String sn) {
        return null;
    }

    @Override
    @RequestMapping(value = {"/role/relation/bind"})
    public Object roleRelationBind(@RequestBody JSONObject requestBody) {
        String userSn = requestBody.getString("userSn");
        String roles = requestBody.getString("roles");
        sysUserRoleRelationService.remove(sysUserRoleRelationService.lambdaQuery().eq(SysUserRoleRelation::getUserSn, userSn).getWrapper());
        Set<SysUserRoleRelation> userRoleRelationSet = new HashSet<>();
        for (String roleSn : roles.split(",")) {
            if (StringUtils.isNoneBlank(roleSn)) {
                SysUserRoleRelation userRoleRelation = new SysUserRoleRelation();
                userRoleRelation.setUserSn(userSn);
                userRoleRelation.setRoleSn(roleSn);
                userRoleRelationSet.add(userRoleRelation);
            }
        }
        return Mono.just(sysUserRoleRelationService.saveBatch(userRoleRelationSet)).map(JsonResult::isSuccess);
    }

    @Override
    @RequestMapping(value = {"/menu/list/get"})
    public Object getMenuList(ServerWebExchange exchange) {
        String username = jsonWebTokenUtils.getSubjectForToken(exchange.getAttribute("token"));
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        return Mono.just(SysPermission.convertToTree(sysPermissionService.listAllPermissionByUser(sysUser.getSn())))
                .map(sysPermissionList -> JsonResult.success("????????????", sysPermissionList));
    }

    @Override
    @RequestMapping(value = {"/default/open/selected/keys"})
    public Object getDefaultKeys(ServerWebExchange exchange) {
        String username = jsonWebTokenUtils.getSubjectForToken(exchange.getAttribute("token"));
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        List<SysPermission> permissionList = SysPermission.convertToTree(sysPermissionService.listAllPermissionByUser(sysUser.getSn()));
        return Mono.just(SysPermission.getDefaultKeys(permissionList))
                .map(result -> JsonResult.success("????????????", result));
    }
}
