package cn.ruiheyun.athena.auth.controller.impl;

import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.auth.controller.IAuthController;
import cn.ruiheyun.athena.common.response.JsonResult;
import cn.ruiheyun.athena.common.util.CommonUtils;
import cn.ruiheyun.athena.common.util.JsonWebTokenUtils;
import cn.ruiheyun.athena.common.util.RSAUtils;
import cn.ruiheyun.athena.common.util.StringRedisUtils;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.Objects;

@RestController
@RequestMapping(value = {"/api/v1/auth"})
public class AuthControllerImpl implements IAuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisUtils stringRedisUtils;
    @Autowired
    private JsonWebTokenUtils jsonWebTokenUtils;

    @Resource
    private ISysUserService sysUserService;

    @Override
    @RequestMapping(value = {"/getPublicKey"}, method = {RequestMethod.GET})
    public Object getPublicKey() {
        return Mono.just(stringRedisUtils.get(RSAUtils.PUBLIC_KEY))
                .map(publicKey -> JsonResult.success("获取成功", publicKey))
                .onErrorResume(throwable -> Mono.empty())
                .switchIfEmpty(Mono.just(JsonResult.failed("获取失败")));
    }

    @Override
    @RequestMapping(value = {"/signIn"}, method = {RequestMethod.POST})
    public Object signIn(@RequestBody JSONObject requestBody, ServerWebExchange exchange) {
        SysUser sysUser = requestBody.toJavaObject(SysUser.class);

        PrivateKey privateKey = RSAUtils.base64ToPrivateKey(stringRedisUtils.get(RSAUtils.PRIVATE_KEY));
        String username = RSAUtils.privateKeyDecrypt(sysUser.getUsername(), privateKey);
        String password = RSAUtils.privateKeyDecrypt(sysUser.getPassword(), privateKey);

        sysUser.setUsername(username);
        sysUser.setPassword(password);

        return sysUserService.findByUsername(sysUser.getUsername())
                .filter(userDetails -> passwordEncoder.matches(sysUser.getPassword(), userDetails.getPassword()))
                .map(userDetails -> jsonWebTokenUtils.generateToken(userDetails, CommonUtils.getRealIpAddress(exchange)))
                .doOnNext(token -> {
                    Claims claims = jsonWebTokenUtils.getClaimsForToken(token);
                    String ip = claims.get(JsonWebTokenUtils.CLAIM_KEY_IP, String.class);
                    sysUserService.updateLastLoginInfo(claims.getSubject(), ip);
                }).map(token -> JsonResult.success("登录成功", token))
                .onErrorResume(throwable -> Mono.empty())
                .switchIfEmpty(Mono.just(JsonResult.failed("登录失败")));
    }

    @Override
    @RequestMapping(value = {"/signUp"}, method = {RequestMethod.POST})
    public Object signUp(@RequestBody JSONObject requestBody) {
        SysUser sysUser = requestBody.toJavaObject(SysUser.class);

        PrivateKey privateKey = RSAUtils.base64ToPrivateKey(stringRedisUtils.get(RSAUtils.PRIVATE_KEY));
        String username = RSAUtils.privateKeyDecrypt(sysUser.getUsername(), privateKey);
        String password = RSAUtils.privateKeyDecrypt(sysUser.getPassword(), privateKey);

        sysUser.setSn(CommonUtils.uuid());
        sysUser.setUsername(username);
        sysUser.setPassword(passwordEncoder.encode(password));

        SysUser databaseUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()).one();
        if (!Objects.isNull(databaseUser)) {
            throw new RuntimeException("用户名重复");
        }

        return Mono.just(sysUserService.save(sysUser))
                .map(JsonResult::isSuccess)
                .onErrorResume(throwable -> Mono.empty())
                .switchIfEmpty(Mono.just(JsonResult.failed()));
    }
}
