package cn.ruiheyun.athena.admin.controller.impl;

import cn.ruiheyun.athena.admin.controller.ISysRoleController;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/admin/role"})
public class SysRoleControllerImpl implements ISysRoleController {
    @Override
    @RequestMapping(value = {"/page"}, method = {RequestMethod.GET})
    public Object page(JSONObject requestBody) {
        return null;
    }

    @Override
    @RequestMapping(value = {"/info"}, method = {RequestMethod.GET})
    public Object info(String sn) {
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
