package cn.ruiheyun.athena.admin.controller;

import cn.ruiheyun.athena.common.controller.IBaseController;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.server.ServerWebExchange;

public interface ISysUserController extends IBaseController {

    Object roleRelationBind(JSONObject requestBody);

    Object getMenuList(ServerWebExchange exchange);

}
