package cn.ruiheyun.athena.common.util;

import cn.hutool.http.HttpUtil;
import cn.ruiheyun.athena.common.response.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CommonUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);
    }

    public static String md5(String src) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(src.getBytes(StandardCharsets.UTF_8));
            byte[] encryptBytes = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for(byte encryptByte : encryptBytes) {
                int val = encryptByte & 0XFF;
                if (val < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(val));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getRealIpAddress(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders httpHeaders = request.getHeaders();
        String ip = httpHeaders.getFirst("x-forwarded-for");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (StringUtils.isBlank(ip)|| "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        return ip.replaceAll(":", ".");
    }

    public static String getIpAddressRegion(String ip) {
        String url = "https://whois.pconline.com.cn/ip.jsp";
        return getIpAddressRegion(url, ip);
    }

    public static String getIpAddressRegion(String url, String ip) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ip", ip);
        return HttpUtil.get(url, paramMap).trim();
    }

    /**
     * 字节数组 转 base64 字符串
     * @param bytes
     * @return
     */
    public static String byteArrayToBase64(byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    /**
     * base64 字符串转 字节数组
     * @param base64
     * @return
     */
    public static byte[] base64ToByteArray(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64);
    }

    public static Mono<Void> response(ServerHttpResponse response, HttpStatus status) {
        return response(response, status, status.getReasonPhrase());
    }

    public static Mono<Void> response(ServerHttpResponse response, HttpStatus status, String msg) {
        response.setStatusCode(status);
        String responseBody = JSONObject.toJSONString(JsonResult.failed(msg));
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        DataBuffer body = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(body));
    }
}
