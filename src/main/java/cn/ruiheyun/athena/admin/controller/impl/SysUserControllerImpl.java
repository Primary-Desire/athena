package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysUserController;
import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.common.request.PageRequestDTO;
import cn.ruiheyun.athena.common.response.JsonResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"/api/v1/admin/user"})
public class SysUserControllerImpl implements ISysUserController {

    @Resource
    private ISysUserService sysUserService;

    @Override
    @RequestMapping(value = {"/page"}, method = {RequestMethod.GET})
    public Object page(@RequestBody JSONObject requestBody) {
        PageRequestDTO pageRequestDTO = requestBody.toJavaObject(PageRequestDTO.class);
        Page<SysUser> userPage = sysUserService.lambdaQuery()
                .select(SysUser::getId, SysUser::getSn, SysUser::getUsername, SysUser::getLastLoginIp, SysUser::getLastLoginRegion, SysUser::getStatus)
                .like(SysUser::getUsername, pageRequestDTO.getKeyword())
                .page(Page.of(pageRequestDTO.getCurrent(), pageRequestDTO.getPageSize()));
        return Mono.just(userPage)
                .map(pageData -> JsonResult.success("查询成功", pageData));
    }

    @Override
    @RequestMapping(value = {"/info"}, method = {RequestMethod.GET})
    public Object info(String sn) {
        return null;
    }

    @Override
    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST})
    public Object save(@RequestBody JSONObject requestBody) {
        return null;
    }

    @Override
    @RequestMapping(value = {"/delete"}, method = {RequestMethod.DELETE})
    public Object delete(String sn) {
        return null;
    }
}
