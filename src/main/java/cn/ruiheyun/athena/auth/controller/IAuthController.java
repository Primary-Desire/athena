package cn.ruiheyun.athena.auth.controller;

import com.alibaba.fastjson.JSONObject;

public interface IAuthController {

    Object getPublicKey();

    Object signUp(JSONObject requestBody);

    Object signIn(JSONObject requestBody);

}
