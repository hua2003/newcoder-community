package com.nowcoder.community;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.MessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MessageMapperTests {
    @Autowired
    private MessageMapper messageMapper;

    @Test
    void testInsert() {
        Message message = new Message();
        message.setFromId(1);
        message.setToId(1);
        message.setConversationId("123");
        User user = new User();
        Map<String, Object> map = new HashMap<>();
        map.put("user", "1q23");
        map.put("password", "1234566");
        message.setContent(JSONObject.toJSONString(map));
        messageMapper.insertMessage(message);
    }
}
