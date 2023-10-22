package com.nowcoder.community.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(Integer code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(Integer code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(Integer code) {
        return getJSONString(code, null, null);
    }
}
