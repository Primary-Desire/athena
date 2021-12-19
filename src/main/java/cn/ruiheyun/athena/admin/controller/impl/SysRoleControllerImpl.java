package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysRoleController;
import cn.ruiheyun.athena.admin.entity.SysRole;
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

@RestController
@RequestMapping(value = {"/api/v1/admin/role"})
public class SysRoleControllerImpl implements ISysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @Override
    @RequestMapping(value = {"/page"}, method = {RequestMethod.GET})
    public Object page(@RequestBody JSONObject requestBody) {
        PageRequestDTO pageRequestDTO = requestBody.toJavaObject(PageRequestDTO.class);
        Page<SysRole> rolePage = sysRoleService.lambdaQuery()
                .select(SysRole::getId, SysRole::getSn, SysRole::getName, SysRole::getStatus)
                .like(SysRole::getName, pageRequestDTO.getKeyword())
                .page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));
        return Mono.just(rolePage).map(pageData -> JsonResult.success("查询成功", pageData));
    }

    @Override
    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST})
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
    @RequestMapping(value = {"/delete"}, method = {RequestMethod.DELETE})
    public Object delete(@RequestParam String sn) {
        return Mono.just(sysRoleService.lambdaUpdate().set(SysRole::getDeleted, 1).eq(SysRole::getSn, sn).update())
                .map(JsonResult::isSuccess);
    }
}
