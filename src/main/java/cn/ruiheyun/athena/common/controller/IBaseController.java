package cn.ruiheyun.athena.common.controller;

import com.alibaba.fastjson.JSONObject;

public interface IBaseController {

    /**
     * 分页列表
     * @param requestBody
     * @return
     */
    Object page(JSONObject requestBody);

    /**
     * 保存
     * @param requestBody
     * @return
     */
    Object save(JSONObject requestBody);

    /**
     * 删除
     * @param sn
     * @return
     */
    Object delete(String sn);

}
