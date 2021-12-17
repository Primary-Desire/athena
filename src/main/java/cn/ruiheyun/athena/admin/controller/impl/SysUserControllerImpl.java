package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysUserController;
import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.common.response.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/admin/user"})
public class SysUserControllerImpl implements ISysUserController {

    @Resource
    private ISysUserService sysUserService;

    @Override
    @RequestMapping(value = {"/list"}, method = {RequestMethod.GET})
    public Object listUser() {
        List<SysUser> userList = sysUserService.lambdaQuery()
                .select(SysUser::getId, SysUser::getSn, SysUser::getUsername, SysUser::getLastLoginIp, SysUser::getLastLoginRegion, SysUser::getStatus)
                .list();
        return Mono.just(userList)
                .map(list -> JsonResult.success("查询成功", list));
    }
}
