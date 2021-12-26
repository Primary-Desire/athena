package cn.ruiheyun.athena.auth.controller.impl;

import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.auth.controller.IAuthController;
import cn.ruiheyun.athena.common.response.JsonResult;
import cn.ruiheyun.athena.common.util.RSAUtils;
import cn.ruiheyun.athena.common.util.ReactiveStringRedisUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements IAuthController {

    @Autowired
    private ReactiveStringRedisUtils reactiveStringRedisUtils;

    @Resource
    private ISysUserService sysUserService;

    @Override
    @RequestMapping("/getPublicKey")
    public Object getPublicKey() {
        return reactiveStringRedisUtils.get(RSAUtils.PUBLIC_KEY)
                .map(publicKey -> JsonResult.success("获取成功", publicKey));
    }

    @Override
    @RequestMapping("/signUp")
    public Object signUp(@RequestBody JSONObject requestBody) {
        return Mono.just(requestBody).map(request -> request.toJavaObject(SysUser.class))
                .flatMap(sysUser -> sysUserService.save(sysUser))
                .map(sysUser -> !Objects.isNull(sysUser))
                .map(JsonResult::isSuccess);
    }

    @Override
    @RequestMapping("/signIn")
    public Object signIn(@RequestBody JSONObject requestBody) {
        return null;
    }
}
