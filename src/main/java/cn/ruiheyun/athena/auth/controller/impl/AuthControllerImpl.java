package cn.ruiheyun.athena.auth.controller.impl;

import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.auth.controller.IAuthController;
import cn.ruiheyun.athena.auth.service.IRSAKeyService;
import cn.ruiheyun.athena.common.response.JsonResult;
import cn.ruiheyun.athena.common.util.CommonUtils;
import cn.ruiheyun.athena.common.util.JsonWebTokenUtils;
import cn.ruiheyun.athena.common.util.RSAUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements IAuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JsonWebTokenUtils jsonWebTokenUtils;

    @Resource
    private IRSAKeyService keyService;
    @Resource
    private ISysUserService sysUserService;

    @Override
    @RequestMapping("/getPublicKey")
    public Object getPublicKey() {
        return keyService.findKey()
                .map(key -> JsonResult.success("获取成功", key.getPublicKey()))
                .onErrorResume(throwable -> Mono.empty())
                .defaultIfEmpty(JsonResult.failed("获取失败"));
    }

    @Override
    @RequestMapping("/signUp")
    public Object signUp(@RequestBody JSONObject requestBody) {
        return Mono.just(requestBody).map(request -> request.toJavaObject(SysUser.class))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(sysUser -> keyService.findKey()
                        .doOnNext(key -> {
                            PrivateKey privateKey = RSAUtils.base64ToPrivateKey(key.getPrivateKey());
                            sysUser.setUsername(RSAUtils.privateKeyDecrypt(sysUser.getUsername(), privateKey));
                            sysUser.setEmail(RSAUtils.privateKeyDecrypt(sysUser.getEmail(), privateKey));
                            sysUser.setPassword(passwordEncoder.encode(RSAUtils.privateKeyDecrypt(sysUser.getPassword(), privateKey)));
                        }).subscribe()).flatMap(sysUser -> sysUserService.save(sysUser))
                .map(sysUser -> !Objects.isNull(sysUser))
                .map(JsonResult::isSuccess);
    }

    @Override
    @RequestMapping("/signIn")
    public Object signIn(@RequestBody JSONObject requestBody, ServerWebExchange exchange) {
        return Mono.just(requestBody).map(request -> request.toJavaObject(SysUser.class))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(sysUser -> keyService.findKey()
                        .doOnNext(key -> {
                            PrivateKey privateKey = RSAUtils.base64ToPrivateKey(key.getPrivateKey());
                            sysUser.setUsername(RSAUtils.privateKeyDecrypt(sysUser.getUsername(), privateKey));
                            sysUser.setEmail(RSAUtils.privateKeyDecrypt(sysUser.getEmail(), privateKey));
                            sysUser.setPassword(RSAUtils.privateKeyDecrypt(sysUser.getPassword(), privateKey));
                        }).subscribe())
                .flatMap(sysUser -> sysUserService.findByUsername(sysUser.getUsername())
                        .filter(userDetails -> passwordEncoder.matches(sysUser.getPassword(), userDetails.getPassword()))
                ).map(userDetails -> jsonWebTokenUtils.generateToken(userDetails, CommonUtils.getRealIpAddress(exchange)))
                .map(token -> JsonResult.success("登录成功", token))
                .onErrorResume(throwable -> Mono.empty())
                .defaultIfEmpty(JsonResult.failed("登录失败"));
    }
}
