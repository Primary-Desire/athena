package cn.ruiheyun.athena.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult {

    private Integer code;

    private String msg;

    private Object data;

    public static JsonResult isSuccess(boolean isSuccess) {
        return isSuccess ? success() : failed();
    }

    public static JsonResult success() {
        return success("成功");
    }

    public static JsonResult success(String msg) {
        return success(msg, null);
    }

    public static JsonResult success(String msg, Object data) {
        return response(1, msg, data);
    }

    public static JsonResult failed() {
        return failed("失败");
    }

    public static JsonResult failed(String msg) {
        return response(-1, msg, null);
    }

    public static JsonResult response(Integer code, String msg, Object data) {
        return new JsonResult(code, msg, data);
    }

}
