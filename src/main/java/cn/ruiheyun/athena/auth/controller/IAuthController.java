package cn.ruiheyun.athena.auth.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.server.ServerWebExchange;

public interface IAuthController {

    /**
     * 获取公钥
     * @return
     */
    Object getPublicKey();

    /**
     * 获取token
     * @param requestBody
     * @param exchange
     * @return
     */
    Object signIn(JSONObject requestBody, ServerWebExchange exchange);

    /**
     * 注册为系统用户
     * @param requestBody
     * @return
     */
    Object signUp(JSONObject requestBody);

}
