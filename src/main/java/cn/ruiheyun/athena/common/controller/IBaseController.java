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
     * 获取详细
     * @param sn
     * @return
     */
    Object info(String sn);

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
