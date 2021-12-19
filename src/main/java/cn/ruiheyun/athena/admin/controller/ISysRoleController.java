package cn.ruiheyun.athena.admin.controller;

import cn.ruiheyun.athena.common.controller.IBaseController;
import com.alibaba.fastjson.JSONObject;

public interface ISysRoleController extends IBaseController {

    Object permissionRelationBind(JSONObject requestBody);

}
