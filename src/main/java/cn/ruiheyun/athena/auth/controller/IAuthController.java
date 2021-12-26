package cn.ruiheyun.athena.auth.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.server.ServerWebExchange;

public interface IAuthController {

    Object getPublicKey();

    Object signUp(JSONObject requestBody);

    Object signIn(JSONObject requestBody, ServerWebExchange exchange);

}
